package project.narrative.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;
import project.narrative.model.entities.Node;
import project.narrative.model.entities.Story;
import project.narrative.model.entities.Character;

import java.util.List;

@Getter
@Setter
@JsonInclude(Include.NON_EMPTY)
@NoArgsConstructor
public class CharacterDTO {
    private Long character_id;
    private String lastName;
    private String firstName;
    private String background;
    private String context;
    private Long story_id;

    public CharacterDTO(Character character) {
        this.character_id = character.getId();
        this.lastName = character.getLastName();
        this.firstName = character.getFirstName();
        this.background = character.getBackground();
        this.context = character.getContext();
        this.story_id = character.getStory().getStoryid();
    }
}
