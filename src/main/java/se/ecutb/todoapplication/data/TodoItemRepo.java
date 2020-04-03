package se.ecutb.todoapplication.data;

import org.springframework.data.jpa.repository.JpaRepository;
import se.ecutb.todoapplication.entity.TodoItem;

public interface TodoItemRepo extends JpaRepository<TodoItem, Integer> {
}
