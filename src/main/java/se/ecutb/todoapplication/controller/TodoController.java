package se.ecutb.todoapplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import se.ecutb.todoapplication.dto.TodoItemFormDto;
import se.ecutb.todoapplication.entity.TodoItem;
import se.ecutb.todoapplication.service.TodoItemService;


import javax.validation.Valid;
import java.util.List;

@Controller
public class TodoController {

    TodoItemService todoItemService;

    @Autowired
    public TodoController(TodoItemService todoItemService) {
        this.todoItemService = todoItemService;
    }

    @GetMapping("todos/create")
    public String createNewTask(Model model){
        model.addAttribute("form", new TodoItemFormDto());
        return "create-task";
    }

    @PostMapping("todos/create/process")
    public String processNewTask(@Valid @ModelAttribute(name = "form") TodoItemFormDto itemFormDto, BindingResult bindingResult){
        if (todoItemService.findByTitle(itemFormDto.getTitle()).isPresent()){
            FieldError error = new FieldError("form", "title", "There is already a task with " + itemFormDto.getTitle()  + " as title");
            bindingResult.addError(error);
        }
        if (bindingResult.hasErrors()) return "create-task";

        todoItemService.create(itemFormDto);
        TodoItem newTodo = todoItemService.findByTitle(itemFormDto.getTitle()).get();
        return "redirect:/todos/"+newTodo.getTodoItemId();
    }

    @GetMapping("todos/todolist")
    public String getTodoList(Model model){
        List<TodoItem> todoItems = todoItemService.findAll();
        model.addAttribute("todolist", todoItems);
        return "task-list";
    }

    @GetMapping("todos/{id}")
    public String getUserView(@PathVariable(name = "id")int id, Model model){
        TodoItem todoItem = todoItemService.findById(id).orElseThrow(IllegalArgumentException::new); //findBy.get() istället?
        model.addAttribute("todo", todoItem);
        return "task-view";
    }
}
