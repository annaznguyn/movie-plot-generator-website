package project.narrative;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import project.narrative.dto.UserDTO;
import project.narrative.error.UserInfoException;
import project.narrative.model.entities.User;
import project.narrative.repository.UserRepo;
import project.narrative.service.UserService;
import project.narrative.controller.UserController;
import project.narrative.controller.UserController.UserInfo;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.UUID;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;
    @Mock
    private UserRepo userRepo;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    // Test case for user login
    @Test
    void testLogin_Success() {
        UserInfo userInfo = new UserInfo();
        userInfo.setPassword("password");
        userInfo.setEmail("user@example.com");

        User mockUser = new User();
        when(userService.Login(userInfo.getPassword(), userInfo.getEmail())).thenReturn(mockUser);

        ResponseEntity<?> response = userController.putMethodName(userInfo);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof UserDTO);
    }

    // Test case for changing user role with admin privileges
    @Test
    void testChangeRole_AdminUser_Success() throws NoSuchAlgorithmException {
        UserInfo userInfo = new UserInfo();
        userInfo.setPassword("password");
        userInfo.setEmail("user@example.com");
        userInfo.setRole("premium");

        User operator = new User();
        operator.setRole("admin");

        User updatedUser = new User();
        when(userService.findUserById("adminUser")).thenReturn(Optional.of(operator));
        when(userService.changeRole("adminUser", userInfo.getPassword(), userInfo.getEmail(), userInfo.getRole())).thenReturn(updatedUser);

        ResponseEntity<?> response = userController.changeRole(userInfo, "adminUser");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof UserDTO);
    }

    // Test case for changing user role without admin privileges
    @Test
    void testChangeRole_NonAdminUser_Forbidden() throws NoSuchAlgorithmException {
        UserInfo userInfo = new UserInfo();
        userInfo.setPassword("password");
        userInfo.setEmail("user@example.com");
        userInfo.setRole("admin");

        User operator = new User();
        operator.setRole("user");

        when(userService.findUserById("regularUser")).thenReturn(Optional.of(operator));

        ResponseEntity<?> response = userController.changeRole(userInfo, "regularUser");

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Only admin can change other to limited roles", response.getBody());
    }

    // Test case for changing user role without admin privileges
    @Test
    void testChangeRole_WrongPassword() throws NoSuchAlgorithmException {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("john");
        userInfo.setPassword("password");
        userInfo.setEmail("user@example.com");
        userInfo.setRole("admin");

        User operator = new User();
        operator.setUsername("Iam_admin");
        operator.setRole("admin");

        User falsePerson = new User();
        falsePerson.setUsername("not_john");
        falsePerson.setRole("user");
        falsePerson.setHashed_pwd("wrong_password");

        when(userService.findUserById("admin")).thenReturn(Optional.of(operator));
        when(userRepo.findByEmail("user@example.com")).thenReturn(Optional.of(falsePerson));

        ResponseEntity<?> response = userController.changeRole(userInfo, "admin");

        // Assert the response status is FORBIDDEN
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Wrong e-mail address or password", response.getBody());
    }


    // Test case for invalid role change
    @Test
    void testChangeRole_UserNotFound() throws NoSuchAlgorithmException {
        UserInfo userInfo = new UserInfo();
        userInfo.setPassword("password");
        userInfo.setEmail("user@example.com");
        userInfo.setRole("USER");

        when(userService.findUserById("unknownUser")).thenReturn(Optional.empty());

        ResponseEntity<?> response = userController.changeRole(userInfo, "unknownUser");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Operator user not found", response.getBody());
    }

    // Test case for deleting user account
    @Test
    void testDeleteAccount_Success() {
        UserInfo userInfo = new UserInfo();
        userInfo.setPassword("password");
        userInfo.setEmail("user@example.com");

        when(userService.deleteAccount("user123", userInfo.getPassword(), userInfo.getEmail())).thenReturn("user123");

        ResponseEntity<?> response = userController.delete(userInfo, "user123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("user123", response.getBody());
    }
}
