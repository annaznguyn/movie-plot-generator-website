package project.narrative;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import project.narrative.controller.StoryController;
import project.narrative.dto.StoryDTO;
import project.narrative.error.UserInfoException;
import project.narrative.service.StoryService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class StoryControllerTest {

    @Mock
    private StoryService storyService;

    @InjectMocks
    private StoryController storyController;

    @Value("${API_KEY}")
    private String key;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        storyController = new StoryController(storyService, key);
    }

    @Test
    void testCreateStory_Success() {
        StoryController.StoryInfo storyInfo = new StoryController.StoryInfo();
        storyInfo.email = "user1@gmail.com";
        storyInfo.storyname = "Story Title";
        storyInfo.genre = "Fiction";
        storyInfo.description = "Story description";

        when(storyService.createStory(storyInfo.email, storyInfo.storyname, storyInfo.genre, storyInfo.description))
                .thenReturn(new StoryDTO());

        ResponseEntity<?> response = storyController.createStory(storyInfo);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(storyService, times(1)).createStory(storyInfo.email, storyInfo.storyname, storyInfo.genre,
                storyInfo.description);
    }

    @Test
    void testUpdateStory_Success() {
        Long storyId = 1L;
        StoryController.StoryInfo storyInfo = new StoryController.StoryInfo();
        storyInfo.storyname = "Updated Story";
        storyInfo.genre = "Drama";
        storyInfo.description = "Updated description";

        when(storyService.updateStory(storyId, storyInfo.storyname, storyInfo.genre, storyInfo.description))
                .thenReturn(new StoryDTO());

        ResponseEntity<?> response = storyController.updateStory(storyId, storyInfo);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(storyService, times(1)).updateStory(storyId, storyInfo.storyname, storyInfo.genre,
                storyInfo.description);
    }

    @Test
    void testGetStory_Success() {
        Long storyId = 1L;
        when(storyService.getStory(storyId)).thenReturn(new StoryDTO());

        ResponseEntity<?> response = storyController.getStory(storyId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(storyService, times(1)).getStory(storyId);
    }

    @Test
    void testGetStory_NotFound() {
        Long storyId = 1L;
        when(storyService.getStory(storyId)).thenReturn(null);

        ResponseEntity<?> response = storyController.getStory(storyId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    void testGetStoriesForUser_Success() {
        String userId = "user1";
        List<StoryDTO> storyList = new ArrayList<>();
        storyList.add(new StoryDTO());

        when(storyService.getStoriesWithNodeHierarchyForUser(userId)).thenReturn(storyList);

        ResponseEntity<List<StoryDTO>> response = storyController.getStoriesForUser(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(storyList, response.getBody());
    }

    @Test
    void testGetStoriesForUser_NotFound() {
        String userId = "user1";

        when(storyService.getStoriesWithNodeHierarchyForUser(userId)).thenReturn(Collections.emptyList());

        ResponseEntity<List<StoryDTO>> response = storyController.getStoriesForUser(userId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    void testGetStoriesForUser_UserInfoException() {
        String userId = "user1";
        when(storyService.getStoriesWithNodeHierarchyForUser(userId)).thenThrow(new UserInfoException());

        ResponseEntity<List<StoryDTO>> response = storyController.getStoriesForUser(userId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(Collections.emptyList(), response.getBody());
    }

    @Test
    void testGetStoriesForUser_InternalServerError() {
        String userId = "user1";
        when(storyService.getStoriesWithNodeHierarchyForUser(userId)).thenThrow(new RuntimeException());

        ResponseEntity<List<StoryDTO>> response = storyController.getStoriesForUser(userId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testGetStoriesWithNodeHierarchyForUser_Success() {
        String userId = "user1";
        List<StoryDTO> storyList = new ArrayList<>();
        storyList.add(new StoryDTO());

        when(storyService.getStoriesWithNodeHierarchyForUser(userId)).thenReturn(storyList);

        ResponseEntity<List<StoryDTO>> response = storyController.getStoriesWithNodeHierarchyForUser(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(storyList, response.getBody());
    }

    @Test
    void testGetStoriesWithNodeHierarchyForUser_NotFound() {
        String userId = "user1";

        when(storyService.getStoriesWithNodeHierarchyForUser(userId)).thenReturn(Collections.emptyList());

        ResponseEntity<List<StoryDTO>> response = storyController.getStoriesWithNodeHierarchyForUser(userId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    void testGetStoriesWithNodeHierarchyForUser_UserInfoException() {
        String userId = "user1";
        when(storyService.getStoriesWithNodeHierarchyForUser(userId)).thenThrow(new UserInfoException());

        ResponseEntity<List<StoryDTO>> response = storyController.getStoriesWithNodeHierarchyForUser(userId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(Collections.emptyList(), response.getBody());
    }

    @Test
    void testGetStoriesWithNodeHierarchyForUser_InternalServerError() {
        String userId = "user1";
        when(storyService.getStoriesWithNodeHierarchyForUser(userId)).thenThrow(new RuntimeException());

        ResponseEntity<List<StoryDTO>> response = storyController.getStoriesWithNodeHierarchyForUser(userId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
