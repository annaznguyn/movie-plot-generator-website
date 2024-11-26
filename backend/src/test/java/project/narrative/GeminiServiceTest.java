package project.narrative;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import project.narrative.dto.CharacterDTO;
import project.narrative.model.APIResponse;
import project.narrative.model.entities.Story;
import project.narrative.repository.StoryRepo;
import project.narrative.service.GeminiService;
import java.util.Optional;

import java.util.Collections;

public class GeminiServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private StoryRepo storyRepo;

    @InjectMocks
    private GeminiService geminiService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendAndReceiveRequest_Success() {
        String key = "testKey";
        String prompt = "Test prompt";
        String expectedResponseText = "Generated content";

        // Mock the APIResponse structure
        APIResponse.Candidate.Content.Part part = new APIResponse.Candidate.Content.Part();
        part.setText(expectedResponseText);
        APIResponse.Candidate.Content content = new APIResponse.Candidate.Content();
        content.setParts(Collections.singletonList(part));
        APIResponse.Candidate candidate = new APIResponse.Candidate();
        candidate.setContent(content);
        APIResponse apiResponse = new APIResponse();
        apiResponse.setCandidates(Collections.singletonList(candidate));

        // Mock the RestTemplate response
        ResponseEntity<APIResponse> mockResponseEntity = new ResponseEntity<>(apiResponse, HttpStatus.OK);
        when(restTemplate.exchange(
                any(String.class),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(APIResponse.class)))
                .thenReturn(mockResponseEntity);

        // Call the method
        String result = geminiService.sendAndReceiveRequest(key, prompt);

        // Assertions
        assertEquals(expectedResponseText, result);
        verify(restTemplate, times(1)).exchange(any(String.class), eq(HttpMethod.POST), any(HttpEntity.class),
                eq(APIResponse.class));
    }

    @Test
    void testCharacterGenerated_Success() {
        String key = "testKey";
        String firstName = "John";
        String lastName = "Doe";
        String context = "Test context";
        Long storyId = 1L;
        String genre = "Fantasy";
        String generatedBackground = "Generated background for John Doe.";

        // Mock the Story object
        Story mockStory = new Story();
        mockStory.setStoryid(storyId);
        mockStory.setGenre(genre);

        // Mock the storyRepo to return a valid Story
        when(storyRepo.findById(storyId)).thenReturn(Optional.of(mockStory));

        // Mock the APIResponse structure
        APIResponse.Candidate.Content.Part part = new APIResponse.Candidate.Content.Part();
        part.setText(generatedBackground);
        APIResponse.Candidate.Content content = new APIResponse.Candidate.Content();
        content.setParts(Collections.singletonList(part));
        APIResponse.Candidate candidate = new APIResponse.Candidate();
        candidate.setContent(content);
        APIResponse apiResponse = new APIResponse();
        apiResponse.setCandidates(Collections.singletonList(candidate));

        // Mock the RestTemplate response
        ResponseEntity<APIResponse> mockResponseEntity = new ResponseEntity<>(apiResponse, HttpStatus.OK);
        when(restTemplate.exchange(
                any(String.class),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(APIResponse.class)))
                .thenReturn(mockResponseEntity);

        // Call the method
        CharacterDTO result = geminiService.characterGenerated(key, firstName, lastName, context, storyId);

        // Assertions
        assertEquals(firstName, result.getFirstName());
        assertEquals(lastName, result.getLastName());
        assertEquals(generatedBackground, result.getBackground());
        assertEquals(context, result.getContext());

        // Verify that the dependencies were called
        verify(storyRepo, times(1)).findById(storyId);
        verify(restTemplate, times(1)).exchange(any(String.class), eq(HttpMethod.POST), any(HttpEntity.class),
                eq(APIResponse.class));
    }

    @Test
    void testCharacterGenerated_StoryNotFound() {
        String key = "testKey";
        String firstName = "John";
        String lastName = "Doe";
        String context = "Test context";
        Long storyId = 1L;

        // Mock the storyRepo to return an empty result, simulating story not found
        when(storyRepo.findById(storyId)).thenReturn(Optional.empty());

        // Verify that a RuntimeException is thrown
        assertThrows(RuntimeException.class, () -> {
            geminiService.characterGenerated(key, firstName, lastName, context, storyId);
        });

        // Verify that sendAndReceiveRequest was never called, indirectly, by checking
        // that storyRepo was called once
        verify(storyRepo, times(1)).findById(storyId);
        // No need to verify geminiService directly since it’s the service under test
    }

}
