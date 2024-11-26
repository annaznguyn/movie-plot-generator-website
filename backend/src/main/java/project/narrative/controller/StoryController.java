package project.narrative.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.WebProperties.Resources.Chain.Strategy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.Data;
import project.narrative.dto.StoryDTO;
import project.narrative.error.UnfoundStoryException;
import project.narrative.error.UserInfoException;
import project.narrative.model.APIRequest;
import project.narrative.model.entities.Story;
import project.narrative.service.GeminiService;
import project.narrative.service.StoryService;

import java.util.*;

@RestController
@RequestMapping("/stories")
public class StoryController {

    private final StoryService storyService;

    private String key;

    public StoryController(StoryService storyService, @Value("${API_KEY}") String key) {
        this.key = key;
        this.storyService = storyService;
    }

    @PostMapping
    public ResponseEntity<?> createStory(@RequestBody StoryInfo storyInfo) {
        System.out.println(storyInfo.storyname + " " + storyInfo.genre + " "+ storyInfo.description);
        return ResponseEntity
                .ok(storyService.createStory(storyInfo.email, storyInfo.storyname, storyInfo.genre, storyInfo.description));
    }

    @DeleteMapping("/{storyId}/delete")
    public ResponseEntity<?> deleteStory(@PathVariable Long storyId) {
        System.out.println(storyId + " ");
        if(storyService.getStory(storyId) != null){
            return ResponseEntity
                    .ok(storyService.deleteStory(storyId));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
    }


    @PutMapping("/{storyId}")
    public ResponseEntity<?> updateStory(@PathVariable Long storyId, @RequestBody StoryInfo storyInfo) {
        return ResponseEntity.ok(storyService.updateStory(storyId, storyInfo.storyname, storyInfo.genre, storyInfo.description));
    }

    @GetMapping("/{storyId}")
    public ResponseEntity<?> getStory(@PathVariable Long storyId) {
        return ResponseEntity.ok(storyService.getStory(storyId));
    }

    /**
     * Endpoint for get all stories of a user
     */
    @GetMapping
    public ResponseEntity<List<StoryDTO>> getStoriesForUser(@RequestParam("email") String email) {
        try {
            List<StoryDTO> stories = storyService.getStoriesWithNodeHierarchyForUser(email);
            return stories.isEmpty()
                    ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                    : ResponseEntity.ok(stories);
        } catch (UserInfoException e) {
            // If there's an issue with finding the user, return a 404 Not Found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        } catch (Exception e) {
            // If there's any other exception, return 500 Internal Server Error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Endpoint for get all stories with their node tree of a user
     */
    @GetMapping("/{userId}/nodetree")
    public ResponseEntity<List<StoryDTO>> getStoriesWithNodeHierarchyForUser(@PathVariable String userId) {
        try {
            List<StoryDTO> stories = storyService.getStoriesWithNodeHierarchyForUser(userId);
            return stories.isEmpty()
                    ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                    : ResponseEntity.ok(stories);
        } catch (UserInfoException e) {
            // If there's an issue with finding the user, return a 404 Not Found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        } catch (Exception e) {
            // If there's any other exception, return 500 Internal Server Error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Data
    public static class StoryInfo {
        public String email;
        public String storyname;
        public String genre;
        public String description;
    }

}