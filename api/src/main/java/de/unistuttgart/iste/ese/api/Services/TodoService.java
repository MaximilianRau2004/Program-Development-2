package de.unistuttgart.iste.ese.api.Services;

import de.unistuttgart.iste.ese.api.DTOs.GetTodoDTO;
import de.unistuttgart.iste.ese.api.DTOs.PostTodoDTO;
import de.unistuttgart.iste.ese.api.DTOs.TodoDTO;
import de.unistuttgart.iste.ese.api.Models.Todo;
import de.unistuttgart.iste.ese.api.Models.Assignee;
import de.unistuttgart.iste.ese.api.Models.TodoModel;
import de.unistuttgart.iste.ese.api.Repositories.TodoRepository;
import de.unistuttgart.iste.ese.api.Repositories.AssigneeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for managing TODOs.
 * Provides CRUD operations, classification, and CSV generation utilities.
 */
@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private AssigneeRepository assigneeRepository;

    private final TodoModel todoModel = new TodoModel("model.pmml");
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Retrieves all Todos
     *
     * @return a list of all TODOs as GetTodoDTO
     */
    public List<GetTodoDTO> getAllTodos() {
        return ((List<Todo>) todoRepository.findAll()).stream()
            .map(this::convertToGetTodoDTO)
            .collect(Collectors.toList());
    }

    /**
     * Retrieves a Todo by its ID.
     *
     * @param id the ID of the Todo to retrieve.
     * @return the requested Todo as GetTodoDTO
     * @throws ResponseStatusException if the Todo with the given ID does not exist.
     */
    public GetTodoDTO getTodoById(long id) {
        Todo todo = findTodoById(id);
        return convertToGetTodoDTO(todo);
    }

    /**
     * Classifies a Todo title to determine its category.
     *
     * @param todoTitle the title of the Todo to classify.
     * @return the predicted category for the Todo title.
     * @throws ResponseStatusException if the title is invalid or classification fails.
     */
    public String classifyTodoTitle(String todoTitle) {
        validateTitle(todoTitle);
        try {
            return todoModel.predictClass(todoTitle);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Classification failed", e);
        }
    }

    /**
     * Generates a CSV representation of all Todos
     *
     * @return a CSV string containing all Todos
     */
    public String generateCSV() {
        StringBuilder csvContent = new StringBuilder("id,title,description,finished,assignees,createdDate,dueDate,finishedDate,category\n");
        for (Todo todo : (List<Todo>) todoRepository.findAll()) {
            csvContent.append(formatTodoToCSV(todo));
        }
        return csvContent.toString();
    }

    /**
     * Creates a new Todo
     *
     * @param requestBody the data to create the Todo from.
     * @return the created Todo as TodoDTO
     * @throws ResponseStatusException if the title is invalid or assignees cannot be found.
     */
    public TodoDTO createTodo(PostTodoDTO requestBody) {
        validateTitle(requestBody.getTitle());
        validateDueDate(requestBody.getDueDate());
        List<Assignee> assignees = getAssignees(requestBody.getAssigneeIdList());
        String category = todoModel.predictClass(requestBody.getTitle());

        Todo toDo = new Todo(
            requestBody.getTitle(),
            requestBody.getDescription(),
            requestBody.isFinished(),
            assignees,
            new Date(),
            requestBody.getDueDate() != null ? new Date(requestBody.getDueDate()) : null,
            null,
            category
        );

        todoRepository.save(toDo);
        return convertToTodoDTO(toDo);
    }

    /**
     * Updates an existing Todo by its ID.
     *
     * @param id the ID of the Todo to update.
     * @param requestBody the new data for the Todo
     * @return the updated Todo as GetTodoDTO
     * @throws ResponseStatusException if the Todo does not exist or the input data is invalid.
     */
    public GetTodoDTO updateTodo(long id, PostTodoDTO requestBody) {
        Todo existingTodo = findTodoById(id);
        
        List<Assignee> assignees = getAssignees(requestBody.getAssigneeIdList());
        String category = todoModel.predictClass(requestBody.getTitle());

        existingTodo.setTitle(requestBody.getTitle());
        existingTodo.setDescription(requestBody.getDescription());
        existingTodo.setAssigneeList(assignees);
        existingTodo.setFinished(requestBody.isFinished());
        existingTodo.setFinishedDate(requestBody.isFinished() ? new Date() : null);
        existingTodo.setCategory(category);
        existingTodo.setDueDate(requestBody.getDueDate() != null ? new Date(requestBody.getDueDate()) : null);

        validateTitle(requestBody.getTitle());
        validateDueDate(requestBody.getDueDate());
        
        todoRepository.save(existingTodo);
        return convertToGetTodoDTO(existingTodo);
    }

    /**
     * Deletes a Todo by its ID.
     *
     * @param id the ID of the Todo to delete.
     * @throws ResponseStatusException if the Todo does not exist.
     */
    public void deleteTodoById(long id) {
        Todo toDoToDelete = findTodoById(id);
        todoRepository.deleteById(id);
    }

    /**
     * Finds a Todo by its ID.
     *
     * @param id the ID of the Todo to find.
     * @return the Todo entity.
     * @throws ResponseStatusException if the Todo with the given ID does not exist.
     */
    private Todo findTodoById(long id) {
        Todo todo = todoRepository.findById(id);
        if (todo == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ToDo nicht gefunden");
        }
        return todo;
    }

    /**
     * Converts a Todo entity to a GetTodoDTO
     *
     * @param todo the Todo entity.
     * @return the Todo as GetTodoDTO
     */
    private GetTodoDTO convertToGetTodoDTO(Todo todo) {
        return new GetTodoDTO(
            todo.getId(),
            todo.getTitle(),
            todo.getDescription(),
            todo.isFinished(),
            new ArrayList<>(todo.getAssigneeList()),
            todo.getCreatedDate().getTime(),
            todo.getDueDate() != null ? todo.getDueDate().getTime() : null,
            todo.getFinishedDate() != null ? todo.getFinishedDate().getTime() : null,
            todo.getCategory()
        );
    }

    /**
     * Converts a Todo entity to a TodoDTO
     *
     * @param todo the Todo entity.
     * @return the Todo as TodoDTO
     */
    private TodoDTO convertToTodoDTO(Todo todo) {
        return new TodoDTO(
            todo.getId(),
            todo.getTitle(),
            todo.getDescription(),
            todo.isFinished(),
            todo.getAssigneeList(),
            todo.getCreatedDate().getTime(),
            todo.getDueDate() != null ? todo.getDueDate().getTime() : null,
            todo.getCategory()
        );
    }

    /**
     * Retrieves a list of Assignees based on their Ids.
     *
     * @param assigneeIds the list of assignee IDs.
     * @return a list of Assignees.
     * @throws ResponseStatusException if duplicates are found or an ID is invalid.
     */
    private List<Assignee> getAssignees(List<Long> assigneeIds) {
        if (assigneeIds == null) return Collections.emptyList();
        Set<Long> uniqueIds = new HashSet<>(assigneeIds);
        if (uniqueIds.size() < assigneeIds.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Doppelte Assignee Ids sind nicht erlaubt");
        }
        return assigneeIds.stream()
            .map(id -> assigneeRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "Assignee nnicht gefunden")))
            .collect(Collectors.toList());
    }

    /**
     * Validates a Todo title.
     *
     * @param title the title to validate.
     * @throws ResponseStatusException if the title is null or empty.
     */
    private void validateTitle(String title) {
        if (title == null || title.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Titel muss angegeben werden");
        }
    }

    /**
     * Validates a Todo due date.
     *
     * @param dueDate the dueDate to validate.
     * @throws ResponseStatusException if the dueDate is null or not in the future.
     */
    private void validateDueDate(Long dueDate) {
        if (dueDate == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fälligkeitsdatum muss angegeben werden");
        }

        long currentTimestamp = System.currentTimeMillis();
        if (dueDate <= currentTimestamp) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fälligkeitsdatum muss in der Zukunft liegen");
        }
    }

    /**
     * Formats a Todo entity into a CSV string.
     *
     * @param todo the Todo entity.
     * @return the CSV representation of the Todo
     */
    private String formatTodoToCSV(Todo todo) {
        String assignees = todo.getAssigneeList().stream()
            .map(a -> a.getPrename() + " " + a.getName())
            .collect(Collectors.joining("+"));

        return String.join(",",
            String.valueOf(todo.getId()),
            String.valueOf(todo.getTitle()),
            escapeCSV(todo.getDescription()),
            String.valueOf(todo.isFinished()),
            escapeCSV(assignees),
            dateFormat.format(todo.getCreatedDate()),
            todo.getDueDate() != null ? dateFormat.format(todo.getDueDate()) : "",
            todo.getFinishedDate() != null ? dateFormat.format(todo.getFinishedDate()) : "",
            escapeCSV(todo.getCategory())) + "\n";
    }

    /**
     * Escapes special characters for CSV formatting.
     *
     * @param field the field to escape.
     * @return the escaped field.
     */
    private String escapeCSV(String field) {
        if (field == null) return "";
        if (field.contains(",") || field.contains("\"")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        return field;
    }
}

