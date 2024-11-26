package project.narrative;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import project.narrative.error.UserInfoException;
import project.narrative.model.entities.User;
import project.narrative.repository.UserRepo;
import project.narrative.service.UserService;

import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_Success() throws NoSuchAlgorithmException {
        UUID userId = UUID.randomUUID(); // Generate a UUID
        String username = "newUser";
        String password = "password123";
        String email = "test@example.com";
        String role = "USER";

        when(userRepo.findById(username)).thenReturn(Optional.empty());
        when(userRepo.findByEmail(email)).thenReturn(Optional.empty());

        User user = userService.registerUser(userId, username, password, email, role);

        assertNotNull(user);
        assertEquals(username, user.getUsername());
        assertEquals(email, user.getEmail());
        assertEquals(role, user.getRole());
        verify(userRepo, times(1)).save(any(User.class));
    }

    @Test
    void testChangeRole_Success() throws NoSuchAlgorithmException {
        UUID userId = UUID.randomUUID(); // Generate a UUID
        String username = "user123";
        String password = "password123";
        String email = "user@example.com";
        String role = "ADMIN";

        User user = new User(userId, username, Date.valueOf(LocalDate.now()), email, "USER", "hashed_pwd", "salt");

        when(userRepo.findById(username)).thenReturn(Optional.of(user));
        when(userRepo.findByEmail(email)).thenReturn(Optional.of(user));

        // Instead of mocking dataCheck, allow it to run as it is in the test
        user.setHashed_pwd(userService.hashPasswordWithSalt(password, user.getSalt())); // Ensure dataCheck passes

        User updatedUser = userService.changeRole(username, password, email, role);

        assertEquals(role, updatedUser.getRole());
        verify(userRepo, times(1)).save(updatedUser);
    }

    @Test
    void testDeleteAccount_Success() throws NoSuchAlgorithmException {
        UUID userId = UUID.randomUUID(); // Generate a UUID
        String username = "user123";
        String password = "password123";
        String email = "user@example.com";

        User user = new User(userId, username, Date.valueOf(LocalDate.now()), email, "USER", "hashed_pwd", "salt");

        when(userRepo.findById(username)).thenReturn(Optional.of(user));
        when(userRepo.findByEmail(email)).thenReturn(Optional.of(user));

        // Set the hashed password for dataCheck to pass naturally
        user.setHashed_pwd(userService.hashPasswordWithSalt(password, user.getSalt()));

        String deletedUsername = userService.deleteAccount(username, password, email);

        assertEquals(username, deletedUsername);
        verify(userRepo, times(1)).deleteById(username);
    }

    @Test
    void testUpdatePassword_Success() throws NoSuchAlgorithmException {
        UUID userId = UUID.randomUUID(); // Generate a UUID
        String username = "user123";
        String password = "oldPassword";
        String newPassword = "newPassword123";
        String email = "user@example.com";

        // Create a user with known salt and hashed password for the initial password
        User user = new User(userId, username, Date.valueOf(LocalDate.now()), email, "USER", "old_hashed_pwd",
                "old_salt");

        // Mock repository to return the user by email
        when(userRepo.findByEmail(email)).thenReturn(Optional.of(user));

        // Manually set the correct hashed password for the old password
        String correctHashedOldPassword = userService.hashPasswordWithSalt(password, user.getSalt());
        user.setHashed_pwd(correctHashedOldPassword);

        // Call the method
        User updatedUser = userService.updatePassword(username, password, newPassword, email);

        // Assertions to verify password was updated
        assertNotNull(updatedUser.getHashed_pwd());
        assertNotEquals("old_hashed_pwd", updatedUser.getHashed_pwd());
        assertNotEquals(correctHashedOldPassword, updatedUser.getHashed_pwd());
        verify(userRepo, times(1)).save(updatedUser);
    }

    @Test
    void testDataCheck_PasswordMatches() throws NoSuchAlgorithmException {
        UUID userId = UUID.randomUUID(); // Generate a UUID
        String password = "password123";
        String email = "user@example.com";

        // Create a User object and set the salt and expected hashed password
        User user = new User(userId, "username", Date.valueOf(LocalDate.now()), email, "USER", null, "salt");

        // Calculate the expected hashed password
        String expectedHashedPassword = userService.hashPasswordWithSalt(password, user.getSalt());
        user.setHashed_pwd(expectedHashedPassword);

        // Mock the repository to return the user object
        when(userRepo.findByEmail(email)).thenReturn(Optional.of(user));

        // Call the method
        boolean result = userService.dataCheck(password, email);

        // Verify the result
        assertTrue(result);
    }

    @Test
    void testLogin_Success() throws NoSuchAlgorithmException {
        UUID userId = UUID.randomUUID(); // Generate a UUID
        String password = "password123";
        String email = "user@example.com";

        User user = new User(userId, "username", Date.valueOf(LocalDate.now()), email, "USER", "hashed_pwd", "salt");

        UserService userServiceSpy = spy(userService);

        when(userRepo.findByEmail(email)).thenReturn(Optional.of(user));
        doReturn("hashed_pwd").when(userServiceSpy).hashPasswordWithSalt(password, user.getSalt()); // Mock hash matches

        User loggedInUser = userServiceSpy.Login(password, email);

        assertNotNull(loggedInUser);
        assertEquals("username", loggedInUser.getUsername());
    }

    @Test
    void testLogin_InvalidPassword() throws NoSuchAlgorithmException {
        UUID userId = UUID.randomUUID(); // Generate a UUID
        String password = "wrongPassword";
        String email = "user@example.com";

        User user = new User(userId, "username", Date.valueOf(LocalDate.now()), email, "USER", "hashed_pwd", "salt");

        UserService userServiceSpy = spy(userService);

        when(userRepo.findByEmail(email)).thenReturn(Optional.of(user));
        doReturn("wrong_hashed_pwd").when(userServiceSpy).hashPasswordWithSalt(password, user.getSalt()); // Mock hash
                                                                                                          // mismatch

        assertThrows(RuntimeException.class, () -> {
            userServiceSpy.Login(password, email);
        });
    }

    @Test
    void testDataCheck_PasswordDoesNotMatch() throws NoSuchAlgorithmException {
        UUID userId = UUID.randomUUID(); // Generate a UUID
        String password = "wrongPassword";
        String email = "user@example.com";

        User user = new User(userId, "username", Date.valueOf(LocalDate.now()), email, "USER", "hashed_pwd", "salt");

        UserService userServiceSpy = spy(userService);

        when(userRepo.findByEmail(email)).thenReturn(Optional.of(user));
        doReturn("wrong_hashed_pwd").when(userServiceSpy).hashPasswordWithSalt(password, user.getSalt()); // Mock hash
                                                                                                          // mismatch

        boolean result = userServiceSpy.dataCheck(password, email);

        assertFalse(result);
    }
}
