package project.narrative.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import project.narrative.dto.UserDTO;
import project.narrative.model.entities.User;
import project.narrative.service.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * UserController handles user-related operations such as registration, login,
 * role changes, and account deletion. It provides endpoints for different user
 * actions.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    // Injecting UserService to handle business logic
    private final UserService userService;

    // Constructor to initialize UserService
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Endpoint for creating a new user.
     * 
     * @param userInfo contains the user's login credentials (username, password,
     *                 email, role).
     * @return whether the login is successful or not.
     */

//    @PostMapping
//    public ResponseEntity<?> createUser(@Valid @RequestBody UserInfo userInfo) throws NoSuchAlgorithmException {
//        return ResponseEntity.ok(userService.registerUser(userInfo.username, userInfo.password, userInfo.email, userInfo.role));
//    }


    /**
     * Endpoint for user login.
     * 
     * @param userInfo contains the user's login credentials (username, password,
     *                 email, role).
     * @return whether the login is successful or not.
     */
    @GetMapping
    public ResponseEntity<?> putMethodName(@Valid @RequestBody UserInfo userInfo) {
        return ResponseEntity.ok(new UserDTO(userService.Login(userInfo.password, userInfo.email)));
    }

    /**
     * Endpoint to change a user's role.
     * One can only be changed to premium by Admin
     * @param userInfo contains the user's username, password, email, and role.
     * @return the updated user with the premium role.
     */
    @PutMapping("/{userId}/roles")
    public ResponseEntity<?> changeRole(@Valid @RequestBody UserInfo userInfo, @PathVariable("userId") String userId)
            throws NoSuchAlgorithmException {
        // Check if the operator has admin privileges
        Optional<User> operator = userService.findUserById(userId);
        if (operator.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Operator user not found");
        }
        if (!"admin".equalsIgnoreCase(operator.get().getRole())) {
            if( "premium".equalsIgnoreCase(userInfo.getRole()) ||
                    "admin".equalsIgnoreCase(userInfo.getRole())){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only admin can change other to limited roles");
            }
        }

        User modified = userService.changeRole(userId, userInfo.password, userInfo.email, userInfo.role);
        if(modified == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Wrong e-mail address or password");
        }
        return ResponseEntity
                .ok(new UserDTO(modified));
    }


//    @PutMapping("/{userId}")
//    public ResponseEntity<?> update(@Valid @RequestBody UserInfo userInfo, @RequestParam("method") String method,
//            @PathVariable("userId") String userId)
//            throws NoSuchAlgorithmException {
//        if (method.equals("password")) {
//            return ResponseEntity.ok(userService.updatePassword(userId, userInfo.password,
//                    userInfo.newPassword, userInfo.email));
//        } else if (method.equals("nickname")) {
//            return ResponseEntity.ok(userService.updateNickName(userId, userInfo.password, userInfo.nickname,
//                    userInfo.email));
//        }
//        return ResponseEntity.badRequest().body("Invalid method");
//    }


    /**
     * Endpoint to delete a user account. Requires username, password, email, and
     * role.
     * 
     * @param userInfo contains the user's username, password, and email.
     * @return a confirmation of the deleted account.
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> delete(@Valid @RequestBody UserInfo userInfo, @PathVariable("userId") String userId) {
        return ResponseEntity.ok(userService.deleteAccount(userId, userInfo.password, userInfo.email));
    }

    /**
     * Inner class to hold the user information in requests.
     */
    @Data
    public static class UserInfo {
        public String nickname; // User's nickname
        public String username; // User's username
        public String password; // User's password
        public String newPassword; // User's new password
        @Email(message = "Email should be valid")
        public String email; // User's email address, with validation
        public String role;
    }
}
