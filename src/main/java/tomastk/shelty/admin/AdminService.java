package tomastk.shelty.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tomastk.shelty.jwtconfig.TokenService;
import tomastk.shelty.user.Role;
import tomastk.shelty.user.User;
import tomastk.shelty.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public AdminResponse createAdmin(AdminRequest createDetails) {
        User admin = User.builder()
                .role(Role.ADMIN)
                .password(passwordEncoder.encode(createDetails.getPassword()))
                .username(createDetails.getUsername())
                .build();
        Optional<User> optAdmin = userRepository.findByUsername(createDetails.getUsername());
        if (optAdmin.isPresent()) {
            return AdminResponse.builder()
                    .authError("An administrator with those credentials already exists.")
                    .build();
        }
        userRepository.save(admin);
        return AdminResponse.builder()
                .token(tokenService.getToken(admin))
                .build();

    }

    public AdminResponse updateAdmin(User adminToUpdate) {

        boolean adminExists = userRepository.existsById(adminToUpdate.getId());
        adminToUpdate.setPassword(passwordEncoder.encode(adminToUpdate.getPassword()));


        adminToUpdate.setRole(Role.ADMIN);
        if (!adminExists) {
            return AdminResponse.builder()
                    .authError("There is not administrator with those credentials")
                    .build();
        }

        try {
            userRepository.save(adminToUpdate);
            String adminToken = tokenService.getToken(adminToUpdate);
            return AdminResponse
                    .builder()
                    .token(adminToken)
                    .build();

        } catch (RuntimeException ex ) {
            return AdminResponse.builder()
                    .authError("There is an error creating this administrator")
                    .build();
        }



    }

    public String deleteAdmin(int idAdminToDelete) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User admin = (User) authentication.getPrincipal();

        int idAdminAuthenticated = admin.getId();


        if (idAdminToDelete != idAdminAuthenticated && !admin.getRole().equals(Role.SUPER_ADMIN)) {
            return "You don't have the permissions to delete another admin.";
        }

        try {
            userRepository.deleteById(idAdminToDelete);
        } catch (RuntimeException ex ) {
            return ex.getMessage();
        }

        return "Admin deleted succesfully";
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public List<User> getUsersByRole(Role role) {
        List<User> allUsers = userRepository.findAll();
        List<User> roleUsers = new ArrayList<>();

        for (User user : allUsers) {
            if (user.getRole().equals(role)) {
                roleUsers.add(user);
            }
        }

        return roleUsers;

    }
}
