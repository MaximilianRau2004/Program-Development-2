package de.unistuttgart.iste.ese.api.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "assignees")
public class Assignee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "Vorname muss angegeben werden!")
    @JsonProperty("prename")
    @Column(name = "pre_name")
    private String prename;
    
    @NotNull(message = "Nachname muss angegeben werden!")
    private String name;

    @NotNull(message = "Email muss angegeben werden!")
    @Email
    private String email;

    public Assignee(String prename, String name, String email) {
        this.prename = prename;
        this.name = name;
        this.email = email;
    }

    public Assignee() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrename() {
        return prename;
    }

    public void setPrename(String prename) {
        this.prename = prename;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
