package de.unistuttgart.iste.ese.api.DTOs;

import java.util.Date;
import java.util.List;

public class RequestDTO {
    private String title;
    private String description;
    private boolean finished;
    private List<Long> assigneeIdList;
    private Date createdDate;
    private Date dueDate;
    private Date finishedDate;
    private String category;

    public RequestDTO(String title, String description, Boolean finished, List<Long> assigneeIdList, Date createdDate, Date dueDate) {
        this.title = title;
        this.description = description;
        this.finished = finished;
        this.assigneeIdList = assigneeIdList;
        this.createdDate = createdDate;
        this.dueDate = dueDate;
    }

    public RequestDTO() {
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
        }
        this.finished = finished;
    }

    public List<Long> getAssigneeIdList() {
        return assigneeIdList;
    }

    public void setAssigneeIdList(List<Long> assigneeIdList) {
        this.assigneeIdList = assigneeIdList;
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
        return finishedDate;
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
}
