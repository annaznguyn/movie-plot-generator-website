package project.narrative;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import project.narrative.dto.StoryDTO;
import project.narrative.dto.UserDTO;
import project.narrative.error.UserInfoException;
import project.narrative.model.entities.Story;
import project.narrative.model.entities.User;
import project.narrative.repository.StoryRepo;
import project.narrative.repository.UserRepo;
import project.narrative.service.DashboardService;

import java.time.LocalDate;
import java.util.Date;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DashboardServiceTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private StoryRepo storyRepo;

    @InjectMocks
    private DashboardService dashboardService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() {
        UUID userId = UUID.randomUUID();  // Generate a UUID
        UUID userId2 = UUID.randomUUID();  // Generate a UUID
        User user1 = new User(userId, "user1", new Date(System.currentTimeMillis()), "email1@example.com", "ROLE_USER", "pwd1",
                "salt1");
        User user2 = new User(userId2, "user2", new Date(System.currentTimeMillis()), "email2@example.com", "ROLE_USER", "pwd2",
                "salt2");

        when(userRepo.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<UserDTO> users = dashboardService.getAllUsers();

        assertEquals(2, users.size());
        assertEquals("user1", users.get(0).getUsername());
        assertEquals("user2", users.get(1).getUsername());
    }

    @Test
    void testGetStoriesByUserid_UserExists() {
        UUID userId = UUID.randomUUID();  // Generate a UUID
        String username = "user1";
        User user = new User(userId, username, new Date(System.currentTimeMillis()), "email@example.com", "ROLE_USER", "pwd",
                "salt");

        Story story1 = new Story("Story 1", user, "Genre 1", LocalDate.now(), LocalDate.now());
        Story story2 = new Story("Story 2", user, "Genre 2", LocalDate.now(), LocalDate.now());

        when(userRepo.findById(username)).thenReturn(Optional.of(user));
        when(storyRepo.findByUserUsername(username)).thenReturn(Optional.of(Arrays.asList(story1, story2)));

        List<StoryDTO> stories = dashboardService.getStoriesByUserid(username);

        assertEquals(2, stories.size());
        assertEquals("Story 1", stories.get(0).getStoryName());
        assertEquals("Story 2", stories.get(1).getStoryName());
    }


    @Test
    void testGetStoriesByUserid_UserNotFound() {
        String username = "nonExistentUser";

        when(userRepo.findById(username)).thenReturn(Optional.empty());

        assertThrows(UserInfoException.class, () -> dashboardService.getStoriesByUserid(username));
    }

    @Test
    void testGetStoriesByUsernameAndCreationDate() {
        UUID userId = UUID.randomUUID();  // Generate a UUID
        String username = "user1";
        LocalDate creationDate = LocalDate.now();

        Story story = new Story("Story 1", new User(userId, username, new Date(System.currentTimeMillis()), "email@example.com",
                "ROLE_USER", "pwd", "salt"), "Genre", creationDate, LocalDate.now());

        when(storyRepo.findByUserUsernameAndCreationDate(username, creationDate))
                .thenReturn(Optional.of(Collections.singletonList(story)));

        List<StoryDTO> stories = dashboardService.getStoriesByUsernameAndCreationDate(username, creationDate);

        assertEquals(1, stories.size());
        assertEquals("Story 1", stories.get(0).getStoryName());
    }

    @Test
    void testGetStoriesByUsernameAndLastUpdate() {
        UUID userId = UUID.randomUUID();  // Generate a UUID
        String username = "user1";
        LocalDate lastUpdate = LocalDate.now();

        Story story = new Story("Story 1", new User(userId, username, new Date(System.currentTimeMillis()), "email@example.com",
                "ROLE_USER", "pwd", "salt"), "Genre", LocalDate.now(), lastUpdate);

        when(storyRepo.findByUserUsernameAndLastUpdate(username, lastUpdate))
                .thenReturn(Optional.of(Collections.singletonList(story)));

        List<StoryDTO> stories = dashboardService.getStoriesByUsernameAndLastUpdate(username, lastUpdate);

        assertEquals(1, stories.size());
        assertEquals("Story 1", stories.get(0).getStoryName());
    }

    @Test
    void testGetStoriesByUsernameAndStoryname() {
        UUID userId = UUID.randomUUID();  // Generate a UUID
        String username = "user1";
        String storyname = "Unique Story";

        Story story = new Story(storyname, new User(userId, username, new Date(System.currentTimeMillis()), "email@example.com",
                "ROLE_USER", "pwd", "salt"), "Genre", LocalDate.now(), LocalDate.now());

        when(storyRepo.findByUserUsernameAndStoryname(username, storyname))
                .thenReturn(Optional.of(Collections.singletonList(story)));

        List<StoryDTO> stories = dashboardService.getStoriesByUsernameAndStoryname(username, storyname);

        assertEquals(1, stories.size());
        assertEquals(storyname, stories.get(0).getStoryName());
    }

    @Test
    void testGetStoriesByUsernameAndGenre() {
        UUID userId = UUID.randomUUID();  // Generate a UUID
        String username = "user1";
        String genre = "Fantasy";

        Story story = new Story("Story 1", new User(userId, username, new Date(System.currentTimeMillis()), "email@example.com",
                "ROLE_USER", "pwd", "salt"), genre, LocalDate.now(), LocalDate.now());

        when(storyRepo.findByUserUsernameAndGenre(username, genre))
                .thenReturn(Optional.of(Collections.singletonList(story)));

        List<StoryDTO> stories = dashboardService.getStoriesByUsernameAndGenre(username, genre);

        assertEquals(1, stories.size());
        assertEquals("Story 1", stories.get(0).getStoryName());
        assertEquals(genre, stories.get(0).getGenre());
    }
}
