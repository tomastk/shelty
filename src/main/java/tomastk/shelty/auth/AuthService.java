package tomastk.shelty.auth;

import ch.qos.logback.core.net.server.Client;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tomastk.shelty.auth.RegisterRequest.UserRequest;
import tomastk.shelty.jwtconfig.TokenService;
import tomastk.shelty.models.daos.UserDataDAO;
import tomastk.shelty.models.entities.UserData;
import tomastk.shelty.models.validators.UserRegisterValidator;
import tomastk.shelty.services.impl.UserDataImpl;
import tomastk.shelty.user.Role;
import tomastk.shelty.user.User;
import tomastk.shelty.user.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserDataImpl dataService;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

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
                .password(passwordEncoder.encode(registerDetails.getPassword()))
                .role(Role.USER)
                .build();
        Optional<User> userExisting = userRepository.findByUsername(userToRegister.getUsername());
        try {
            User savedUser = userRepository.save(userToRegister);
            crearUserData(savedUser);
        } catch (DataAccessException ex){
            return AuthResponse.builder()
                    .token(null)
                    .authError("El username ya está en uso")
                    .build();
        }
        return AuthResponse.builder()
                .token(tokenService.getToken(userToRegister))
                .build();
    }

    public AuthResponse login(LoginRequest loginDetails) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDetails.getUsername(), loginDetails.getPassword()));
        } catch (RuntimeException ex) {
            return AuthResponse.builder()
                .token(null)
                .authError(ex.getMessage())
                .build();
        }

        UserDetails userToGetLogged = userRepository.findByUsername(loginDetails.getUsername()).orElseThrow();
        String token = tokenService.getToken(userToGetLogged);
        return AuthResponse.builder()
                .token(token)
                .build();

    }
}
