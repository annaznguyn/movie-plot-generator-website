package project.narrative.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.narrative.dto.StoryDTO;
import project.narrative.model.APIRequest;
import project.narrative.service.GeminiService;
import project.narrative.service.NodeService;

import java.util.*;

@RestController
@RequestMapping("stories/{storiesId}/nodes")
public class NodeController {

    private final NodeService nodeService;

    private String key;

    public NodeController(NodeService nodeService, @Value("${API_KEY}") String key) {
        this.nodeService = nodeService;
        this.key = key;
    }

    /**
     * Endpoint for creating a new node.
     * 
     * @param prompt contains the description, username, storyid, parentId,
     *               nodeName, and genre of the new node.
     * @return details about the created node.
     */
    @PostMapping
    public ResponseEntity<?> createNode(@RequestBody NodeInit nodeInit, @PathVariable Long storiesId) {
        return ResponseEntity
                .ok(nodeService.createNode(nodeInit.username, storiesId, nodeInit.parentId));
    }

    @PutMapping("/{nodeId}")
    public ResponseEntity<?> updateNode(@RequestBody NodeInfo nodeInfo, @PathVariable Long storiesId,
            @PathVariable Long nodeId) {
        return ResponseEntity.ok(nodeService.updateNode(storiesId, nodeId, nodeInfo.description, nodeInfo.context,
                nodeInfo.nodeName, nodeInfo.summary, nodeInfo.Characters));
    }

    @GetMapping
    public ResponseEntity<?> getGenerated(@PathVariable Long storiesId, @RequestBody Prompt prompt) {
        return ResponseEntity.ok(nodeService.generateResult(key, storiesId, prompt.parentId, prompt.context, prompt.Characters));
    }

    /**
     * Endpoint for deleting a node.
     * 
     * @param nodeId nodeId of the node.
     * @return details about the deleted node.
     */
    @PostMapping("deleteNode")
    public ResponseEntity<?> deleteNode(@RequestBody DeleteData deleteData) {
        nodeService.deleteNode(deleteData.nodeId);
        return ResponseEntity.ok("Delete succesfully!");
    }

    @GetMapping("{nodeId}")
    public ResponseEntity<?> getNode(@PathVariable Long nodeId, @PathVariable Long storiesId) {
        return ResponseEntity.ok(nodeService.getNode(nodeId, storiesId));
    }

    @Data
    public static class DeleteData {
        public Long nodeId;
    }

    /**
     * Data to be edited
     */
    @Data
    public static class EditData {
        public Long nodeId;
        public String context;
        public String description;
        public String nodeName;
    }

    /**
     * InnerAPIController
     */
    @Data
    public static class NodeInit {
        public String username;
        public Long parentId;
    }

    @Data
    public static class NodeInfo {
        public String description;
        public String username;
        public String nodeName;
        public String context;
        public String summary;
        public List<CharacterInfo> Characters;
    }

    @Data
    public static class Prompt {
        public String context;
        public Long parentId;
        public List<CharacterInfo> Characters;
    }

    @Data
    @AllArgsConstructor
    public static class CharacterInfo {
        public String firstName;
        public String lastName;
    }



}