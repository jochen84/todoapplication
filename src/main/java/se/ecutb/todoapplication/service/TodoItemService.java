package se.ecutb.todoapplication.service;

import se.ecutb.todoapplication.dto.TodoItemFormDto;
import se.ecutb.todoapplication.entity.AppUser;
import se.ecutb.todoapplication.entity.TodoItem;

import java.util.Optional;

public interface TodoItemService {

    TodoItem create(TodoItemFormDto itemFormDto);
    TodoItem save(TodoItem todoItem);
    Optional<TodoItem> findById(int todoItemId);
    Optional<TodoItem> findByAssignee(AppUser appUser);

}
