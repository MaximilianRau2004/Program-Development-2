package de.unistuttgart.iste.ese.api.Repositories;

import de.unistuttgart.iste.ese.api.Models.Assignee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssigneeRepository extends JpaRepository<Assignee, Long> {

    Assignee findById(long id);
}
