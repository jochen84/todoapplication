package se.ecutb.todoapplication.data;

import org.springframework.data.jpa.repository.JpaRepository;
import se.ecutb.todoapplication.entity.AppUser;
import se.ecutb.todoapplication.entity.TodoItem;

import java.util.Optional;

public interface TodoItemRepo extends JpaRepository<TodoItem, Integer> {

    @Override
    Optional<TodoItem> findById(Integer integer);

    Optional<TodoItem> findByAssignee(AppUser appUser);

    Optional<TodoItem> findByTitle(String todoTitle);
}
