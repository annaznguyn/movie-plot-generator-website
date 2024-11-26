package project.narrative.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "character")
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String lastName;
    private String firstName;

    @Lob
    private String background;

    @Lob
    private String context;

    @ManyToOne
    @JoinColumn(name = "storyid") // This defines the FK column in the character table
    private Story story; // StoryId is FK to Story

    public Character(String lastName, String firstName, String background, String context, Story story) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.background = background;
        this.context = context;
        this.story = story;
    }

}
