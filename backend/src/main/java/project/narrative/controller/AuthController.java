package project.narrative.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.narrative.dto.UserDTO;
import project.narrative.service.AuthService;
import reactor.core.publisher.Mono;

import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest request)
            throws NoSuchAlgorithmException {
        if (!request.password.equals(request.confirmPassword)) {
            return ResponseEntity.badRequest().body(null);
        }

        UserDTO newUserDTO = authService.register(
            request.userId,
            request.username,
            request.password,
            request.email,
            request.role
        );

        return ResponseEntity.ok(newUserDTO);
    }

    @PostMapping("/signin")
    public Mono<ResponseEntity<String>> signInUser(@RequestBody SignInRequest request) {

        return authService.login(
            request.email,
            request.password
        ).map(response -> ResponseEntity.ok("User registered successfully"))
            .onErrorReturn(ResponseEntity.status(500).body("Sign in failed."));
    }

    public static class SignUpRequest {
        public UUID userId;
        public String username;
        public String email;
        public String password;
        public String confirmPassword;
        public String role;
    }

    public static class SignInRequest {
        public String email;
        public String password;
    }

}