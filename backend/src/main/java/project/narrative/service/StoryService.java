package project.narrative.service;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import project.narrative.dto.NodeDTO;
import project.narrative.dto.StoryDTO;
import project.narrative.error.UserInfoException;
import project.narrative.error.UnfoundStoryException;
import project.narrative.model.entities.Node;
import project.narrative.model.entities.Story;
import project.narrative.model.entities.User;
import project.narrative.model.entities.Character;
import project.narrative.dto.CharacterDTO;
import project.narrative.repository.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.time.LocalDate;

@Service
public class StoryService {
    private final StoryRepo storyRepo;
    private final UserRepo userRepo;
    private final NodeRepo nodeRepo;
    private final CharacterRepo characterRepo;

    public StoryService(StoryRepo storyRepo, UserRepo userRepo, NodeRepo nodeRepo, CharacterRepo characterRepo) {
        this.storyRepo = storyRepo;
        this.nodeRepo = nodeRepo;
        this.userRepo = userRepo;
        this.characterRepo = characterRepo;
    }

    @Transactional
    public StoryDTO createStory(String email, String storyname, String genre, String description) {
        User user = userRepo.findByEmail(email).orElseThrow(() -> new UserInfoException());
        System.out.println(storyname + " " + genre + " "+ description);
        Story story = new Story(storyname, user, genre, LocalDate.now(), LocalDate.now());
        story.setDescription(description);
        storyRepo.save(story);
        return new StoryDTO(story);
    }

    @Transactional
    public StoryDTO getStory(Long storyId) {
        Story story = storyRepo.findById(storyId).orElseThrow(() -> new UnfoundStoryException());
        List<Character> characters = characterRepo.findByStory(story);
        List<CharacterDTO> charactersDTO = new ArrayList<CharacterDTO>();
        for (Character character : characters) {
            charactersDTO.add(new CharacterDTO(character));
        }

        StoryDTO storyDTO = new StoryDTO(story);
        storyDTO.setCharacters(charactersDTO);
        return storyDTO;
    }

    @Transactional
    public StoryDTO updateStory(Long storyId, String storyname, String genre, String description) {
        Story story = storyRepo.findById(storyId).orElseThrow(() -> new UnfoundStoryException());
        
        story.setStoryname(storyname);
        story.setGenre(genre);
        story.setDescription(description);
        storyRepo.save(story);
        return new StoryDTO(story);
    }

    @Transactional
    public StoryDTO deleteStory (Long storyId) {
        Story story = storyRepo.findById(storyId).orElseThrow(() -> new UnfoundStoryException());
        StoryDTO dto = new StoryDTO((story));
        storyRepo.deleteById(storyId);
        return dto;
    }

    // Method to find stories where storyname contains a substring and username
    // matches
    @Transactional
    public List<Story> getStoriesByNameContainingAndUsername(String storyname, String username) {
        // Returning a list of stories if found, or an empty list if not found
        return storyRepo.findByStorynameContainingAndUserUsername(storyname, username)
                .orElse(Collections.emptyList());
    }

    // Get stories created within a specific time range or open-ended
    @Transactional
    public List<Story> getStoriesByCreationDateRange(Optional<LocalDate> startDateOptional, Optional<LocalDate> endDateOptional) {
        LocalDate startDate = startDateOptional.orElse(null);
        LocalDate endDate = endDateOptional.orElse(null);

        if (startDate != null && endDate != null) {
            return storyRepo.findByCreationDateBetween(startDate, endDate)
                    .orElse(Collections.emptyList());
        } else if (startDate != null) {
            return storyRepo.findByCreationDateAfter(startDate)
                    .orElse(Collections.emptyList());
        } else if (endDate != null) {
            return storyRepo.findByCreationDateBefore(endDate)
                    .orElse(Collections.emptyList());
        } else {
            // If both startDate and endDate are null, return all stories
            return storyRepo.findAll();
        }
    }

    // Get stories updated within a specific time range or open-ended
    @Transactional
    public List<Story> getStoriesByLastUpdateRange(Optional<LocalDate> startDateOptional, Optional<LocalDate> endDateOptional) {
        LocalDate startDate = startDateOptional.orElse(null);
        LocalDate endDate = endDateOptional.orElse(null);

        if (startDate != null && endDate != null) {
            return storyRepo.findByLastUpdateBetween(startDate, endDate)
                    .orElse(Collections.emptyList());
        } else if (startDate != null) {
            return storyRepo.findByLastUpdateAfter(startDate)
                    .orElse(Collections.emptyList());
        } else if (endDate != null) {
            return storyRepo.findByLastUpdateBefore(endDate)
                    .orElse(Collections.emptyList());
        } else {
            // If both startDate and endDate are null, return all stories
            return storyRepo.findAll();
        }
    }


    public List<StoryDTO> getStoriesWithNodeHierarchyForUser(String email) {
        // Retrieve user from repository
        User user = userRepo.findByEmail(email)
                .orElseThrow(UserInfoException::new);
        System.out.println("User retrieved: " + user);


        // Retrieve all stories for the user using Optional
        List<Story> stories = storyRepo.findByUserUsername(user.getUsername()).orElseThrow(UnfoundStoryException::new);
        System.out.println("Stories retrieved: " + stories);

        // Create a list to hold StoryDTOs
        List<StoryDTO> storyDTOs = new ArrayList<>();

        // Iterate through each story
        for (Story story : stories) {
            System.out.println("Processing story: " + story);

            // Find all root nodes for the current story (those with no parent)
            List<Node> rootNodes = nodeRepo.findByStoryStoryidAndParentIdIsNull(story.getStoryid())
                    .orElse(Collections.emptyList()); // Handle case when no root nodes exist
            System.out.println("Root nodes retrieved: " + rootNodes);

            // Create a list to hold root NodeDTOs
            List<NodeDTO> rootNodeDTOs = new ArrayList<>();

            // Build the node hierarchy for each root node
            for (Node rootNode : rootNodes) {
                NodeDTO rootNodeDTO = NodeService.buildNodeHierarchy(rootNode, nodeRepo);
                rootNodeDTOs.add(rootNodeDTO);
            }

            // Create a StoryDTO object and populate it
            StoryDTO storyDTO = new StoryDTO(story);
            storyDTO.setStoryId(story.getStoryid());
            storyDTO.setStoryName(story.getStoryname());
            storyDTO.setRootNodes(rootNodeDTOs);

            // Add the StoryDTO to the list
            storyDTOs.add(storyDTO);
        }

        // Return the list of StoryDTOs
        return storyDTOs;
    }



}
