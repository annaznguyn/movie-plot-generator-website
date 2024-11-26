package project.narrative.controller;

import lombok.Data;
import project.narrative.service.CharacterService;
import project.narrative.service.DashboardService;
import project.narrative.service.GeminiService;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;

import project.narrative.model.entities.Character;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Handle requests related to dashboard
 */
@RestController
@RequestMapping("stories/{storyId}/characters")
public class CharacterController {

    private final CharacterService characterService;
    private final GeminiService gemini;

    private String key;

    public CharacterController(CharacterService characterService, GeminiService gemini,
            @Value("${API_KEY}") String key) {
        this.characterService = characterService;
        this.gemini = gemini;
        this.key = key;
    }

    @GetMapping("/{characterId}")
    public ResponseEntity<?> get(@PathVariable("characterId") Long id) {
        // TODO: process POST request

        return ResponseEntity.ok(characterService.get(id));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@PathVariable("storyId") Long storyId) {
        // TODO: process POST request

        return ResponseEntity.ok(characterService.getAll(storyId));
    }

    @PostMapping
    public ResponseEntity<?> create(@PathVariable("storyId") Long storyId) {
        // TODO: process POST request
        return ResponseEntity.ok(characterService.create(storyId).getCharacter_id());

    }

    @PutMapping("/{characterId}")
    public ResponseEntity<?> save(@RequestBody CharacterInfo characterInfo, @PathVariable("storyId") Long storyId,
            @PathVariable("characterId") Long characterId) {
        // TODO: process POST request
        return ResponseEntity.ok(characterService.save(characterInfo.lastName, characterInfo.firstName,
                characterInfo.background, characterInfo.context, storyId, characterId));
    }

    @DeleteMapping("{characterId}")
    public ResponseEntity<?> delete(@PathVariable("characterId") Long id) {
        // TODO: process POST request
        characterService.delete(id);
        return ResponseEntity.ok(id);
    }

    @GetMapping("/generation")
    public ResponseEntity<?> getMethodName(@RequestBody CharacterInfo characterInfo,
            @PathVariable("storyId") Long storyId) {
        return ResponseEntity.ok(gemini.characterGenerated(this.key, characterInfo.firstName, characterInfo.lastName,
                characterInfo.context, storyId));
    }

    /**
     * InnerCharacterController
     */
    @Data
    public static class CharacterInfo {
        private String lastName;
        private String firstName;
        private String background;
        private String context;
    }
}
