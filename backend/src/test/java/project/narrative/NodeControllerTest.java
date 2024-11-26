package project.narrative;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import project.narrative.controller.NodeController;
import project.narrative.controller.NodeController.CharacterInfo;
import project.narrative.controller.NodeController.NodeInfo;
import project.narrative.controller.NodeController.NodeInit;
import project.narrative.controller.NodeController.Prompt;
import project.narrative.controller.NodeController.DeleteData;
import project.narrative.dto.NodeDTO;
import project.narrative.service.NodeService;
import project.narrative.dto.GeneratedResultDTO;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class NodeControllerTest {

    @Mock
    private NodeService nodeService;

    @InjectMocks
    private NodeController nodeController;

    @Value("${API_KEY}")
    private String key;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateNode_Success() {
        Long storiesId = 1L;
        NodeInit nodeInit = new NodeInit();
        nodeInit.username = "testUser";
        nodeInit.parentId = null;

        NodeDTO expectedNode = new NodeDTO(); // Use a mock NodeDTO object
        when(nodeService.createNode(nodeInit.username, storiesId, nodeInit.parentId)).thenReturn(expectedNode);

        ResponseEntity<?> response = nodeController.createNode(nodeInit, storiesId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedNode, response.getBody());
    }

    @Test
    void testUpdateNode_Success() {
        Long storiesId = 1L;
        Long nodeId = 2L;
        NodeInfo nodeInfo = new NodeInfo();
        nodeInfo.description = "Updated Description";
        nodeInfo.context = "Updated Context";
        nodeInfo.nodeName = "Updated Name";
        nodeInfo.summary = "Updated Summary";
        nodeInfo.Characters = Collections.singletonList(new CharacterInfo("John", "Doe"));

        NodeDTO updatedNode = new NodeDTO(); // Use a mock NodeDTO object
        when(nodeService.updateNode(storiesId, nodeId, nodeInfo.description, nodeInfo.context, nodeInfo.nodeName,
                nodeInfo.summary, nodeInfo.Characters))
                .thenReturn(updatedNode);

        ResponseEntity<?> response = nodeController.updateNode(nodeInfo, storiesId, nodeId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedNode, response.getBody());
    }

    @Test
    void testGetGenerated_Success() {
        Long storiesId = 1L;
        Prompt prompt = new Prompt();
        prompt.context = "Generate Context";
        prompt.parentId = null;
        prompt.Characters = Collections.singletonList(new CharacterInfo("Alice", "Smith"));

        // Create a mock GeneratedResultDTO to return from generateResult
        GeneratedResultDTO generatedResult = new GeneratedResultDTO();
        generatedResult.setResult("Generated Content");

        when(nodeService.generateResult(key, storiesId, prompt.parentId, prompt.context, prompt.Characters))
                .thenReturn(generatedResult);

        ResponseEntity<?> response = nodeController.getGenerated(storiesId, prompt);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(generatedResult, response.getBody());
    }

    @Test
    void testDeleteNode_Success() {
        DeleteData deleteData = new DeleteData();
        deleteData.nodeId = 1L;

        doNothing().when(nodeService).deleteNode(deleteData.nodeId);

        ResponseEntity<?> response = nodeController.deleteNode(deleteData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Delete succesfully!", response.getBody());
    }

    @Test
    void testGetNode_Success() {
        Long nodeId = 1L;
        Long storiesId = 1L;

        NodeDTO nodeDetails = new NodeDTO(); // Use a mock NodeDTO object
        when(nodeService.getNode(nodeId, storiesId)).thenReturn(nodeDetails);

        ResponseEntity<?> response = nodeController.getNode(nodeId, storiesId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(nodeDetails, response.getBody());
    }
}
