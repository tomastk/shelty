package tomastk.shelty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import tomastk.shelty.user.Role;
import tomastk.shelty.user.User;
import tomastk.shelty.user.UserRepository;

@SpringBootApplication
public class SheltyApplication {



	public static void main(String[] args) {
		SpringApplication.run(SheltyApplication.class, args);
	}

}
