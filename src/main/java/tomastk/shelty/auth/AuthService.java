package tomastk.shelty.auth;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tomastk.shelty.auth.Request.UserRequest;
import tomastk.shelty.auth.Request.ValidCodeRequest;
import tomastk.shelty.jwtconfig.TokenService;
import tomastk.shelty.models.entities.UserData;
import tomastk.shelty.models.validators.UserRegisterValidator;
import tomastk.shelty.services.impl.AdminSecurityContextHandler;
import tomastk.shelty.services.impl.UserDataImpl;
import tomastk.shelty.user.*;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Autowired
    private final Logger logger;
    private final UserDataImpl dataService;
    private final UserCodeRepository userCodeRepository;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    private void sendUserCode(User user) {
        UserCode userCode = saveUserCode();
        // ToDo: Enviar el código por email

        user.setUserCode(userCode);
        userRepository.save(user);
    }

    private void deleteUserCode(User user) {
        UserCode userCode = user.getUserCode();
        user.setUserCode(null);

        userRepository.save(user);
        userCodeRepository.delete(userCode);
    }

    private UserCode saveUserCode() {
        UserCode userCode = UserCode.builder()
                .generatedAt(new Date())
                .code(generateUserCode())
                .build();
        return userCodeRepository.save(userCode);
    }


    private boolean codeIsNotExpired(UserCode userCode) {
        int minutes = 10;
        long miliseconds = minutes * 60 * 1000;
        return userCode.getGeneratedAt().getTime() + miliseconds > new Date().getTime();
    }


    private String generateUserCode() {
        return String.valueOf((int) (Math.random() * 900000) + 100000);
    }

    private void crearUserData(User user) {
        dataService.save(UserData.builder()
                        .id(user.getId())
                .build());
    }

    public AuthResponse register(UserRequest registerDetails) {
        final UserRegisterValidator userRegisterValidator = new UserRegisterValidator();
        Map<String, String> requestErrors = userRegisterValidator.validate(registerDetails);

        if (!requestErrors.isEmpty()) {
            return AuthResponse.builder().authError(requestErrors).build();
        }

        User userToRegister = User.builder()
                .username(registerDetails.getUsername())
                .email(registerDetails.getEmail())
                .password(passwordEncoder.encode(registerDetails.getPassword()))
                .role(Role.USER)
                .build();

        Optional<User> userExisting = userRepository.findByUsername(userToRegister.getUsername());

        if (userExisting.isPresent()) {
            return AuthResponse.builder()
                    .token(null)
                    .authError("El username ya está en uso")
                    .build();
        }
        try {
            sendUserCode(userToRegister);
            crearUserData(userToRegister);
        } catch (DataAccessException ex){
            logger.error(ex.getMessage());
            return AuthResponse.builder()
                    .token(null)
                    .authError("Error al registrar el usuario")
                    .build();
        }

        return AuthResponse.builder()
                .token(tokenService.getToken(userToRegister))
                .build();
    }

    public UserDTO login(LoginRequest loginDetails) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDetails.getUsername(), loginDetails.getPassword()));
        } catch (RuntimeException ex) {
           return UserDTO
                   .builder()
                   .authError("Bad credentials")
                   .build();
        }

        User userToGetLogged = userRepository.findByUsername(loginDetails.getUsername()).orElseThrow();

        String token = tokenService.getToken(userToGetLogged);
        return UserDTO.builder()
                .token(token)
                .username(userToGetLogged.getUsername())
                .role(userToGetLogged.getRole())
                .build();

    }

    public AuthResponse validCode(ValidCodeRequest validCodeRequest) {
        User user = userRepository.findByUsername(validCodeRequest.getUsername()).orElse(null);

        if (user == null || user.getUserCode() == null) {
            return AuthResponse
                    .builder()
                    .authError("El usuario no existe o no tiene un código de verificación")
                    .build();
        }
        String codeUser = user.getUserCode().getCode();
        if (!validCodeRequest.getCode().equals(codeUser)) {
            return AuthResponse
                    .builder()
                    .authError("El código es incorrecto")
                    .build();
        }
        if (!codeIsNotExpired(user.getUserCode())) {
            deleteUserCode(user);
            sendUserCode(user);
            return AuthResponse
                    .builder()
                    .authError("El código ha expirado")
                    .build();
        }

        deleteUserCode(user);
        String token = tokenService.getToken(user);

        if (!user.isVerified()){
            user.setVerified(true);
            userRepository.save(user);
        }
        return AuthResponse
                .builder()
                .token(token)
                .build();
    }

    public AuthResponse forgotPassword(String username) {
        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            return AuthResponse
                    .builder()
                    .authError("El usuario no existe")
                    .build();
        }

        if (!user.isVerified()) {
            return AuthResponse
                    .builder()
                    .authError("El usuario no ha sido verificado")
                    .build();
        }

        if (user.getUserCode() != null)  {
            return AuthResponse
                    .builder()
                    .authError("El usuario ya tiene un código")
                    .build();
        }

        user.setUserCode(saveUserCode());
        userRepository.save(user);

        return AuthResponse
                .builder()
                .message("Código enviado")
                .token(null)
                .build();
    }

    public AuthResponse resetPassword(String username, String newPassword) {
        User userToResetPassword = userRepository.findByUsername(username).orElse(null);

        if (userToResetPassword == null) {
            return AuthResponse
                    .builder()
                    .authError("El usuario no existe")
                    .build();
        }
        if (!userHasPermissions(userToResetPassword.getId())) {
            return AuthResponse
                    .builder()
                    .authError("No puedes cambiar la contraseña de otro usuario")
                    .build();
        }
        userToResetPassword.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(userToResetPassword);
        return AuthResponse
                .builder()
                .message("Contraseña cambiada")
                .build();
    }

    public boolean userHasPermissions(long userId) {
        return userId == AdminSecurityContextHandler.getUser().getId() || AdminSecurityContextHandler.getUserRole() == Role.ADMIN;
    }
}
