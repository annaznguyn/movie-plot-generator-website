package project.narrative.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "story")
public class Story {

    @Id
    @GeneratedValue
    private Long storyid;
    private String storyname;

    // @OneToMany
    // @JoinColumn(name = "username") // This defines the FK column in the character table
    // private List<Story> stories;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // This defines the FK column in the character table
    private User user; // Username is FK to UserList

    @OneToMany(mappedBy = "story", orphanRemoval = true)
    private List<Character> characters; // OneToMany relationship with Character

    // New fields for creation and last update timestamps
    // @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date", updatable = false)  // Not updatable after creation
    private LocalDate creationDate;

    // @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_update")
    private LocalDate lastUpdate;

    private String genre;

    private String description;

    public Story(String storyname, User user, String genre, LocalDate creationDate, LocalDate lastUpdate) {
        this.storyname = storyname;
        this.user = user;
        this.characters = new ArrayList<>();
        this.genre = genre;
        this.creationDate = creationDate;
        this.lastUpdate = lastUpdate;
        // this.stories = new ArrayList<>();
    }

}
