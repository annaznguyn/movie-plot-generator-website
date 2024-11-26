package project.narrative.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import project.narrative.controller.NodeController.CharacterInfo;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "node")
public class Node {

    @Getter
    @Id
    @GeneratedValue
    private Long nodeId;

    @Getter
    private Long parentId;

    @Lob
    private String context; // prompt

    @Lob
    private String description; // shot description

    @Lob
    private String result; // New property to store the AI result or generated content

    @Lob
    private String summary;

    private String nodeName;

    private String genre;

    @Column(columnDefinition = "TEXT")
    private String charactersJson;

    @Transient
    private List<CharacterInfo> characters;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // This defines the FK column in the character table
    private User user; // Username is FK to UserList

    @ManyToOne
    @JoinColumn(name = "storyid") // This defines the FK column in the character table
    private Story story; // StoryId is FK to Story


    public List<CharacterInfo> getCharactersJson() {

        if (charactersJson != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                characters = objectMapper.readValue(charactersJson, new TypeReference<List<CharacterInfo>>() {
                });
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return characters;
    }

    public void setCharacters(List<CharacterInfo> characters) {
        this.characters = characters;

        if (characters != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                this.charactersJson = objectMapper.writeValueAsString(characters);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

}
