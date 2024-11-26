package project.narrative.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a user attempts to edit a non-leaf node.
 * 
 * HTTP Status: 409 CONFLICT - Indicates that the request could not
 * be completed due to a conflict with the current state of the resource.
 */
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Cannot edit non-leaf node.")
public class EditNonLeafNodeException extends RuntimeException {

}