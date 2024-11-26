package project.narrative.service;

import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import project.narrative.error.UserInfoException;
import project.narrative.model.entities.User;
import project.narrative.repository.UserRepo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    // Repository for interacting with the User entity in the database
    private final UserRepo userRepo;

    // Constructor to inject the UserRepo dependency
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    // Method to register a new user, includes password hashing with salt
    @Transactional
    public User registerUser(UUID id, String username, String password, String email, String role)
            throws NoSuchAlgorithmException {

        // Generate salt and hash the password with the generated salt
        String salt = generateSalt();
        System.out.println(password);
        String hashed_pwd = hashPasswordWithSalt(password, salt);

        // Create a new user and save it to the database
        User user = new User(id, username, Date.valueOf(LocalDate.now()), email, role, hashed_pwd, salt);
        userRepo.save(user);
        return user;
    }

    // Method to change a user's role, checks password validity first
    @Transactional
    public User changeRole(String username, String password, String email, String role)
            throws NoSuchAlgorithmException {
        // Verify user credentials
        if (!dataCheck(password, email)) {
            throw new UserInfoException(); // Throw exception if credentials are invalid
        }

        // Fetch the user and update their role
        User user = userRepo.findById(username).orElseThrow();
        user.setUsername(username);
        user.setRole(role);
        userRepo.save(user); // Save the updated user
        return user;
    }

    // Method to delete a user's account, checks password validity first
    @Transactional
    public String deleteAccount(String username, String password, String email) {
        // Verify user credentials
        if (!dataCheck(password, email)) {
            throw new UserInfoException(); // Throw exception if credentials are invalid
        }

        // Delete user from the database
        User user = userRepo.findById(username).orElseThrow();
        userRepo.deleteById(username);
        return username; // Return the user if found
    }

    // Method to update a user's password
    @Transactional
    public User updatePassword(String username, String password, String newPassword, String email)
            throws NoSuchAlgorithmException {
        User user = userRepo.findByEmail(email).orElseThrow(() -> new UserInfoException());
        // Verify user credentials before updating password
        if (!dataCheck(password, email)) {
            throw new UserInfoException();
        }

        // Generate salt and hash the password with the generated salt
        String salt = generateSalt();
        String hashed_pwd = hashPasswordWithSalt(newPassword, salt);

        user.setHashed_pwd(hashed_pwd); // Update password
        user.setSalt(salt); // Update salt
        userRepo.save(user); // Save the updated user
        return user;
    }

//    @Transactional
//    public User updateNickName(String username, String password, String nickName, String email) {
//        User user = userRepo.findByEmail(email).orElseThrow(() -> new UserInfoException());
//        // Verify user credentials before updating password
//        if (!dataCheck(password, email)) {
//            throw new UserInfoException();
//        }
//
//        user.setNickname(nickName); // Update password
//        userRepo.save(user); // Save the updated user
//        return user;
//    }

    public User Login(String password, String email) {
        User searchedUser = userRepo.findByEmail(email).orElseThrow(() -> new UserInfoException());
        try {
            // Hash the provided password with the stored salt and compare
            String hashed_pwd = hashPasswordWithSalt(password, searchedUser.getSalt());
            if (!searchedUser.getHashed_pwd().equals(hashed_pwd)) {
                System.out.println(searchedUser.getHashed_pwd());
                System.out.println(hashed_pwd);
                System.out.println(password);
                System.out.println(searchedUser.getSalt());
                throw new RuntimeException("Unmatched passwords");
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return searchedUser; // Return false if password does not match
    }

    // Method to check if provided password matches the stored hashed password, And
    // if the user exists on database
    public Boolean dataCheck(String password, String email) {
        User searchedUser = userRepo.findByEmail(email).orElseThrow(() -> new UserInfoException());
        try {
            // Hash the provided password with the stored salt and compare
            String hashed_pwd = hashPasswordWithSalt(password, searchedUser.getSalt());
            if (searchedUser.getHashed_pwd().equals(hashed_pwd)) {
                return true; // Return true if password matches
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return false; // Return false if password does not match
    }

    // Utility method to hash a password with a given salt
    public String hashPasswordWithSalt(String password, String salt) throws NoSuchAlgorithmException {
        String saltedPassword = password + salt; // Combine password and salt

        // Create a MessageDigest instance for SHA-256 hashing
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        // Hash the salted password
        byte[] hashedPassword = md.digest(saltedPassword.getBytes());

        // Return the hashed password as a base64 encoded string
        return Base64.getEncoder().encodeToString(hashedPassword);
    }

    // Utility method to generate a random salt for password hashing
    public String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt); // Generate random bytes
        return Base64.getEncoder().encodeToString(salt); // Return salt as base64 string
    }

    public Optional<User> findUserById(String username) {
        return userRepo.findById(username);
    }
}
