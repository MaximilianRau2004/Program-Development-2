package de.unistuttgart.iste.ese.api.DTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import de.unistuttgart.iste.ese.api.Models.Assignee;
import de.unistuttgart.iste.ese.api.Models.Todo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ResponseDTO {
    private Long id;
    private String title;
    private String description;
    private boolean finished;
    private List<Assignee> assigneeList;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dueDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date finishedDate;
    private String category;

    public ResponseDTO(Long id, Todo todo) {
        this.id = id;
        this.title = todo.getTitle();
        this.description = todo.getDescription();
        this.finished = todo.isFinished();
        this.assigneeList = todo.getAssigneeList();
        this.createdDate = todo.getCreatedDate() != null ? todo.getCreatedDate() : new Date();
        this.dueDate = todo.getDueDate();
        this.finishedDate = todo.isFinished() ? todo.getFinishedDate() : null;
        this.category = todo.getCategory();
    }

    public ResponseDTO(Todo todo) {
        this.id = todo.getId();
        this.title = todo.getTitle();
        this.description = todo.getDescription();
        this.finished = todo.isFinished();
        this.assigneeList = todo.getAssigneeList();
        this.createdDate = todo.getCreatedDate() != null ? todo.getCreatedDate() : new Date();
        this.dueDate = todo.getDueDate();
        this.finishedDate = todo.isFinished() ? todo.getFinishedDate() : null;

        this.category = todo.getCategory();
    }

    public ResponseDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        if (finished) {
            finishedDate = new Date();
        } else {
            finishedDate = null; 
        }
        this.finished = finished;
    }

    public List<Assignee> getAssigneeList() {
        return assigneeList;
    }

    public void setAssigneeList(List<Assignee> assigneeList) {
        this.assigneeList = assigneeList;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getFinishedDate() {
        return finished ? finishedDate : null; 
    }

    public void setFinishedDate(Date finishedDate) {
        this.finishedDate = finishedDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    private ResponseDTO(Long id, String title, String description, boolean finished, List<Assignee> assigneeList, Date createdDate, Date dueDate, Date finishedDate, String category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.finished = finished;
        this.assigneeList = assigneeList;
        this.createdDate = createdDate;
        this.dueDate = dueDate;
        this.finishedDate = finishedDate;
        this.category = category;
    }
}
