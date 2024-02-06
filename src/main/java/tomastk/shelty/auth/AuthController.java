package tomastk.shelty.auth;

import lombok.AllArgsConstructor;
import tomastk.shelty.user.Role;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tomastk.shelty.auth.RegisterRequest.UserRequest;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginHandler(@RequestBody LoginRequest loginDetails){
        return ResponseEntity.ok(authService.login(loginDetails));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerHandler(@RequestBody UserRequest registerDetails){
        return ResponseEntity.ok(authService.register(registerDetails));
    }

}