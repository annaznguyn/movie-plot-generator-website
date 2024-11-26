package project.narrative.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.narrative.model.entities.Story;
import project.narrative.model.entities.User;

import java.time.LocalDate;

@Repository
public interface StoryRepo extends JpaRepository<Story, Long> {

    // Find story by storyid and user, wrapped in Optional
    Optional<Story> findByStoryidAndUser(Long storyid, User user);

    // Find all stories by username, wrapped in Optional if needed
    Optional<List<Story>> findByUserUsername(String username);

    // Find stories where the storyname contains the specified substring, wrapped in Optional
    Optional<List<Story>> findByStorynameContainingAndUserUsername(String storyname, String username);

    // Find stories created within a specific date range
    Optional<List<Story>> findByCreationDateBetween(LocalDate startDate, LocalDate endDate);

    // Find stories updated within a specific date range
    Optional<List<Story>> findByLastUpdateBetween(LocalDate startDate, LocalDate endDate);

    // Open-ended time range queries
    Optional<List<Story>> findByCreationDateAfter(LocalDate startDate);
    Optional<List<Story>> findByCreationDateBefore(LocalDate endDate);

    Optional<List<Story>> findByLastUpdateAfter(LocalDate startDate);
    Optional<List<Story>> findByLastUpdateBefore(LocalDate endDate);

    // Find stories by username and creation date
    Optional<List<Story>> findByUserUsernameAndCreationDate(String username, LocalDate creationDate);

    // Find stories by username and last update date
    Optional<List<Story>> findByUserUsernameAndLastUpdate(String username, LocalDate lastUpdate);

    // Find stories by username and storyname
    Optional<List<Story>> findByUserUsernameAndStoryname(String username, String storyname);

    // Find stories by username and genre
    Optional<List<Story>> findByUserUsernameAndGenre(String username, String genre);
}