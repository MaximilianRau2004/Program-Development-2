package de.unistuttgart.iste.ese.api.Controller;

import de.unistuttgart.iste.ese.api.ApiVersion1;
import de.unistuttgart.iste.ese.api.DTOs.GetTodoDTO;
import de.unistuttgart.iste.ese.api.DTOs.PostTodoDTO;
import de.unistuttgart.iste.ese.api.DTOs.TodoDTO;
import de.unistuttgart.iste.ese.api.Services.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@ApiVersion1
public class TodoController {

    @Autowired
    private TodoService toDoService;

    /**
     * Get a list of all todos.
     *
     * @return a list of all todos as {@link GetTodoDTO}.
     */
    @GetMapping("/todos")
    public List<GetTodoDTO> getTodos() {
        return toDoService.getAllTodos();
    }

    /**
     * Get a specific todo by its ID.
     *
     * @param id the ID of the todo.
     * @return the todo as GetTodoDTO
     */
    @GetMapping("/todos/{id}")
    public GetTodoDTO getTodo(@PathVariable("id") long id) {
        return toDoService.getTodoById(id);
    }

    /**
     * Classify a todo title to predict its category.
     *
     * @param requestBody a map containing the "title" key with the todo title.
     * @return the predicted category of the todo.
     */
    @GetMapping("/classify")
    public String todoModel(@RequestBody Map<String, String> requestBody) {
        return toDoService.classifyTodoTitle(requestBody.get("title"));
    }

    /**
     * Download all todos as a CSV file.
     *
     * @return a CSV file containing all todos.
     */
    @GetMapping("/csv-downloads/todos")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> getTodoCSV() {
        String csvContent = toDoService.generateCSV();
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, "text/csv");
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=todos.csv");

        return new ResponseEntity<>(csvContent, headers, HttpStatus.OK);
    }

    /**
     * Create a new todo.
     *
     * @param requestBody the todo data as PostTodoDTO
     * @return the created todo as TodoDTO
     */
    @PostMapping("/todos")
    @ResponseStatus(HttpStatus.CREATED)
    public TodoDTO createTodo(@RequestBody PostTodoDTO requestBody) {
        return toDoService.createTodo(requestBody);
    }

    /**
     * Update an existing todo by its ID.
     *
     * @param id the ID of the todo to update.
     * @param requestBody the updated todo data as PostTodoDTO
     * @return the updated todo as GetTodoDTO
     */
    @PutMapping("/todos/{id}")
    public GetTodoDTO updateTodo(@PathVariable("id") long id, @RequestBody PostTodoDTO requestBody) {
        return toDoService.updateTodo(id, requestBody);
    }

    /**
     * Delete a specific todo by its ID.
     *
     * @param id the ID of the todo to delete.
     */
    @DeleteMapping("/todos/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTodo(@PathVariable("id") long id) {
        toDoService.deleteTodoById(id);
    }
}

