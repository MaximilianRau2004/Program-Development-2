package de.unistuttgart.iste.ese.api.Controller;

import de.unistuttgart.iste.ese.api.ApiVersion1;
import de.unistuttgart.iste.ese.api.Models.Assignee;
import de.unistuttgart.iste.ese.api.Services.AssigneeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@ApiVersion1
public class AssigneeController {

    @Autowired
    private AssigneeService assigneeService;

    /**
     * Retrieve all assignees.
     *
     * @return a list of all assignees.
     */
    @GetMapping("/assignees")
    public List<Assignee> getAssignees() {
        return assigneeService.getAssignees();
    }

    /**
     * Retrieve a specific assignee by their ID.
     *
     * @param id the ID of the assignee to retrieve.
     * @return the assignee with the specified ID.
     * @throws ResponseStatusException if the assignee with the given ID is not found.
     */
    @GetMapping("/assignees/{id}")
    public Assignee getAssignee(@PathVariable("id") long id) {
        return assigneeService.getAssignee(id);
    }

    /**
     * Create a new assignee.
     *
     * @param requestBody the data for the new assignee.
     * @return the created assignee.
     * @throws ResponseStatusException if the email provided is not a valid university email.
     */
    @PostMapping("/assignees")
    @ResponseStatus(HttpStatus.CREATED)
    public Assignee createAssignee(@Valid @RequestBody Assignee requestBody) {
        return assigneeService.createAssignee(requestBody);
    }

    /**
     * Update an existing assignee by their ID.
     *
     * @param id the ID of the assignee to update.
     * @param requestBody the updated data for the assignee.
     * @return the updated assignee.
     * @throws ResponseStatusException if the assignee with the given ID is not found.
     */
    @PutMapping("/assignees/{id}")
    public Assignee updateAssignee(@PathVariable("id") long id, @Valid @RequestBody Assignee requestBody) {
        return assigneeService.updateAssignee(id, requestBody);
    }

    /**
     * Delete an assignee by their ID.
     *
     * @param id the ID of the assignee to delete.
     * @throws ResponseStatusException if the assignee with the given ID is not found.
     */
    @DeleteMapping("/assignees/{id}")
    public void deleteAssignee(@PathVariable("id") long id) {
        assigneeService.deleteAssignee(id);
    }
}
