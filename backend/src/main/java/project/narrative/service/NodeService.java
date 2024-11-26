package project.narrative.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import project.narrative.controller.NodeController.CharacterInfo;
import project.narrative.dto.CharacterDTO;
import project.narrative.dto.GeneratedResultDTO;
import project.narrative.dto.NodeDTO;
import project.narrative.dto.StoryDTO;
import project.narrative.error.UnfoundStoryException;
import project.narrative.error.UserInfoException;
import project.narrative.error.EditNonLeafNodeException;
import project.narrative.model.entities.Node;
import project.narrative.model.entities.Story;
import project.narrative.model.entities.User;
import project.narrative.repository.*;
import project.narrative.model.entities.Character;
import project.narrative.dto.GeneratedResultDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.LinkedList;
import java.lang.RuntimeException;

@Service
public class NodeService {
    private final UserRepo userRepo;
    private final StoryRepo storyRepo;
    private final NodeRepo nodeRepo;
    private final GeminiService geminiService;
    private final CharacterRepo characterRepo;

    public NodeService(UserRepo userRepo, StoryRepo storyRepo, NodeRepo nodeRepo, GeminiService geminiService,
            CharacterRepo characterRepo) {
        this.nodeRepo = nodeRepo;
        this.userRepo = userRepo;
        this.storyRepo = storyRepo;
        this.geminiService = geminiService;
        this.characterRepo = characterRepo;
    }

    @Transactional
    public NodeDTO createNode(String username, Long storyid, Long parentId) {
        User user = userRepo.findById(username).orElseThrow(() -> new UserInfoException());
        Story story = storyRepo.findById(storyid).orElseThrow(() -> new UnfoundStoryException());

        if (!story.getUser().getUsername().equals(username)) {
            throw new UnfoundStoryException();
        }

        Node node = new Node();
        node.setStory(story);
        node.setParentId(parentId);
        node.setUser(user);

        nodeRepo.save(node);
        return new NodeDTO(node);
    }

    @Transactional
    public NodeDTO updateNode(Long storyid, Long nodeId, String description, String context,
            String nodeName, String summary, List<CharacterInfo> Characters)
            throws UserInfoException, UnfoundStoryException, EditNonLeafNodeException {

        Node node = nodeRepo.findById(nodeId).orElseThrow(() -> new UnfoundStoryException());
        if (!node.getStory().getStoryid().equals(storyid)) {
            throw new UnfoundStoryException();
        }
        if (!nodeRepo.findByParentId(nodeId).get().isEmpty()) {
            throw new EditNonLeafNodeException();
        }
        node.setDescription(description);
        node.setContext(context);
        node.setNodeName(nodeName);
        node.setSummary(summary);
        node.setCharacters(Characters);
        nodeRepo.save(node);
        return new NodeDTO(node);
    }

    @Transactional
    public NodeDTO getNode(Long nodeId, Long storyId) {

        Story story = storyRepo.findById(storyId).orElseThrow(() -> new UnfoundStoryException());
        Node node = nodeRepo.findById(nodeId).orElseThrow();

        // Check if the node belongs to the story
        if (!node.getStory().getStoryid().equals(story.getStoryid())) {
            throw new RuntimeException("Node does not belong to the story");
        }
        // System.out.println(node.getCharactersJson());
        NodeDTO nodeDTO = new NodeDTO(node);

        return nodeDTO;

    }

    @Transactional
    public GeneratedResultDTO generateResult(String key, Long storyid, Long parentId,
            String context, List<CharacterInfo> charactersName) throws UserInfoException, UnfoundStoryException {
        // confirm is this the node root node, cuz root has no context
        Boolean root = false;

        Story story = storyRepo.findById(storyid).orElseThrow(() -> new UnfoundStoryException());

        // Generate New Result from AI

        StringBuilder sb = new StringBuilder();
        if (parentId == null || !nodeRepo.findById(parentId).isPresent()) {
            root = true;
        }

        if("premium".equalsIgnoreCase(story.getUser().getRole())){
            sb.append(String.format("""
                Based on the following provided plot, generate a new story continuation.
                Please ensure the new content is creative, introducing and Guarantee the story style is %s.
                Only return the new story content, keeping it concise and coherent.
                ========================================

                    """, story.getGenre()));
        } else{
            sb.append(String.format("""
                Based on the following provided plot, generate a new story continuation in no more than 500 words.
                Please ensure the new content is creative, introducing and Guarantee the story style is %s.
                Only return the new story content, keeping it concise and coherent.
                ========================================

                    """, story.getGenre()));
        }


        if (root) {
            sb.append("""
                    Plot synopsis: None
                    ====================================

                        """);
        } else {
            sb.append("Plot synopsis: ");
            sb.append("\"" + nodeRepo.findById(parentId).get().getSummary() + "\"");
            sb.append("""
                        ====================================

                    """);
        }

        sb.append("Expected direction of the plot: ");
        sb.append("\" " + context + "\"");

        sb.append("=====================================\n");
        sb.append(
                "Here are some important tasks to help you get to know their names again, as well as various background information: \n");

        for (CharacterInfo name : charactersName) {
            Character character = characterRepo.findByFirstNameAndLastName(name.getFirstName(), name.getLastName())
                    .orElseThrow(() -> new RuntimeException(String
                            .format("There is no character in database called %s %s of story %s", name.getFirstName(),
                                    name.getLastName(),
                                    story.getStoryname())));
            sb.append(String.format("lastName=%s, firstName=%s, background=%s", character.getLastName(),
                    character.getLastName(), character.getBackground()));
            sb.append("=====================================\n");
            // System.out.println(character.toString());
        }

        String result = geminiService.sendAndReceiveRequest(key, sb.toString());
        // String result = "This is a test result";

        // Collect all context form history
        sb = new StringBuilder();
        sb.append(
                """
                        Please combine the following two parts into a coherent and concise story outline. Ensure that no important details are lost, and the resulting story serves as a complete background for future story development.

                        Requirements:
                        - Combine both parts into a smooth and brief story outline, maintaining the flow of the narrative and including all essential details.
                        - Ensure the final outline can be used as a foundation for the next story generation.
                        - Keep the outline within 200 words, focusing on clarity and conciseness.

                        ===================================

                            """);

        if (root) {
            sb.append("""
                        Part 1: Summary of everything that has happened so far
                        "This is begin of whole story"
                    """);
        } else {
            sb.append("""
                        Part 1: Summary of everything that has happened so far
                    """);
            sb.append("\"" + nodeRepo.findById(parentId).get().getContext() + "\"");
            sb.append("""
                        ====================================

                    """);
        }
        sb.append("Part 2: New record");
        sb.append("\"" + result + "\"");
        String summary = geminiService.sendAndReceiveRequest(key, sb.toString());
        // String summary = "This is the summary";

        return new GeneratedResultDTO(result, summary);
    }

    /**
     * Recursive method to build the node hierarchy
     */
    public static NodeDTO buildNodeHierarchy(Node parentNode, NodeRepo nodeRepo) {

        // Check if the node has a user; if not, skip creating NodeDTO
        if (parentNode.getUser() == null) {
            return null;
        }

        // Create a NodeDTO for the current parent node
        NodeDTO nodeDTO = new NodeDTO(parentNode);
        nodeDTO.setNodeId(parentNode.getNodeId());
        nodeDTO.setDescription(parentNode.getDescription());
        nodeDTO.setContext(parentNode.getContext());

        // Find all child nodes of the current node using Optional
        Optional<List<Node>> optionalChildNodes = nodeRepo.findByParentId(parentNode.getNodeId());
        List<NodeDTO> childNodeDTOs = new ArrayList<>();

        // Check if child nodes are present and populate the list of NodeDTOs
        if (optionalChildNodes.isPresent()) {
            List<Node> childNodes = optionalChildNodes.get();
            for (Node childNode : childNodes) {
                NodeDTO childNodeDTO = buildNodeHierarchy(childNode, nodeRepo); // Recursively build the hierarchy
                childNodeDTOs.add(childNodeDTO);
            }
        }

        // Set the child nodes in the parent NodeDTO
        nodeDTO.setChildren(childNodeDTOs);

        return nodeDTO;
    }


    /**
     * Deleting a node.
     * 
     * @param nodeId nodeId of the node.
     * @return deleted node.
     */
    @Transactional
    public void deleteNode(Long nodeId) {
        Queue<Long> toBeDeletedNodes = new LinkedList<>();
        toBeDeletedNodes.add(nodeId);

        while (!toBeDeletedNodes.isEmpty()) {
            Long currentNode = toBeDeletedNodes.poll();
            nodeRepo.deleteById(currentNode);

            // If the current node has children, add them to the queue to remove them
            List<Node> children = nodeRepo.findByParentId(currentNode).orElse(Collections.emptyList());
            if (!children.isEmpty()) {
                for (Node n : children) {
                    toBeDeletedNodes.add(n.getNodeId());
                }
            }
        }
    }

}