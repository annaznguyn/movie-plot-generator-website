package project.narrative.service;

import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import project.narrative.error.UserInfoException;

import project.narrative.model.entities.User;
import project.narrative.model.entities.Story;
import project.narrative.repository.UserRepo;
import project.narrative.repository.StoryRepo;
import project.narrative.dto.UserDTO;
import project.narrative.dto.StoryDTO;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Random;
import java.time.LocalDate;

@Service
public class DashboardService {
    // Repository for interacting with the User entity in the database
    private final UserRepo userRepo;
    private final StoryRepo storyRepo;
    private List<String> images;

    // Constructor to inject the UserRepo dependency
    public DashboardService(UserRepo userRepo, StoryRepo storyRepo) {
        this.userRepo = userRepo;
        this.storyRepo = storyRepo;
        this.images = new ArrayList<>(Arrays.asList("https://pics.craiyon.com/2023-06-27/a48bbcad80454e8e8a2822399ce1cf93.webp", "https://pics.craiyon.com/2024-09-06/s-dOEP-bRYu2QaFdVkWFoQ.webp", "https://pics.craiyon.com/2024-09-05/Pm47JdG7TjGT9xJNbMowrw.webp", "https://pics.craiyon.com/2024-09-01/h0s3iCU1Rxikh4fFv9IVlw.webp", "https://pics.craiyon.com/2024-09-02/J70qAnKPTHKDyQs6_5NSxA.webp", "https://pics.craiyon.com/2024-10-23/rmHYVjc1TAylqjz3GJxRTQ.webp", "https://pics.craiyon.com/2024-10-23/M2XiCDyNQ7Cb5RGwTjzSdA.webp"));
    }

    /** 
     * Find all registered users and their details.
     * @return a list of user DTOs.
    */
    @Transactional
    public List<UserDTO> getAllUsers() {
        // findAll() automatically returns an empty list if there is no user
        List<User> users = userRepo.findAll();
        List<UserDTO> userdtos = new ArrayList<>();
        for (User u : users) {
            userdtos.add(new UserDTO(u));
        }
        return userdtos;
    }

    /** 
     * Find stories of a specific user.
     * @return a list of story DTOs of the user.
    */
    @Transactional
    public List<StoryDTO> getStoriesByUserid(String username) {
        // Return the stories of a specific user if there is any
        User user = userRepo.findById(username).orElseThrow(UserInfoException::new);
        List<Story> stories = storyRepo.findByUserUsername(user.getUsername()).orElse(Collections.emptyList());
        List<StoryDTO> storydtos = new ArrayList<>();
        Random random = new Random();
        for (Story s : stories) {
            StoryDTO storydto = new StoryDTO(s);
            storydto.setImage(images.get(random.nextInt(images.size())));
            storydtos.add(storydto);
        }
        return storydtos;
    }

    /** 
     * Find all stories by username and creationDate.
     * @param username given username.
     * @param creationDate given date the story is created.
     * @return a list of story DTOs with the username and the creationDate.
    */
    @Transactional
    public List<StoryDTO> getStoriesByUsernameAndCreationDate(String username, LocalDate creationDate) {
        System.out.println(creationDate);
        List<Story> stories = storyRepo.findByUserUsernameAndCreationDate(username, creationDate).orElse(Collections.emptyList());
        List<StoryDTO> storydtos = new ArrayList<>();
        for (Story s : stories) {
            storydtos.add(new StoryDTO(s));
        }
        return storydtos;
    }

    /** 
     * Find all stories by username and lastUpdate.
     * @param username given username.
     * @param lastUpdate given last updated date the story is created.
     * @return a list of story DTOs with the username and the lastUpdate.
    */
    @Transactional
    public List<StoryDTO> getStoriesByUsernameAndLastUpdate(String username, LocalDate lastUpdate) {
        List<Story> stories = storyRepo.findByUserUsernameAndLastUpdate(username, lastUpdate).orElse(Collections.emptyList());
        List<StoryDTO> storydtos = new ArrayList<>();
        for (Story s : stories) {
            storydtos.add(new StoryDTO(s));
        }
        return storydtos;
    }

    /** 
     * Find all stories by username and storyname.
     * @param username given username.
     * @param storyname given name of the story.
     * @return a list of story DTOs with the username and the storyname as stories' names can be the same.
    */
    @Transactional
    public List<StoryDTO> getStoriesByUsernameAndStoryname(String username, String storyname) {
        List<Story> stories = storyRepo.findByUserUsernameAndStoryname(username, storyname).orElse(Collections.emptyList());
        List<StoryDTO> storydtos = new ArrayList<>();
        for (Story s : stories) {
            storydtos.add(new StoryDTO(s));
        }
        return storydtos;
    }

    /** 
     * Find all stories by username and genre.
     * @param username username.
     * @param genre genre of the stories.
     * @return a list of story DTOs with the username and the genre.
    */
    @Transactional
    public List<StoryDTO> getStoriesByUsernameAndGenre(String username, String genre) {
        List<Story> stories = storyRepo.findByUserUsernameAndGenre(username, genre).orElse(Collections.emptyList());
        List<StoryDTO> storydtos = new ArrayList<>();
        for (Story s : stories) {
            storydtos.add(new StoryDTO(s));
        }
        return storydtos;
    }
}