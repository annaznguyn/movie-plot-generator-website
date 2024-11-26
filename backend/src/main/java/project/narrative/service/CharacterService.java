package project.narrative.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import project.narrative.repository.CharacterRepo;
import project.narrative.repository.StoryRepo;
import project.narrative.model.entities.Character;
import project.narrative.model.entities.Story;
import project.narrative.dto.CharacterDTO;
import java.util.List;
import java.util.Optional;

@Service
public class CharacterService {

    private final CharacterRepo characterRepo;
    private final StoryRepo storyRepo;
    private final GeminiService geminiService;

    public CharacterService(CharacterRepo characterRepo, StoryRepo storyRepo, GeminiService geminiService) {
        this.characterRepo = characterRepo;
        this.storyRepo = storyRepo;
        this.geminiService = geminiService;
    }

    @Transactional
    public CharacterDTO create(Long StoryId) {
        Character character = new Character();
        Story story = storyRepo.findById(StoryId).orElseThrow(() -> new RuntimeException("Story not found"));
        character.setStory(story);
        characterRepo.save(character);
        return new CharacterDTO(character);
    }

    // Can exist duplicated character with first name
    @Transactional
    public CharacterDTO save(String fname, String lname, String background, String Context, Long StoryId,
            Long characterId) {
        // System.out.println(StoryId);
        Story story = storyRepo.findById(StoryId).orElseThrow(() -> new RuntimeException("Story not found"));
        Optional<Character> characterChecker = characterRepo.findByFirstNameAndStory(fname, story);
        if (characterChecker.isPresent() && characterChecker.get().getId() != characterId) {
            throw new RuntimeException("Character already exists");
        } else {

        }
        Character character = characterRepo.findById(characterId)
                .orElseThrow(() -> new RuntimeException("Character not found"));
        character.setFirstName(fname);
        character.setLastName(lname);
        character.setBackground(background);
        character.setContext(Context);
        character.setStory(story);
        characterRepo.save(character);
        return new CharacterDTO(character);
    }

    @Transactional
    public CharacterDTO get(Long id) {
        Character character = characterRepo.findById(id).orElseThrow(() -> new RuntimeException("Character not found"));
        return new CharacterDTO(character);
    }

    @Transactional
    public void delete(Long id) {
        characterRepo.deleteById(id);
    }

    @Transactional
    public List<CharacterDTO> getAll(Long storyId) {
        Story story = storyRepo.findById(storyId).orElseThrow(() -> new RuntimeException("Story not found"));
        List<CharacterDTO> characterDTO = characterRepo.findByStory(story).stream().map(CharacterDTO::new).toList();
        return characterDTO;
    }

}
