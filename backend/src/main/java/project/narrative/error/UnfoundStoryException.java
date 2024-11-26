package project.narrative.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a user attempts to create account that already exists.
 * 
 * HTTP Status: 409 CONFLICT - Indicates that the request could not
 * be completed due to a conflict with the current state of the resource.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Unfound Story for this user")
public class UnfoundStoryException extends RuntimeException{
    
}
