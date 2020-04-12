package se.ecutb.todoapplication.service;

import se.ecutb.todoapplication.dto.TodoItemFormDto;
import se.ecutb.todoapplication.entity.AppUser;
import se.ecutb.todoapplication.entity.TodoItem;

import java.util.List;
import java.util.Optional;

public interface TodoItemService {

    TodoItem create(TodoItemFormDto itemFormDto);
    TodoItem save(TodoItem todoItem);
    Optional<TodoItem> findById(int todoItemId);
    List<TodoItem> findByAssignee(AppUser appUser);
    Optional<TodoItem> findByTitle(String todoTitle);
    List<TodoItem> findAll();
}
