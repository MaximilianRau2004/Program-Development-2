package de.unistuttgart.iste.ese.api.DTOs;

import java.util.Date;
import java.util.List;

public class PostTodoDTO {
    private String title;
    private String description;
    private boolean finished;
    private List<Long> assigneeIdList;
    private Long createdDate;
    private Long dueDate;
    private Long finishedDate;
    private String category;

    public PostTodoDTO(String title, String description, Boolean finished, List<Long> assigneeIdList, Long createdDate, Long dueDate) {
        this.title = title;
        this.description = description;
        this.finished = finished;
        this.assigneeIdList = assigneeIdList;
        this.createdDate = createdDate;
        this.dueDate = dueDate;
    }

    public PostTodoDTO() {
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
            finishedDate = new Date().getTime();
        }
        this.finished = finished;
    }

    public List<Long> getAssigneeIdList() {
        return assigneeIdList;
    }

    public void setAssigneeIdList(List<Long> assigneeIdList) {
        this.assigneeIdList = assigneeIdList;
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

    public Long getFinishedDate() {
        return finishedDate;
    }

    public void setFinishedDate(Long finishedDate) {
        this.finishedDate = finishedDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
