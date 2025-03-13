package de.unistuttgart.iste.ese.api.DTOs;

import de.unistuttgart.iste.ese.api.Models.Assignee;

import java.util.List;

public class TodoDTO {
    private Long id;
    private String title;
    private String description;
    private boolean finished;
    private List<Assignee> assigneeList;
    private Long createdDate;
    private Long dueDate;
    private String category;

    public TodoDTO(Long id, String title, String description, boolean finished, List<Assignee> assigneeList, Long createdDate, Long dueDate, String category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.finished = finished;
        this.assigneeList = assigneeList;
        this.createdDate = createdDate;
        this.dueDate = dueDate;
        this.category = category;
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
        this.finished = finished;
    }

    public List<Assignee> getAssigneeList() {
        return assigneeList;
    }

    public void setAssigneeList(List<Assignee> assigneeList) {
        this.assigneeList = assigneeList;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public Long getDueDate() {
        return dueDate;
    }

    public void setDueDate(Long dueDate) {
        this.dueDate = dueDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
