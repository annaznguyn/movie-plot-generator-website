package project.narrative;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import project.narrative.controller.DashboardController;
import project.narrative.controller.DashboardController.*;
import project.narrative.service.DashboardService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class DashboardControllerTest {

    @Mock
    private DashboardService dashboardService;

    @InjectMocks
    private DashboardController dashboardController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test getAllStories with valid username
    @Test
    void testGetAllStories_ValidUsername() {
        AllStoriesParam param = new AllStoriesParam();
        param.username = "user123";

        when(dashboardService.getStoriesByUserid("user123")).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = dashboardController.getAllStories(param);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.emptyList(), response.getBody());
    }


    // Test getAllUsers returns list of users
    @Test
    void testGetAllUsers() {
        when(dashboardService.getAllUsers()).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = dashboardController.getAllUsers();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.emptyList(), response.getBody());
    }

    // Test getStoriesByUsernameAndCreationDate with valid data
    @Test
    void testGetStoriesByUsernameAndCreationDate_ValidData() {
        CreationDateFilterParam param = new CreationDateFilterParam();
        param.username = "user123";
        param.creationDate = LocalDate.now();

        when(dashboardService.getStoriesByUsernameAndCreationDate(param.username, param.creationDate))
                .thenReturn(Collections.emptyList());

        ResponseEntity<?> response = dashboardController.getStoriesByUsernameAndCreationDate(param);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.emptyList(), response.getBody());
    }

    // Test getStoriesByUsernameAndLastUpdate with valid data
    @Test
    void testGetStoriesByUsernameAndLastUpdate_ValidData() {
        LastUpdateFilterParam param = new LastUpdateFilterParam();
        param.username = "user123";
        param.lastUpdate = LocalDate.now();

        when(dashboardService.getStoriesByUsernameAndLastUpdate(param.username, param.lastUpdate))
                .thenReturn(Collections.emptyList());

        ResponseEntity<?> response = dashboardController.getStoriesByUsernameAndLastUpdate(param);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.emptyList(), response.getBody());
    }


    // Test getStoriesByUsernameAndStoryname with valid data
    @Test
    void testGetStoriesByUsernameAndStoryname_ValidData() {
        StorynameFilterParam param = new StorynameFilterParam();
        param.username = "user123";
        param.storyname = "Test Story";

        when(dashboardService.getStoriesByUsernameAndStoryname(param.username, param.storyname))
                .thenReturn(Collections.emptyList());

        ResponseEntity<?> response = dashboardController.getStoriesByUsernameAndStoryname(param);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.emptyList(), response.getBody());
    }

    // Test getStoriesByUsernameAndGenre with valid data
    @Test
    void testGetStoriesByUsernameAndGenre_ValidData() {
        GenreFilterParam param = new GenreFilterParam();
        param.username = "user123";
        param.genre = "Fantasy";

        when(dashboardService.getStoriesByUsernameAndGenre(param.username, param.genre))
                .thenReturn(Collections.emptyList());

        ResponseEntity<?> response = dashboardController.getStoriesByUsernameAndGenre(param);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.emptyList(), response.getBody());
    }

}
