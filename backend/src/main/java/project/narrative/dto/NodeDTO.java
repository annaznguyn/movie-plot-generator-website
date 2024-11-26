package project.narrative.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.narrative.controller.NodeController.CharacterInfo;
import project.narrative.model.entities.Node;
import project.narrative.model.entities.Story;

import java.util.List;

@Getter
@Setter
@JsonInclude(Include.NON_EMPTY)
@NoArgsConstructor
public class NodeDTO {
    private String userName;
    private Long storyId;
    private String storyName;
    private String genre;
    private Long nodeId;
    private String description;
    private String result;
    private String context;
    private Long parentNode;
    private String summary;
    private List<CharacterInfo> characters;
    // Child nodes (the hierarchy under this node)
    private List<NodeDTO> children;

    public NodeDTO(Node node) {
        this.context = node.getContext();
        this.description = node.getDescription();
        this.userName = node.getUser().getUsername();
        this.storyName = node.getStory().getStoryname();
        this.storyId = node.getStory().getStoryid();
        this.result = node.getResult();
        this.nodeId = node.getNodeId();
        this.parentNode = node.getParentId();
        this.genre = node.getStory().getGenre();
        this.characters = node.getCharacters();
        this.summary = node.getSummary();
        System.out.println(parentNode + " " + genre);
    }
}
