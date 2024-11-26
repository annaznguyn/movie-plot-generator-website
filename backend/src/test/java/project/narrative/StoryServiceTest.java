package project.narrative;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import project.narrative.dto.StoryDTO;
import project.narrative.error.UnfoundStoryException;
import project.narrative.error.UserInfoException;
import project.narrative.model.entities.Story;
import project.narrative.model.entities.User;
import project.narrative.repository.*;
import project.narrative.service.StoryService;
import project.narrative.model.entities.Character;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StoryServiceTest {

    @Mock
    private StoryRepo storyRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private CharacterRepo characterRepo;

    @InjectMocks
    private StoryService storyService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetStoriesByTitleAndUsername() {
        // Create mock User
        User user = new User();
        user.setUsername("john_doe");

        // Create mock Story
        Story story = new Story();
        story.setStoryname("Magic Adventure");
        story.setUser(user); // Set the user relationship

        // Mock the repository call
        when(storyRepo.findByStorynameContainingAndUserUsername("Magic Adventure", "john_doe"))
                .thenReturn(Optional.of(Arrays.asList(story)));

        // Call the service method
        List<Story> result = storyService.getStoriesByNameContainingAndUsername("Magic Adventure", "john_doe");

        // Verify the results
        assertEquals(1, result.size());
        assertEquals("Magic Adventure", result.get(0).getStoryname());
        assertEquals("john_doe", result.get(0).getUser().getUsername());
    }

    @Test
    public void testGetStoriesByTitleAndUsername_EmptyList() {
        // Mock the repository call for no matches
        when(storyRepo.findByStorynameContainingAndUserUsername("Nonexistent Title", "john_doe"))
                .thenReturn(Optional.empty());

        // Call the service method
        List<Story> result = storyService.getStoriesByNameContainingAndUsername("Nonexistent Title", "john_doe");

        // Verify the results
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetStoriesByCreationDateRange_Found() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);

        Story story = new Story();
        story.setStoryname("Story in Range");
        story.setCreationDate(LocalDate.of(2023, 6, 15));

        when(storyRepo.findByCreationDateBetween(startDate, endDate))
                .thenReturn(Optional.of(Arrays.asList(story)));

        List<Story> result = storyService.getStoriesByCreationDateRange(Optional.of(startDate), Optional.of(endDate));

        assertEquals(1, result.size());
        assertEquals("Story in Range", result.get(0).getStoryname());
    }

    @Test
    public void testGetStoriesByCreationDateRange_NoEnddate() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);

        Story story = new Story();
        story.setStoryname("Story in Range");
        story.setCreationDate(LocalDate.of(2023, 6, 15));

        when(storyRepo.findByCreationDateAfter(startDate))
                .thenReturn(Optional.of(Arrays.asList(story)));
        List<Story> result = storyService.getStoriesByCreationDateRange(Optional.of(startDate), Optional.empty());

        assertEquals(1, result.size());
        assertEquals("Story in Range", result.get(0).getStoryname());
    }

    @Test
    public void testGetStoriesByCreationDateRange_NoStartdate() {
        LocalDate endDate = LocalDate.of(2023, 12, 1);

        Story story = new Story();
        story.setStoryname("Story in Range");
        story.setCreationDate(LocalDate.of(2023, 6, 15));

        when(storyRepo.findByCreationDateBefore(endDate))
                .thenReturn(Optional.of(Arrays.asList(story)));
        List<Story> result = storyService.getStoriesByCreationDateRange(Optional.empty(), Optional.of(endDate));

        assertEquals(1, result.size());
        assertEquals("Story in Range", result.get(0).getStoryname());
    }

    @Test
    public void testGetStoriesByCreationDateRange_Unlimited() {
        LocalDate endDate = LocalDate.of(2023, 12, 1);

        Story story = new Story();
        story.setStoryname("Story in Range");
        story.setCreationDate(LocalDate.of(2023, 6, 15));

        when(storyRepo.findAll()).thenReturn(Arrays.asList(story));
        List<Story> result = storyService.getStoriesByCreationDateRange(Optional.empty(), Optional.empty());

        assertEquals(1, result.size());
        assertEquals("Story in Range", result.get(0).getStoryname());
    }

    @Test
    public void testGetStoriesByCreationDateRange_NoMatches() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);

        when(storyRepo.findByCreationDateBetween(startDate, endDate)).thenReturn(Optional.empty());

        List<Story> result = storyService.getStoriesByCreationDateRange(Optional.of(startDate), Optional.of(endDate));

        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetStoriesByLastUpdateRange_Found() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);

        Story story = new Story();
        story.setStoryname("Story Last Updated in Range");
        story.setLastUpdate(LocalDate.of(2023, 6, 15));

        when(storyRepo.findByLastUpdateBetween(startDate, endDate))
                .thenReturn(Optional.of(Arrays.asList(story)));

        List<Story> result = storyService.getStoriesByLastUpdateRange(Optional.of(startDate), Optional.of(endDate));

        assertEquals(1, result.size());
        assertEquals("Story Last Updated in Range", result.get(0).getStoryname());
    }

    @Test
    public void testGetStoriesByLastUpdateRange_NoMatches() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);

        when(storyRepo.findByLastUpdateBetween(startDate, endDate)).thenReturn(Optional.empty());

        List<Story> result = storyService.getStoriesByLastUpdateRange(Optional.of(startDate), Optional.of(endDate));

        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetStoriesByLastUpdateDateRange_OnlyStartDate() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);

        Story story = new Story();
        story.setStoryname("Story After Start Date");
        story.setCreationDate(LocalDate.of(2023, 6, 15));

        when(storyRepo.findByLastUpdateAfter(startDate))
                .thenReturn(Optional.of(Arrays.asList(story)));

        List<Story> result = storyService.getStoriesByLastUpdateRange(Optional.of(startDate), Optional.empty());

        assertEquals(1, result.size());
        assertEquals("Story After Start Date", result.get(0).getStoryname());
    }

    @Test
    public void testGetStoriesByLastUpdateRange_OnlyEndDate() {
        LocalDate endDate = LocalDate.of(2023, 12, 31);

        Story story = new Story();
        story.setStoryname("Story Before End Date");
        story.setLastUpdate(LocalDate.of(2023, 6, 15));

        when(storyRepo.findByLastUpdateBefore(endDate))
                .thenReturn(Optional.of(Arrays.asList(story)));

        List<Story> result = storyService.getStoriesByLastUpdateRange(Optional.empty(), Optional.of(endDate));

        assertEquals(1, result.size());
        assertEquals("Story Before End Date", result.get(0).getStoryname());
    }
    @Test
    public void testGetStoriesByLastUpdateRange_Unlimited() {
        LocalDate endDate = LocalDate.of(2023, 12, 31);

        Story story = new Story();
        story.setStoryname("Story Before End Date");
        story.setLastUpdate(LocalDate.of(2023, 6, 15));

        when(storyRepo.findAll()).thenReturn(Arrays.asList(story));
;

        List<Story> result = storyService.getStoriesByLastUpdateRange(Optional.empty(), Optional.empty());

        assertEquals(1, result.size());
        assertEquals("Story Before End Date", result.get(0).getStoryname());
    }



    @Test
    public void testGetStoriesByCreationDateRange_Success() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);

        Story story = new Story();
        story.setCreationDate(LocalDate.of(2023, 6, 15));

        when(storyRepo.findByCreationDateBetween(startDate, endDate))
                .thenReturn(Optional.of(List.of(story)));

        List<Story> result = storyService.getStoriesByCreationDateRange(Optional.of(startDate), Optional.of(endDate));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(LocalDate.of(2023, 6, 15), result.get(0).getCreationDate());
    }

    @Test
    public void testGetStoriesByNameContainingAndUsername_Success() {
        String storyname = "Adventure";
        String username = "testUser";

        Story story = new Story();
        story.setStoryname("Adventure Story");
        User user = new User();
        user.setUsername(username);
        story.setUser(user);

        when(storyRepo.findByStorynameContainingAndUserUsername(storyname, username))
                .thenReturn(Optional.of(List.of(story)));

        List<Story> result = storyService.getStoriesByNameContainingAndUsername(storyname, username);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Adventure Story", result.get(0).getStoryname());
    }

    @Test
    void testCreateStory_Success() {
        String username = "testUser";
        String email = "xxx@gmail.com";
        String storyname = "Test Story";
        String genre = "Fantasy";
        String desciption = "This is a test story";

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);

        Story story = new Story(storyname, user, genre, LocalDate.now(), LocalDate.now());

        when(userRepo.findByEmail(email)).thenReturn(Optional.of(user));
        when(storyRepo.save(any(Story.class))).thenReturn(story);

        StoryDTO storyDTO = storyService.createStory(email, storyname, genre, desciption);

        assertNotNull(storyDTO);
        assertEquals(storyname, storyDTO.getStoryName());
        assertEquals(genre, storyDTO.getGenre());
        verify(storyRepo, times(1)).save(any(Story.class));
    }

    @Test
    void testCreateStory_UserInfoException() {
        String username = "nonExistentUser";
        String storyname = "Test Story";
        String genre = "Fantasy";
        String desciption = "This is a test story";

        when(userRepo.findById(username)).thenReturn(Optional.empty());

        assertThrows(UserInfoException.class, () -> {
            storyService.createStory(username, storyname, genre, desciption);
        });
    }

    @Test
    void testGetStory_Success() {
        Long storyId = 1L;
        Story story = new Story();
        story.setStoryid(storyId);
        story.setStoryname("Test Story");

        // Create and set a mock User
        User user = new User();
        user.setUsername("testUser");
        story.setUser(user); // Set the User for the Story

        // Create and set a mock Character
        Character character = new Character();
        character.setId(1L);
        character.setFirstName("John");
        character.setLastName("Doe");
        character.setStory(story);

        List<Character> characters = new ArrayList<>();
        characters.add(character);

        when(storyRepo.findById(storyId)).thenReturn(Optional.of(story));
        when(characterRepo.findByStory(story)).thenReturn(characters);

        StoryDTO storyDTO = storyService.getStory(storyId);

        assertNotNull(storyDTO);
        assertEquals("Test Story", storyDTO.getStoryName());
        assertEquals("testUser", storyDTO.getUserName()); // Validate the User is set
        assertEquals(1, storyDTO.getCharacters().size());
        assertEquals("John", storyDTO.getCharacters().get(0).getFirstName());
    }

    @Test
    void testGetStory_UnfoundStoryException() {
        Long storyId = 1L;

        when(storyRepo.findById(storyId)).thenReturn(Optional.empty());

        assertThrows(UnfoundStoryException.class, () -> {
            storyService.getStory(storyId);
        });
    }

    @Test
    void testUpdateStory_Success() {
        Long storyId = 1L;
        String newStoryName = "Updated Story";
        String newGenre = "Updated Genre";
        String newDescription = "Updated Description";

        // Set up the User associated with the Story
        User user = new User();
        user.setUsername("testUser");

        // Set up the existing Story with the associated User
        Story existingStory = new Story();
        existingStory.setStoryid(storyId);
        existingStory.setStoryname("Original Story");
        existingStory.setGenre("Original Genre");
        existingStory.setDescription("Original Description");
        existingStory.setUser(user); // Set the User

        when(storyRepo.findById(storyId)).thenReturn(Optional.of(existingStory));
        when(storyRepo.save(existingStory)).thenReturn(existingStory);

        // Call the updateStory method
        StoryDTO result = storyService.updateStory(storyId, newStoryName, newGenre, newDescription);

        // Assertions to verify the update
        assertNotNull(result);
        assertEquals(newStoryName, result.getStoryName());
        assertEquals(newGenre, result.getGenre());
        assertEquals(newDescription, result.getDescription());

        verify(storyRepo, times(1)).findById(storyId);
        verify(storyRepo, times(1)).save(existingStory);
    }

    @Test
    void testUpdateStory_NotFound() {
        Long storyId = 1L;

        when(storyRepo.findById(storyId)).thenReturn(Optional.empty());

        assertThrows(UnfoundStoryException.class, () -> {
            storyService.updateStory(storyId, "New Story", "New Genre", "New Description");
        });

        verify(storyRepo, times(1)).findById(storyId);
        verify(storyRepo, never()).save(any(Story.class));
    }

    @Test
    void testUpdateStory_EmptyFields() {
        Long storyId = 1L;

        // Set up the User associated with the Story
        User user = new User();
        user.setUsername("testUser");

        // Set up the existing Story with the associated User
        Story existingStory = new Story();
        existingStory.setStoryid(storyId);
        existingStory.setStoryname("Original Story");
        existingStory.setGenre("Original Genre");
        existingStory.setDescription("Original Description");
        existingStory.setUser(user); // Set the User

        when(storyRepo.findById(storyId)).thenReturn(Optional.of(existingStory));
        when(storyRepo.save(existingStory)).thenReturn(existingStory);

        // Call the updateStory method with empty fields
        StoryDTO result = storyService.updateStory(storyId, "", "", "");

        // Assertions to verify the update
        assertNotNull(result);
        assertEquals("", result.getStoryName());
        assertEquals("", result.getGenre());
        assertEquals("", result.getDescription());

        verify(storyRepo, times(1)).findById(storyId);
        verify(storyRepo, times(1)).save(existingStory);
    }

}
