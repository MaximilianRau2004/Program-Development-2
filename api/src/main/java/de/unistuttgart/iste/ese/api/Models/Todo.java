package de.unistuttgart.iste.ese.api.Models;

import de.unistuttgart.iste.ese.api.DTOs.RequestDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "Titel muss angegeben werden")
    private String title;
    private String description;
    private Boolean finished;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "todo_assignee_list",
        joinColumns = @JoinColumn(name = "todo_id"),
        inverseJoinColumns = @JoinColumn(name = "assignee_id")
    )
    private List<Assignee> assigneeList;
    private Date createdDate;

    @Future(message = "Fälligkeitsdatum muss in der Zukunft liegen")
    @NotNull(message = "Fälligkeitsdatum  muss angegeben werden!")
    private Date dueDate;
    private Date finishedDate;

    @Column(nullable = true)
    private String category;


    public Todo(Long id, String title, String description, Boolean finished, List<Assignee> assigneeList, Date createdDate, Date dueDate, Date finishedDate, String category) {
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

    public Todo(RequestDTO dto, List<Assignee> assigneeList, String category) {
        this.title = dto.getTitle();
        this.description = dto.getDescription();
        this.finished = dto.isFinished();
        this.assigneeList = assigneeList;
        this.createdDate = new Date();
        this.dueDate = dto.getDueDate();
        this.finishedDate = dto.isFinished() ? new Date() : null;
        this.category = category;
    }
    

    public Todo() {
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
            this.setFinishedDate(new Date());
        } else {
            this.setFinishedDate(null);
        }
        this.finished = finished;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Assignee> getAssigneeList() {
        return assigneeList != null ? assigneeList : new ArrayList<>();
    }

    public void setAssigneeList(List<Assignee> assigneeList) {
        this.assigneeList = assigneeList;
    }

    public Date getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(Date createdDate) {}

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
