package project.narrative;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import project.narrative.controller.CharacterController;
import project.narrative.controller.CharacterController.CharacterInfo;
import project.narrative.dto.CharacterDTO;
import project.narrative.model.entities.Character;
import project.narrative.service.CharacterService;
import project.narrative.service.GeminiService;

import java.util.Collections;
import java.util.List;

public class CharacterControllerTest {

    @Mock
    private CharacterService characterService;

    @Mock
    private GeminiService geminiService;

    @InjectMocks
    private CharacterController characterController;

    @Value("${API_KEY}")
    private String apiKey;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        characterController = new CharacterController(characterService, geminiService, apiKey);
    }

    // Test case for getting a single character by ID
    @Test
    void testGetCharacter_Success() {
        Long characterId = 1L;
        CharacterDTO characterDTO = new CharacterDTO();
        characterDTO.setCharacter_id(characterId);

        when(characterService.get(characterId)).thenReturn(characterDTO);

        ResponseEntity<?> response = characterController.get(characterId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(characterDTO, response.getBody());
    }

    // Test case for getting all characters of a story
    @Test
    void testGetAllCharacters_Success() {
        Long storyId = 1L;
        List<CharacterDTO> characterDTOs = Collections.singletonList(new CharacterDTO());

        when(characterService.getAll(storyId)).thenReturn(characterDTOs);

        ResponseEntity<?> response = characterController.getAll(storyId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(characterDTOs, response.getBody());
    }

    // Test case for creating a new character
    @Test
    void testCreateCharacter_Success() {
        Long storyId = 1L;
        Long newCharacterId = 10L;
        CharacterDTO characterDTO = new CharacterDTO();
        characterDTO.setCharacter_id(newCharacterId);

        when(characterService.create(storyId)).thenReturn(characterDTO);

        ResponseEntity<?> response = characterController.create(storyId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(newCharacterId, response.getBody());
    }

    // Test case for updating a character
    @Test
    void testUpdateCharacter_Success() {
        Long storyId = 1L;
        Long characterId = 10L;
        CharacterInfo characterInfo = new CharacterInfo();
        characterInfo.setFirstName("John");
        characterInfo.setLastName("Doe");
        characterInfo.setBackground("Background");
        characterInfo.setContext("Context");

        CharacterDTO updatedCharacterDTO = new CharacterDTO();
        updatedCharacterDTO.setCharacter_id(characterId);

        when(characterService.save(characterInfo.getLastName(), characterInfo.getFirstName(),
                characterInfo.getBackground(), characterInfo.getContext(), storyId, characterId)).thenReturn(updatedCharacterDTO);

        ResponseEntity<?> response = characterController.save(characterInfo, storyId, characterId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedCharacterDTO, response.getBody());
    }

    // Test case for deleting a character
    @Test
    void testDeleteCharacter_Success() {
        Long characterId = 1L;

        doNothing().when(characterService).delete(characterId);

        ResponseEntity<?> response = characterController.delete(characterId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(characterId, response.getBody());
        verify(characterService, times(1)).delete(characterId);
    }

    // Test case for generating a character with AI
    @Test
    void testCharacterGeneration_Success() {
        Long storyId = 1L;
        CharacterInfo characterInfo = new CharacterInfo();
        characterInfo.setFirstName("John");
        characterInfo.setLastName("Doe");
        characterInfo.setContext("Fantasy world");

        CharacterDTO generatedCharacterDTO = new CharacterDTO();
        generatedCharacterDTO.setFirstName("John");
        generatedCharacterDTO.setLastName("Doe");

        when(geminiService.characterGenerated(apiKey, characterInfo.getFirstName(), characterInfo.getLastName(),
                characterInfo.getContext(), storyId)).thenReturn(generatedCharacterDTO);

        ResponseEntity<?> response = characterController.getMethodName(characterInfo, storyId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(generatedCharacterDTO, response.getBody());
    }


    // Edge case for character creation with invalid story ID
    @Test
    void testCreateCharacter_InvalidStoryId() {
        Long invalidStoryId = -1L;
        when(characterService.create(invalidStoryId)).thenThrow(new IllegalArgumentException("Invalid story ID"));

        assertThrows(IllegalArgumentException.class, () -> characterController.create(invalidStoryId));
    }
}
