package project.narrative;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

/*
NOTE:
    - Exclusion for now because it will scream if there is no database that's configured
    for the current project.
 */
@SpringBootApplication
@EnableScheduling
public class NarrativeApplication {
    public static void main(String[] args) {
        // System.out.println("hello");
        SpringApplication.run(NarrativeApplication.class, args);
    }
    
    @Bean
    public RestTemplate template(){
        return new RestTemplate();
    }
}
