package project.narrative.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import project.narrative.model.entities.Character;
import project.narrative.model.entities.Node;
import project.narrative.model.entities.Story;

public interface CharacterRepo extends JpaRepository<Character, Long> {

    Optional<Character> findByIdAndStory(Long id, Story story);

    List<Character> findByStory(Story story);

    Optional<Character> findByFirstNameAndStory(String fname, Story story);

    Optional<Character> findByFirstName(String name);

    Optional<Character> findByFirstNameAndLastName(String firstName, String lastName);

}
