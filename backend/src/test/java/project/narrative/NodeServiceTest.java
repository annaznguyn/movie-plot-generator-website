package project.narrative;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import project.narrative.dto.GeneratedResultDTO;
import project.narrative.dto.NodeDTO;
import project.narrative.dto.StoryDTO;
import project.narrative.error.EditNonLeafNodeException;
import project.narrative.error.UnfoundStoryException;
import project.narrative.error.UserInfoException;
import project.narrative.model.entities.Character;
import project.narrative.model.entities.Node;
import project.narrative.model.entities.Story;
import project.narrative.model.entities.User;
import project.narrative.repository.*;
import project.narrative.service.GeminiService;
import project.narrative.service.NodeService;
import project.narrative.controller.NodeController.CharacterInfo;
import project.narrative.service.StoryService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class NodeServiceTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private StoryRepo storyRepo;

    @Mock
    private NodeRepo nodeRepo;

    @InjectMocks
    private NodeService nodeService;

    @InjectMocks
    private StoryService storyService;

    @Mock
    private GeminiService geminiService;

    @Mock
    private CharacterRepo characterRepo;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetStoriesWithNodeHierarchyForUser() {
        // Arrange: Mock the user
        User user = new User();
        user.setUsername("john_doe");
        user.setEmail("xxx@gmail.com");

        Story story = new Story();
        story.setUser(user);
        story.setStoryid(1L);
        story.setStoryname("Sample Story");

        Node rootNode = new Node();
        rootNode.setStory(story);
        rootNode.setParentId(null);

        // Mock repository responses
        when(userRepo.findByEmail("xxx@gmail.com")).thenReturn(Optional.of(user));
        when(storyRepo.findByUserUsername("john_doe")).thenReturn(Optional.of(List.of(story)));
        when(nodeRepo.findByStoryStoryidAndParentIdIsNull(1L)).thenReturn(Optional.of(List.of(rootNode)));

        List<StoryDTO> stories = storyService.getStoriesWithNodeHierarchyForUser("xxx@gmail.com");

        // Assert: Check if data is retrieved as expected
        assertNotNull(stories);
        assertFalse(stories.isEmpty());
        assertEquals("Sample Story", stories.get(0).getStoryName());
    }

    /**
     * with more stories each one having multiple parent nodes and deeper child
     * nodes
     */
    @Test
    public void testGetStoriesWithNodeHierarchyForUser2() {
        // Mock User
        User mockUser = new User();
        mockUser.setUsername("john_doe");
        mockUser.setEmail("xxx@gmail.com");

        // Mock data for first story
        Story mockStory1 = new Story();
        mockStory1.setStoryid(1L);
        mockStory1.setStoryname("First Story");
        mockStory1.setUser(mockUser); // Link to user

        // Mock data for second story
        Story mockStory2 = new Story();
        mockStory2.setStoryid(2L);
        mockStory2.setStoryname("Second Story");
        mockStory2.setUser(mockUser); // Link to user

        // Mock root nodes for first story, pass the user and story
        Node rootNode1 = createNode(1L, null, "Root Node 1 - Story 1", mockUser, mockStory1);
        Node rootNode2 = createNode(2L, null, "Root Node 2 - Story 1", mockUser, mockStory1);

        Node node1_1 = createNode(5L, rootNode1.getNodeId(), "Root Node 1-1 - Story 1", mockUser, mockStory1);
        Node node1_2 = createNode(6L, rootNode1.getNodeId(), "Root Node 1-2 - Story 1", mockUser, mockStory1);

        Node node1_1_1 = createNode(7L, node1_1.getNodeId(), "Root Node 1-1-1 - Story 1", mockUser, mockStory1);

        // Mock root nodes for second story, pass the user and story
        Node rootNode3 = createNode(3L, null, "Root Node 1 - Story 2", mockUser, mockStory2);
        Node rootNode4 = createNode(4L, null, "Root Node 2 - Story 2", mockUser, mockStory2);

        // Mock repository behavior
        when(userRepo.findByEmail("xxx@gmail.com"))
                .thenReturn(Optional.of(mockUser));

        when(storyRepo.findByUserUsername("john_doe"))
                .thenReturn(Optional.of(Arrays.asList(mockStory1, mockStory2)));

        when(nodeRepo.findByStoryStoryidAndParentIdIsNull(1L))
                .thenReturn(Optional.of(Arrays.asList(rootNode1, rootNode2)));
        when(nodeRepo.findByStoryStoryidAndParentIdIsNull(2L))
                .thenReturn(Optional.of(Arrays.asList(rootNode3, rootNode4)));

        when(nodeRepo.findByParentId(1L))
                .thenReturn(Optional.of(Arrays.asList(node1_1, node1_2)));
        when(nodeRepo.findByStoryStoryidAndParentIdIsNull(5L))
                .thenReturn(Optional.of(Arrays.asList(node1_1_1)));

        // Call the service method
        List<StoryDTO> result = storyService.getStoriesWithNodeHierarchyForUser("xxx@gmail.com");

        List<StoryDTO> sresult = result;
        // Verifications
        verify(userRepo, times(1)).findByEmail("xxx@gmail.com");
        verify(storyRepo, times(1)).findByUserUsername("john_doe");
        verify(nodeRepo, times(1)).findByStoryStoryidAndParentIdIsNull(1L);
        verify(nodeRepo, times(1)).findByStoryStoryidAndParentIdIsNull(2L);
        // Assertions
        assertEquals(2, result.size()); // Two stories for this user

        // First Story Assertions
        StoryDTO firstStoryDTO = result.get(0);
        assertEquals(1L, firstStoryDTO.getStoryId());
        assertEquals("First Story", firstStoryDTO.getStoryName());

        // Verify root nodes of first story
        List<NodeDTO> firstStoryRootNodes = firstStoryDTO.getRootNodes();
        assertEquals(2, firstStoryRootNodes.size());
    }

    private Node createNode(Long nodeId, Long parentId, String description, User user, Story story) {
        Node node = new Node();
        node.setNodeId(nodeId);
        node.setParentId(parentId);
        node.setDescription(description);
        node.setUser(user); // Set the user for the node
        node.setStory(story); // Set the story for the node
        return node;
    }

    @Test
    void testCreateNode_Success() {
        String username = "testUser";
        Long storyId = 1L;
        Long parentId = null;

        User user = new User();
        user.setUsername(username);

        Story story = new Story();
        story.setStoryid(storyId);
        story.setUser(user);

        when(userRepo.findById(username)).thenReturn(Optional.of(user));
        when(storyRepo.findById(storyId)).thenReturn(Optional.of(story));

        NodeDTO nodeDTO = nodeService.createNode(username, storyId, parentId);

        assertNotNull(nodeDTO);
        verify(nodeRepo, times(1)).save(any(Node.class));
    }

    @Test
    void testCreateNode_UnfoundStoryException() {
        String username = "testUser";
        Long storyId = 1L;
        Long parentId = null;

        when(userRepo.findById(username)).thenReturn(Optional.of(new User()));
        when(storyRepo.findById(storyId)).thenReturn(Optional.empty());

        assertThrows(UnfoundStoryException.class, () -> {
            nodeService.createNode(username, storyId, parentId);
        });
    }

    @Test
    void testUpdateNode_Success() {
        Long storyId = 1L;
        Long nodeId = 1L;
        String description = "New description";
        String context = "New context";
        String nodeName = "New node name";
        String summary = "New summary";
        List<CharacterInfo> characters = new ArrayList<>();

        User user = new User();
        user.setUsername("testUser");

        Story story = new Story();
        story.setStoryid(storyId);
        story.setUser(user);

        Node node = new Node();
        node.setNodeId(nodeId);
        node.setStory(story);
        node.setUser(user);

        when(nodeRepo.findById(nodeId)).thenReturn(Optional.of(node));
        when(nodeRepo.findByParentId(nodeId)).thenReturn(Optional.of(new ArrayList<>()));

        NodeDTO updatedNodeDTO = nodeService.updateNode(storyId, nodeId, description, context, nodeName, summary,
                characters);

        assertNotNull(updatedNodeDTO);
        assertEquals(description, updatedNodeDTO.getDescription());
        verify(nodeRepo, times(1)).save(node);
    }

    @Test
    void testUpdateNode_EditNonLeafNodeException() {
        Long storyId = 1L;
        Long nodeId = 1L;

        Node node = new Node();
        node.setNodeId(nodeId);
        Story story = new Story();
        story.setStoryid(storyId);
        node.setStory(story);

        List<Node> childNodes = new ArrayList<>();
        childNodes.add(new Node());

        when(nodeRepo.findById(nodeId)).thenReturn(Optional.of(node));
        when(nodeRepo.findByParentId(nodeId)).thenReturn(Optional.of(childNodes));

        assertThrows(EditNonLeafNodeException.class, () -> {
            nodeService.updateNode(storyId, nodeId, "", "", "", "", new ArrayList<>());
        });
    }

    @Test
    void testGetNode_Success() {
        Long nodeId = 1L;
        Long storyId = 1L;

        Story story = new Story();
        story.setStoryid(storyId);

        User user = new User();
        user.setUsername("testUser");

        Node node = new Node();
        node.setNodeId(nodeId);
        node.setStory(story);
        node.setUser(user);

        when(storyRepo.findById(storyId)).thenReturn(Optional.of(story));
        when(nodeRepo.findById(nodeId)).thenReturn(Optional.of(node));

        NodeDTO nodeDTO = nodeService.getNode(nodeId, storyId);

        assertNotNull(nodeDTO);
        assertEquals(nodeId, nodeDTO.getNodeId());
    }

    @Test
    void testGetNode_NodeDoesNotBelongToStory() {
        Long nodeId = 1L;
        Long storyId = 1L;

        Story story = new Story();
        story.setStoryid(storyId);

        Story otherStory = new Story();
        otherStory.setStoryid(2L);

        Node node = new Node();
        node.setNodeId(nodeId);
        node.setStory(otherStory);

        when(storyRepo.findById(storyId)).thenReturn(Optional.of(story));
        when(nodeRepo.findById(nodeId)).thenReturn(Optional.of(node));

        assertThrows(RuntimeException.class, () -> {
            nodeService.getNode(nodeId, storyId);
        });
    }

@Test
void testGenerateResult_Success() throws Exception {
    String key = "testKey";
    Long storyId = 1L;
    Long parentId = null;
    String context = "Test context";
    List<CharacterInfo> characters = new ArrayList<>();

    Story story = new Story();
    story.setStoryid(storyId);
    story.setGenre("Fantasy");

    // Setting up a User object with a role
    User user = new User();
    user.setRole("user");  // Set this to the role required in your service logic
    story.setUser(user);    // Associate the user with the story

    when(storyRepo.findById(storyId)).thenReturn(Optional.of(story));
    when(geminiService.sendAndReceiveRequest(anyString(), anyString())).thenReturn("Generated content");

    GeneratedResultDTO resultDTO = nodeService.generateResult(key, storyId, parentId, context, characters);

    assertNotNull(resultDTO);
    assertEquals("Generated content", resultDTO.getResult());
}


@Test
void testGenerateResult_CharacterNotFound() {
    String key = "testKey";
    Long storyId = 1L;
    Long parentId = null;
    String context = "Test context";
    List<CharacterInfo> characters = new ArrayList<>();
    characters.add(new CharacterInfo("John", "Doe"));

    Story story = new Story();
    story.setStoryid(storyId);
    story.setStoryname("Test Story");
    story.setGenre("Fantasy");

    // Setting up a User object with a role
    User user = new User();
    user.setRole("user");  // Set to the required role
    story.setUser(user);   // Associate the user with the story

    when(storyRepo.findById(storyId)).thenReturn(Optional.of(story));
    when(characterRepo.findByFirstNameAndLastName("John", "Doe")).thenReturn(Optional.empty());

    RuntimeException exception = assertThrows(RuntimeException.class, () -> {
        nodeService.generateResult(key, storyId, parentId, context, characters);
    });

    assertEquals("There is no character in database called John Doe of story Test Story", exception.getMessage());
}

@Test
void testGenerateResult_CharacterFound() {
    String key = "testKey";
    Long storyId = 1L;
    Long parentId = null;
    String context = "Test context";
    List<CharacterInfo> characters = new ArrayList<>();
    characters.add(new CharacterInfo("John", "Doe"));

    Story story = new Story();
    story.setStoryid(storyId);
    story.setStoryname("Test Story");
    story.setGenre("Fantasy");

    // Setting up a User object with a role
    User user = new User();
    user.setRole("user");  // Set to the required role
    story.setUser(user);   // Associate the user with the story

    Character character = new Character();
    character.setFirstName("John");
    character.setLastName("Doe");
    character.setStory(story);

    when(storyRepo.findById(storyId)).thenReturn(Optional.of(story));
    when(characterRepo.findByFirstNameAndLastName("John", "Doe")).thenReturn(Optional.of(character));

    assertDoesNotThrow(() -> {
        GeneratedResultDTO resultNodeDTO = nodeService.generateResult(key, storyId, parentId, context, characters);
        assertNotNull(resultNodeDTO);
    });
}


    @Test
    void testGenerateResult_StoryNotFound() {
        String key = "testKey";
        Long storyId = 1L;
        Long parentId = null;
        String context = "Test context";
        List<CharacterInfo> characters = new ArrayList<>();

        when(storyRepo.findById(storyId)).thenReturn(Optional.empty());

        assertThrows(UnfoundStoryException.class, () -> {
            nodeService.generateResult(key, storyId, parentId, context, characters);
        });
    }

    @Test
    void testDeleteNode_SingleNode() {
        Long nodeId = 1L;

        doNothing().when(nodeRepo).deleteById(nodeId);

        nodeService.deleteNode(nodeId);

        verify(nodeRepo, times(1)).deleteById(nodeId);
    }

    @Test
    void testDeleteNode_WithChildren() {
        Long parentId = 1L;
        Long childId1 = 2L;
        Long childId2 = 3L;

        Node child1 = new Node();
        child1.setNodeId(childId1);
        Node child2 = new Node();
        child2.setNodeId(childId2);

        List<Node> children = Arrays.asList(child1, child2);

        when(nodeRepo.findByParentId(parentId)).thenReturn(Optional.of(children));
        doNothing().when(nodeRepo).deleteById(anyLong());

        nodeService.deleteNode(parentId);

        verify(nodeRepo, times(1)).deleteById(parentId);
        verify(nodeRepo, times(1)).deleteById(childId1);
        verify(nodeRepo, times(1)).deleteById(childId2);
    }

    @Test
    void testGenerateResult_RootNode() throws Exception {
        String key = "testKey";
        Long storyId = 1L;
        Long parentId = null; // Represents root node
        String context = "Test context";
        List<CharacterInfo> characters = new ArrayList<>();
    
        Story story = new Story();
        story.setStoryid(storyId);
        story.setGenre("Fantasy");
    
        User user = new User();
        user.setRole("premium"); // Set user role
        story.setUser(user); // Associate user with the story
    
        when(storyRepo.findById(storyId)).thenReturn(Optional.of(story));
        when(geminiService.sendAndReceiveRequest(anyString(), anyString())).thenReturn("Generated content");
    
        GeneratedResultDTO resultDTO = nodeService.generateResult(key, storyId, parentId, context, characters);
    
        assertNotNull(resultDTO);
        assertEquals("Generated content", resultDTO.getResult());
        verify(geminiService, times(2)).sendAndReceiveRequest(anyString(), anyString());
    }
    
    @Test
    void testGenerateResult_NonRootNode() throws Exception {
        String key = "testKey";
        Long storyId = 1L;
        Long parentId = 2L; // Represents a non-root node
        String context = "Test context";
        List<CharacterInfo> characters = new ArrayList<>();
    
        Story story = new Story();
        story.setStoryid(storyId);
        story.setGenre("Fantasy");
    
        User user = new User();
        user.setRole("user"); // Set user role
        story.setUser(user); // Associate user with the story
    
        Node parentNode = new Node();
        parentNode.setNodeId(parentId);
        parentNode.setSummary("Parent node summary");
    
        when(storyRepo.findById(storyId)).thenReturn(Optional.of(story));
        when(nodeRepo.findById(parentId)).thenReturn(Optional.of(parentNode));
        when(geminiService.sendAndReceiveRequest(anyString(), anyString())).thenReturn("Generated content");
    
        GeneratedResultDTO resultDTO = nodeService.generateResult(key, storyId, parentId, context, characters);
    
        assertNotNull(resultDTO);
        assertEquals("Generated content", resultDTO.getResult());
    }
    

    @Test
    void testCreateNode_UserInfoException() {
        String username = "testUser";
        Long storyId = 1L;
        Long parentId = null;

        when(userRepo.findById(username)).thenReturn(Optional.empty());

        assertThrows(UserInfoException.class, () -> {
            nodeService.createNode(username, storyId, parentId);
        });
    }

    @Test
    void testCreateNode_StoryUsernameMismatch() {
        String username = "testUser";
        Long storyId = 1L;
        Long parentId = null;

        User user = new User();
        user.setUsername(username);

        Story story = new Story();
        story.setStoryid(storyId);
        User otherUser = new User();
        otherUser.setUsername("otherUser");
        story.setUser(otherUser);

        when(userRepo.findById(username)).thenReturn(Optional.of(user));
        when(storyRepo.findById(storyId)).thenReturn(Optional.of(story));

        assertThrows(UnfoundStoryException.class, () -> {
            nodeService.createNode(username, storyId, parentId);
        });
    }

    @Test
    void testUpdateNode_NodeNotBelongToStory() {
        Long storyId = 1L;
        Long nodeId = 1L;

        Node node = new Node();
        node.setNodeId(nodeId);
        Story otherStory = new Story();
        otherStory.setStoryid(2L); // Different story ID
        node.setStory(otherStory);

        when(nodeRepo.findById(nodeId)).thenReturn(Optional.of(node));

        assertThrows(UnfoundStoryException.class, () -> {
            nodeService.updateNode(storyId, nodeId, "", "", "", "", new ArrayList<>());
        });
    }

    @Test
    void testGetNode_NodeDoesNotBelongToStory2() {
        Long nodeId = 1L;
        Long storyId = 1L;

        Story story = new Story();
        story.setStoryid(storyId);

        Story otherStory = new Story();
        otherStory.setStoryid(2L); // Different story ID

        Node node = new Node();
        node.setNodeId(nodeId);
        node.setStory(otherStory);

        when(storyRepo.findById(storyId)).thenReturn(Optional.of(story));
        when(nodeRepo.findById(nodeId)).thenReturn(Optional.of(node));

        assertThrows(RuntimeException.class, () -> {
            nodeService.getNode(nodeId, storyId);
        });
    }

}
