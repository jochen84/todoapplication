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
import se.ecutb.todoapplication.dto.UpdateTodoItemFormDto;
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

    @GetMapping("todos/{id}/update")
    public String getUpdateForm(@PathVariable("id") int id, Model model){
        UpdateTodoItemFormDto todoItemForm = new UpdateTodoItemFormDto();
        TodoItem todoItem = todoItemService.findById(id).orElseThrow(IllegalArgumentException::new);
        todoItemForm.setTitle(todoItem.getTitle());
        todoItemForm.setDescription(todoItem.getDescription());
        todoItemForm.setDeadline(todoItem.getDeadline());
        todoItemForm.setReward(todoItem.getReward());
        todoItemForm.setAssignee(todoItem.getAssignee());
        todoItemForm.setDone(todoItem.isDone());
        model.addAttribute("form", todoItemForm);

        return "update-form";
    }

    @PostMapping("todos/{id}/update/process")
    public String processUpdate(
            @PathVariable("id") int id,
            @Valid @ModelAttribute("form") UpdateTodoItemFormDto form,
            BindingResult bindingResult
    ){
        TodoItem original = todoItemService.findById(id).orElseThrow(IllegalArgumentException::new);

        if (todoItemService.findByTitle(form.getTitle()).isPresent() && !form.getTitle().equals(original.getTitle())){
            FieldError error = new FieldError("form", "title", "Title is already in use");
            bindingResult.addError(error);
        }
        if (bindingResult.hasErrors()){
            return "update-form";
        }

        original.setTitle(form.getTitle());
        original.setDescription(form.getDescription());
        original.setDeadline(form.getDeadline());
        original.setReward(form.getReward());
        original.setAssignee(form.getAssignee());
        original.setDone(form.isDone());

        todoItemService.save(original);

        System.err.println(id);
        System.err.println(form.getTitle());
        System.err.println(form.getDescription());
        System.err.println(form.getDeadline());
        System.err.println(form.getReward());
        System.err.println(form.getAssignee());
        System.err.println(form.isDone());

        return "redirect:/todos/" + original.getTodoItemId();
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
