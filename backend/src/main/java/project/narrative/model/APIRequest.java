package project.narrative.model;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class APIRequest {
    private List<Content> contents;

    @Data
    public static class Content {
        private List<Part> parts;

        @Data
        public static class Part {
            private String text;
        }
    }
}