package project.narrative.service;

import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.client.WebClient;
import project.narrative.controller.AuthController;
import project.narrative.dto.UserDTO;
import project.narrative.model.entities.User;
import project.narrative.repository.UserRepo;
import reactor.core.publisher.Mono;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private UserRepo userRepository;
    private UserService userService;

    @Value("${supabase.url}")
    private String baseUrl;

    @Value("${supabase.anon.key}")
    private String anonKey;

    private final WebClient webClient;

    @Autowired
    public AuthService(UserRepo userRepository) {
        this.userRepository = userRepository;
        this.userService = new UserService(userRepository);
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    @Transactional
    public UserDTO register(UUID userId, String username, String password, String email, String role) throws NoSuchAlgorithmException {

//        Optional<User> existingUsername = userRepository.findByUsername(username);
//        Optional<User> existingEmail = userRepository.findByEmail(email);
//        if (existingUsername.isPresent() || existingEmail.isPresent()) {
//            throw new RepeatedUserInfoException(); // Throw exception if user already exists
//        }

//        Mono<SignUpResponse> response = webClient.post()
//            .uri("/auth/v1/signup")
//            .header("apikey",anonKey)
//            .bodyValue(new SignUpRequest(email, password))
//            .retrieve()
//            .bodyToMono(SignUpResponse.class)
//            .onErrorResume(error -> {
//                return Mono.error(new RuntimeException("Sign up failed: " + error.getMessage()));
//        });

        User user = userService.registerUser(userId, username, password, email, role);
        return new UserDTO(user);
    }

    @Transactional
    public Mono<String> login(String email, String password) {
        return webClient.post()
            .uri("/auth/v1/token?grant_type=password")
            .header("apikey",anonKey)
            .bodyValue(new SignInRequest(email, password))
            .retrieve()
            .bodyToMono(String.class)
            .onErrorResume(error -> {
                return Mono.error(new RuntimeException("Sign ip failed: " + error.getMessage()));
        });
    }

    public static class SignUpRequest {
        public String email;
        public String password;

        public SignUpRequest(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }

    public static class SignInRequest {
        private String email;
        private String password;

        public SignInRequest(String email, String password) {
            this.email = email;
            this.password = password;
        }

    }

    @Getter
    public static class SignUpResponse {
        private UUID userId;

        public SignUpResponse(UUID userId) {
            this.userId = userId;
        }
    }
}
