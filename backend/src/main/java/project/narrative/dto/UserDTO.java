package project.narrative.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import project.narrative.model.entities.User;
import project.narrative.service.UserService;

import java.util.List;
import java.util.Date;

@Getter
@Setter
@JsonInclude(Include.NON_EMPTY)
public class UserDTO {
    private String username;
    private String email;
    private String hashedPassword;
    private String role;
    private Date registrationDate;

    public UserDTO(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.hashedPassword = user.getHashed_pwd();
        this.role = user.getRole();

        // remind others to change to camel case so we can have nice @Getter methods
        this.registrationDate = user.getRegistration_date();
    }

}