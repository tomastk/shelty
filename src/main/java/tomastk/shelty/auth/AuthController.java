package tomastk.shelty.auth;

import lombok.AllArgsConstructor;
import tomastk.shelty.auth.Request.ResetPasswordRequest;
import tomastk.shelty.auth.Request.ValidCodeRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tomastk.shelty.auth.Request.UserRequest;
import tomastk.shelty.user.UserDTO;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<UserDTO> loginHandler(@RequestBody LoginRequest loginDetails){
        return ResponseEntity.ok(authService.login(loginDetails));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerHandler(@RequestBody UserRequest registerDetails){
        return ResponseEntity.ok(authService.register(registerDetails));
    }

    @PostMapping("/valid-code")
    public ResponseEntity<AuthResponse> validCodeHandler(@RequestBody ValidCodeRequest validCodeRequest){
        return ResponseEntity.ok(authService.validCode(validCodeRequest));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<AuthResponse> forgotPasswordHandler(@RequestParam String username){
        return ResponseEntity.ok(authService.forgotPassword(username));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<AuthResponse> resetPasswordHandler(@RequestBody ResetPasswordRequest resetPasswordRequest){
        return ResponseEntity.ok(authService.resetPassword(resetPasswordRequest.getUsername(), resetPasswordRequest.getPassword()));
    }

}