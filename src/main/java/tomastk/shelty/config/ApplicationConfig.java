package tomastk.shelty.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import tomastk.shelty.models.validators.AnimalValidator;
import tomastk.shelty.user.Role;
import tomastk.shelty.user.User;
import tomastk.shelty.user.UserRepository;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    @Value("${admin.super-admin.password}")
    private String ADMIN_PASSWORD;

    @Autowired
    private final UserRepository userRepository;

    @Bean
    public Logger logger() {
        return LoggerFactory.getLogger("APP Logger");
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @PostConstruct
    public void init() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!userRepository.findByUsername("superadmin").isPresent()) {
            User superAdmin = User.builder()
                    .username("superadmin")
                    .password(encoder.encode(ADMIN_PASSWORD))
                    .role(Role.SUPER_ADMIN)
                    .build();
            userRepository.save(superAdmin);
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        };
    }

    @Bean
    public AnimalValidator animalValidator() {
        return new AnimalValidator();
    }

}
