package de.unistuttgart.iste.ese.api.Repositories;

import de.unistuttgart.iste.ese.api.Models.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
}
