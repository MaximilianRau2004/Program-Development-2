package de.unistuttgart.iste.ese.api.Services;

import de.unistuttgart.iste.ese.api.Models.Assignee;
import de.unistuttgart.iste.ese.api.Models.Todo;
import de.unistuttgart.iste.ese.api.Repositories.AssigneeRepository;
import de.unistuttgart.iste.ese.api.Repositories.TodoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AssigneeService {

    @Autowired
    private AssigneeRepository assigneeRepository;

    @Autowired
    private TodoRepository toDoRepository;

    /**
     * Retrieve all assignees.
     *
     * @return a list of all assignees.
     */
    public List<Assignee> getAssignees() {
        return assigneeRepository.findAll();
    }

    /**
     * Retrieve a single assignee by its ID.
     *
     * @param id the ID of the assignee.
     * @return the assignee with the specified ID.
     * @throws ResponseStatusException if the assignee with the given ID is not found.
     */
    public Assignee getAssignee(long id) {
        Assignee searchedAssignee = assigneeRepository.findById(id);
        if (searchedAssignee != null) {
            return searchedAssignee;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
            String.format("Assignee with ID %s not found!", id));
    }

    /**
     * Create a new assignee.
     *
     * @param requestBody the assignee data from the POST request.
     * @return the created assignee.
     * @throws ResponseStatusException if the provided email is invalid.
     */
    public Assignee createAssignee(@Valid Assignee requestBody) {
        validatePrename(requestBody.getPrename());
        validateName(requestBody.getName());
        validateUniversityEmail(requestBody.getEmail());

        return assigneeRepository.save(requestBody);
    }

    /**
     * Update an existing assignee.
     *
     * @param id the ID of the assignee to update.
     * @param requestBody the updated assignee data from the PUT request.
     * @return the updated assignee.
     * @throws ResponseStatusException if the assignee with the given ID is not found.
     */
    public Assignee updateAssignee(long id, @Valid Assignee requestBody) {
        Assignee assigneeToUpdate = assigneeRepository.findById(id);
        if (assigneeToUpdate == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Assignee with ID %s not found!", id));
        }
        
        assigneeToUpdate.setPrename(requestBody.getPrename());
        assigneeToUpdate.setName(requestBody.getName());
        assigneeToUpdate.setEmail(requestBody.getEmail());
        
        validatePrename(requestBody.getPrename());
        validateName(requestBody.getName());
        validateUniversityEmail(requestBody.getEmail());
        
        return assigneeRepository.save(assigneeToUpdate);
    }

    /**
     * Delete an existing assignee.
     *
     * @param id the ID of the assignee to delete.
     * @throws ResponseStatusException if the assignee with the given ID is not found.
     */
    public void deleteAssignee(long id) {
        Assignee assigneeToDelete = assigneeRepository.findById(id);
        if (assigneeToDelete == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Assignee with ID %s not found!", id));
        }

        List<Todo> associatedToDos = toDoRepository.findAll();
        for (Todo todo : associatedToDos) {
            todo.getAssigneeList().removeIf(assignee -> assigneeToDelete.getId().equals(id));
            toDoRepository.save(todo);
        }

        assigneeRepository.deleteById(id);
    }

    /**
     * Validates an assignee prename.
     *
     * @param prename the prename to validate.
     * @throws ResponseStatusException if the prename is null or empty.
     */
    private void validatePrename(String prename) {
        if (prename == null || prename.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vorname muss angegeben werden");
        }
    }

    /**
     * Validates an assignee name.
     *
     * @param name the name to validate.
     * @throws ResponseStatusException if the name is null or empty.
     */
    private void validateName(String name) {
        if (name == null || name.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nachname muss angegeben werden");
        }
    }

    /**
     * Validates an assignee email.
     *
     * @param email the email to validate.
     * @throws ResponseStatusException if the email is null, empty, or invalid.
     */
    private void validateUniversityEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email muss angegeben werden");
        }
        if (!email.contains("@") ||
            !email.endsWith("uni-stuttgart.de") ||
            email.equals("uni-stuttgart.de")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalide email, muss mit uni-stuttgart.de enden");
        }
    }
}