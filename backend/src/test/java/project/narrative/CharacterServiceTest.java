package project.narrative;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import project.narrative.dto.CharacterDTO;
import project.narrative.model.entities.Character;
import project.narrative.model.entities.Story;
import project.narrative.repository.CharacterRepo;
import project.narrative.repository.StoryRepo;
import project.narrative.service.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CharacterServiceTest {

    @Mock
    private CharacterRepo characterRepo;

    @Mock
    private StoryRepo storyRepo;

    @Mock
    private GeminiService geminiService;

    @InjectMocks
    private CharacterService characterService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Write tests for each method in CharacterService
    @Test
    void testCreate_Success() {
        Long storyId = 1L;
        Story story = new Story();
        story.setStoryid(storyId);

        when(storyRepo.findById(storyId)).thenReturn(Optional.of(story));
        when(characterRepo.save(any(Character.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CharacterDTO result = characterService.create(storyId);

        assertNotNull(result);
        assertEquals(storyId, result.getStory_id());
        verify(characterRepo, times(1)).save(any(Character.class));
    }

    @Test
    void testCreate_StoryNotFound() {
        Long storyId = 1L;

        when(storyRepo.findById(storyId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> characterService.create(storyId));
        verify(characterRepo, never()).save(any(Character.class));
    }

    @Test
    void testSave_Success() {
        Long storyId = 1L;
        Long characterId = 1L;
        String fname = "John";
        String lname = "Doe";
        String background = "Background";
        String context = "Context";

        Story story = new Story();
        story.setStoryid(storyId);

        Character character = new Character();
        character.setId(characterId);

        when(storyRepo.findById(storyId)).thenReturn(Optional.of(story));
        when(characterRepo.findByFirstNameAndStory(fname, story)).thenReturn(Optional.empty());
        when(characterRepo.findById(characterId)).thenReturn(Optional.of(character));
        when(characterRepo.save(any(Character.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CharacterDTO result = characterService.save(fname, lname, background, context, storyId, characterId);

        assertNotNull(result);
        assertEquals(fname, result.getFirstName());
        assertEquals(lname, result.getLastName());
        assertEquals(background, result.getBackground());
        verify(characterRepo, times(1)).save(any(Character.class));
    }

    @Test
    void testSave_StoryNotFound() {
        Long storyId = 1L;

        when(storyRepo.findById(storyId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> characterService.save("John", "Doe", "Background", "Context", storyId, 1L));
        verify(characterRepo, never()).save(any(Character.class));
    }

    @Test
    void testSave_CharacterNotFound() {
        Long storyId = 1L;
        Long characterId = 1L;

        Story story = new Story();
        story.setStoryid(storyId);

        when(storyRepo.findById(storyId)).thenReturn(Optional.of(story));
        when(characterRepo.findById(characterId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> characterService.save("John", "Doe", "Background", "Context", storyId, characterId));
        verify(characterRepo, never()).save(any(Character.class));
    }

    @Test
    void testSave_DuplicateCharacter() {
        Long storyId = 1L;
        Long characterId = 1L;

        Story story = new Story();
        story.setStoryid(storyId);

        Character duplicateCharacter = new Character();
        duplicateCharacter.setId(2L); // Different ID to simulate duplicate name

        when(storyRepo.findById(storyId)).thenReturn(Optional.of(story));
        when(characterRepo.findByFirstNameAndStory("John", story)).thenReturn(Optional.of(duplicateCharacter));

        assertThrows(RuntimeException.class,
                () -> characterService.save("John", "Doe", "Background", "Context", storyId, characterId));
        verify(characterRepo, never()).save(any(Character.class));
    }

    @Test
    void testGet_Success() {
        Long characterId = 1L;
        Long storyId = 1L;

        // Create and set up Story object
        Story story = new Story();
        story.setStoryid(storyId);

        // Create and set up Character object with associated Story
        Character character = new Character();
        character.setId(characterId);
        character.setStory(story); // Associate the Story with the Character

        // Mock repository response
        when(characterRepo.findById(characterId)).thenReturn(Optional.of(character));

        // Call the service method
        CharacterDTO result = characterService.get(characterId);

        // Assertions
        assertNotNull(result);
        assertEquals(characterId, result.getCharacter_id());
        assertEquals(storyId, result.getStory_id()); // Verify Story ID is correctly set in DTO
    }

    @Test
    void testGet_CharacterNotFound() {
        Long characterId = 1L;

        when(characterRepo.findById(characterId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> characterService.get(characterId));
    }

    @Test
    void testDelete_Success() {
        Long characterId = 1L;

        characterService.delete(characterId);

        verify(characterRepo, times(1)).deleteById(characterId);
    }

    @Test
    void testGetAll_Success() {
        Long storyId = 1L;

        // Create and set up Story object
        Story story = new Story();
        story.setStoryid(storyId);

        // Create and set up Character objects with associated Story
        Character character1 = new Character();
        character1.setId(1L);
        character1.setStory(story); // Associate Story with Character

        Character character2 = new Character();
        character2.setId(2L);
        character2.setStory(story); // Associate Story with Character

        List<Character> characters = List.of(character1, character2);

        // Mock repository responses
        when(storyRepo.findById(storyId)).thenReturn(Optional.of(story));
        when(characterRepo.findByStory(story)).thenReturn(characters);

        // Call the service method
        List<CharacterDTO> result = characterService.getAll(storyId);

        // Assertions
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getCharacter_id());
        assertEquals(2L, result.get(1).getCharacter_id());
        verify(characterRepo, times(1)).findByStory(story);
    }

    @Test
    void testGetAll_StoryNotFound() {
        Long storyId = 1L;

        when(storyRepo.findById(storyId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> characterService.getAll(storyId));
        verify(characterRepo, never()).findByStory(any());
    }

}
