package project.narrative.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import project.narrative.model.entities.Story;
import project.narrative.model.entities.Character;
import project.narrative.dto.CharacterDTO;
// import project.narrative.dto.characterDTO;

import java.util.List;
import java.util.Date;
import java.time.LocalDate;

@Getter
@Setter
@JsonInclude(Include.NON_EMPTY)
@NoArgsConstructor
public class StoryDTO {
    private String userName;
    private Long storyId;
    private String storyName;
    private List<NodeDTO> rootNodes;

    private LocalDate creationDate;
    private LocalDate lastUpdate;

    private String genre;
    private String description;
    private List<CharacterDTO> characters;
    private String image;

    public StoryDTO(Story story) {
        this.storyId = story.getStoryid();
        this.storyName = story.getStoryname();
        this.userName = story.getUser().getUsername();
        this.creationDate = story.getCreationDate();
        this.lastUpdate = story.getLastUpdate();
        this.genre = story.getGenre();
        this.description = story.getDescription();
    }

    public void setImage(String image) {
        this.image = image;
    }
}
