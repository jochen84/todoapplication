package se.ecutb.todoapplication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.ecutb.todoapplication.data.TodoItemRepo;
import se.ecutb.todoapplication.dto.TodoItemFormDto;
import se.ecutb.todoapplication.entity.AppUser;
import se.ecutb.todoapplication.entity.TodoItem;

import java.util.Optional;

@Service
public class TodoItemServiceImpl implements TodoItemService {

    private TodoItemRepo todoItemRepo;

    @Autowired
    public TodoItemServiceImpl(TodoItemRepo todoItemRepo) {
        this.todoItemRepo = todoItemRepo;
    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public TodoItem create(TodoItemFormDto itemFormDto) {

        TodoItem newItem = new TodoItem(
                itemFormDto.getTitle(),
                itemFormDto.getDescription(),
                itemFormDto.getDeadline(),
                itemFormDto.isDone(),
                itemFormDto.getReward(),
                itemFormDto.getAssignee()
        );

        newItem = todoItemRepo.save(newItem);
        return newItem;
    }

    @Override
    public TodoItem save(TodoItem todoItem) {
        return todoItemRepo.save(todoItem);
    }

    @Override
    public Optional<TodoItem> findById(int todoItemId) {
        return todoItemRepo.findById(todoItemId);
    }

    @Override
    public Optional<TodoItem> findByAssignee(AppUser appUser) {
        return todoItemRepo.findByAssignee(appUser);
    }
}
