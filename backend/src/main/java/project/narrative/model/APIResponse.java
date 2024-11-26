package project.narrative.model;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class APIResponse {
    private List<Candidate> candidates;
    private UsageMetadata usageMetadata;
    @Data
    public static class Candidate {
        private Content content;
        private String finishReason;
        private int index;
        private List<SafetyRating> safetyRatings;

        @Data
        public static class Content {
            private List<Part> parts;

            @Data
            public static class Part {
                private String text;
            }
        }

        @Data
        public static class SafetyRating {
            private String category;
            private String probability;
        }
    }

    @Data
    public static class UsageMetadata {
        private int promptTokenCount;
        private int candidatesTokenCount;
        private int totalTokenCount;

    }
}