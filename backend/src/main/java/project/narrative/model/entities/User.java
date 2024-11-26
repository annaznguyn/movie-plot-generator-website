package project.narrative.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private Date registration_date;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String hashed_pwd;

    private String salt;

    @OneToMany(mappedBy = "user")
    private List<Story> stories;

    @OneToMany(mappedBy = "user")
    private List<Node> nodes;

    public User(UUID userId, String username, Date date, String email, String role, String hashed_pwd,
            String salt) {
        this.userId = userId;
        this.username = username;
        this.registration_date = date;
        this.email = email;
        this.role = role;
        this.hashed_pwd = hashed_pwd;
        this.salt = salt;
        this.stories = new ArrayList<>();
        this.nodes = new ArrayList<>();
    }

    // Getters and Setters
}
