package project.narrative.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import project.narrative.dto.CharacterDTO;
import project.narrative.model.APIRequest;
import project.narrative.model.APIResponse;
import project.narrative.model.entities.Story;
import project.narrative.repository.StoryRepo;

import java.util.HashMap;
import java.util.Map;

@Service
public class GeminiService {

    private final RestTemplate template;
    private final StoryRepo storyRepo;

    public GeminiService(RestTemplate template, StoryRepo storyRepo) {
        this.template = template;
        this.storyRepo = storyRepo;
    }

    public String sendAndReceiveRequest(String key, String prompt) {
        String url = "https://generativelanguage.googleapis.com/v1/models/" +
                "gemini-pro:generateContent?key=" + key;

        /*
         * NOTE:
         * - Set the headers in the HTTP request.
         */
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        /*
         * NOTE:
         * - Create the request body of the HTTP request. Structure of the Gemini
         * JSON request is available in the documentation. Check out the the curl
         * command to see what the HTTP request consists of.
         */
        Map<String, Object> body = new HashMap<>();
        Map<String, Object> user = new HashMap<>();
        user.put("role", "user");
        Map<String, String> text = new HashMap<>();
        text.put("text", prompt);
        user.put("parts", new Map[] { text });
        body.put("contents", new Map[] { user });

        /*
         * NOTE:
         * - Create the request entity with headers and body. Just a container for
         * the crap to send to Gemini.
         */
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        /*
         * NOTE:
         * - Make the request to the server and creating a response entity to encap-
         * sulate all the information.
         */
        ResponseEntity<APIResponse> response = template.exchange(
                url,
                HttpMethod.POST,
                request,
                APIResponse.class);

        /*
         * NOTE:
         * - Return the response body.
         */
        return response.getBody().getCandidates().get(0).getContent().getParts().get(0).getText();
    }

    public CharacterDTO characterGenerated(String key, String firstName, String lastName, String context,
            Long storyId) {
        Story story = storyRepo.findById(storyId).orElseThrow(() -> new RuntimeException("Story not found"));
        StringBuilder sb = new StringBuilder();

        // Generate New Result from AI
        sb.append(String.format("""
                Based on the following provided plot, generate a background for %s %s, no more than 50 words.
                Please ensure the new content is creative, introducing and Guarantee the story style is %s.
                Only return the new background content, keeping it concise and coherent.
                ========================================

                    """, firstName, lastName, story.getGenre()));
        String background = sendAndReceiveRequest(key, sb.toString());
        // String background = "This is the background";

        CharacterDTO characterDTO = new CharacterDTO();
        characterDTO.setFirstName(firstName);
        characterDTO.setLastName(lastName);
        characterDTO.setBackground(background);
        characterDTO.setContext(context);
        return characterDTO;
    }

}
