package project.narrative.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import project.narrative.service.DashboardService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import lombok.Data;

import java.util.List;
import java.util.Date;
import java.time.LocalDate;

/**
 * Handle requests related to dashboard
 */
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    /**
     * Endpoint for retrieving all stories of a specific user.
     * @param allStoriesParam contains username.
     * @return a list of stories.
     */
    @PostMapping("recents")
    public ResponseEntity<?> getAllStories(@RequestBody AllStoriesParam allStoriesParam) {
        return ResponseEntity
                .ok(dashboardService.getStoriesByUserid(allStoriesParam.username));
    }

    /**
     * Endpoint for retrieving all registered users and their information for admin.
     * @return a list of users.
     */
    @PostMapping("users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity
                .ok(dashboardService.getAllUsers());
    }

    /**
     * Endpoint for retrieving all users by the given creation date. Note: creation date must be in the form "yyyy-mm-dd".
     * @param creationDateFilterParam contains username and creation date.
     * @return a list of stories with the username and the creation date.
     */
    @PostMapping("story-by-username-and-creationdate")
    public ResponseEntity<?> getStoriesByUsernameAndCreationDate(@RequestBody CreationDateFilterParam creationDateFilterParam) {
        return ResponseEntity
                .ok(dashboardService.getStoriesByUsernameAndCreationDate(creationDateFilterParam.username, creationDateFilterParam.creationDate));
    }

    /**
     * Endpoint for retrieving all users by the given last update date. Note: creation date must be in the form "yyyy-mm-dd".
     * @param lastUpdateFilterParam contains username and last update date.
     * @return a list of stories with the username and the last update date.
     */
    @PostMapping("story-by-username-and-lastupdate")
    public ResponseEntity<?> getStoriesByUsernameAndLastUpdate(@RequestBody LastUpdateFilterParam lastUpdateFilterParam) {
        return ResponseEntity
                .ok(dashboardService.getStoriesByUsernameAndLastUpdate(lastUpdateFilterParam.username, lastUpdateFilterParam.lastUpdate));
    }

    /**
     * Endpoint for retrieving all stories by the given username and storyname.
     * @param storynameFilterParam contains username and storyname.
     * @return a list of stories with the username and the storyname.
     */
    @PostMapping("story-by-username-and-storyname")
    public ResponseEntity<?> getStoriesByUsernameAndStoryname(@RequestBody StorynameFilterParam storynameFilterParam) {
        return ResponseEntity
                .ok(dashboardService.getStoriesByUsernameAndStoryname(storynameFilterParam.username, storynameFilterParam.storyname));
    }

    /**
     * Endpoint for retrieving all stories by the given username and genre.
     * @param genreFilterParam contains username and genre.
     * @return a list of stories with the username and the genre.
     */
    @PostMapping("story-by-username-and-genre")
    public ResponseEntity<?> getStoriesByUsernameAndGenre(@RequestBody GenreFilterParam genreFilterParam) {
        return ResponseEntity
                .ok(dashboardService.getStoriesByUsernameAndGenre(genreFilterParam.username, genreFilterParam.genre));
    }

    /**
     * Inner class to hold the parameters in requests.
     */
    @Data
    public static class GenreFilterParam {
        public String username;
        public String genre;
    }

    @Data
    public static class StorynameFilterParam {
        public String username;
        public String storyname;
    }

    @Data
    public static class CreationDateFilterParam {
        public String username;
        public LocalDate creationDate;
    }

    @Data
    public static class LastUpdateFilterParam {
        public String username;
        public LocalDate lastUpdate;
    }

    @Data
    public static class AllStoriesParam {
        public String username;
    }
}