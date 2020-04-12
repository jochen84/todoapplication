package se.ecutb.todoapplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import se.ecutb.todoapplication.data.AppUserRoleRepo;
import se.ecutb.todoapplication.data.TodoItemRepo;
import se.ecutb.todoapplication.dto.AppUserFormDto;
import se.ecutb.todoapplication.dto.UpdateAppUserFormDto;
import se.ecutb.todoapplication.entity.AppUser;
import se.ecutb.todoapplication.entity.AppUserRole;
import se.ecutb.todoapplication.entity.Role;
import se.ecutb.todoapplication.entity.TodoItem;
import se.ecutb.todoapplication.service.AppUserService;

import javax.validation.Valid;
import java.util.List;

@Controller
public class AppUserController {

    private AppUserService appUserService;
    private AppUserRoleRepo appUserRoleRepo;
    private TodoItemRepo todoItemRepo;

    @Autowired
    public AppUserController(AppUserService appUserService, AppUserRoleRepo appUserRoleRepo, TodoItemRepo todoItemRepo) {
        this.appUserService = appUserService;
        this.appUserRoleRepo = appUserRoleRepo;
        this.todoItemRepo = todoItemRepo;
    }

    @GetMapping("/register/form")
    public String getRegistrationForm(Model model){
        model.addAttribute("form", new AppUserFormDto());
        return "registration-form";
    }

    @PostMapping("/register/process")
    public String processRegistration(@Valid @ModelAttribute(name = "form") AppUserFormDto userFormDto, BindingResult bindingResult){
        if(appUserService.findByUserName(userFormDto.getUserName()).isPresent()){
            FieldError userNameError = new FieldError("form", "userName", "Username " + userFormDto.getUserName().toLowerCase() + " already exists!");
            bindingResult.addError(userNameError);
        }
        if (!userFormDto.getPassword().equals(userFormDto.getPasswordConfirm())){
            FieldError passwordError = new FieldError("form", "passwordConfirm", "Passwords does not match!");
            bindingResult.addError(passwordError);
        }
        if (bindingResult.hasErrors()){
            return "registration-form";
        }

        appUserService.registerNew(userFormDto);
        AppUser newUser = appUserService.findByUserName(userFormDto.getUserName()).get();
        return "redirect:/login";
    }

    @GetMapping("users/{id}")
    public String getUserView(@PathVariable(name = "id")int id, @AuthenticationPrincipal UserDetails caller, Model model) {
        AppUser appUser = appUserService.findById(id).orElseThrow(IllegalArgumentException::new);
        if (caller == null) return "redirect:/accessdenied";
        if (appUser.getUserName().equals(caller.getUsername()) || caller.getAuthorities().stream().anyMatch(x -> x.getAuthority().equals("ADMIN"))) {
            model.addAttribute("user", appUser);
            return "user-view";
        }else{

            return "redirect:/accessdenied";
        }
    }

    /*
    @GetMapping("users/{id}/delete")
    public String deleteUser(@PathVariable("id")int id){
        AppUser appUser = appUserService.findById(id).orElseThrow(IllegalArgumentException::new);
        appUserService.delete(appUser);

        return "user-list";
    }

     */

    @PostMapping("users/delete")
    public String deleteUser(@RequestParam("userId")int userId, @AuthenticationPrincipal UserDetails caller){
        if (caller == null) return "redirect:/accessdenied";
        if (caller.getAuthorities().stream().anyMatch(x -> x.getAuthority().equals("ADMIN"))){
            AppUser appUser = appUserService.findById(userId).orElseThrow(IllegalArgumentException::new);
            //Få till så den tömmer todolist
            //appUser.getTodoItems().stream().forEach(x -> x.setAssignee(null));        //Denna funkar, men hur använda removeUsersTodo
            List<TodoItem> todos = todoItemRepo.findByAssignee(appUser);
            todos.stream().forEach(x->appUser.removeUsersTodo(x));
                appUserService.delete(appUser);
            return "redirect:/users/userlist";
        }else{
            return "redirect:/accessdenied"; //Eller return delete failed? :)
        }

    }


    @GetMapping("users/{id}/update")
    public String getUserUpdateForm(@PathVariable("id")int id, Model model){
        UpdateAppUserFormDto appUserFormDto = new UpdateAppUserFormDto();
        AppUser appUser = appUserService.findById(id).orElseThrow(IllegalArgumentException::new);
        AppUserRole admin = appUserRoleRepo.findByRole(Role.ADMIN).orElseThrow(IllegalArgumentException::new);
        appUserFormDto.setFirstName(appUser.getFirstName());
        appUserFormDto.setLastName(appUser.getLastName());
        appUserFormDto.setUserId(appUser.getUserId());
        appUserFormDto.setAdmin(appUser.getRoleSet().contains(admin));
        model.addAttribute("form", appUserFormDto);

        return "update-appuser";
    }

    @PostMapping("users/{id}/update/process")
    public String processUpdate(@PathVariable("id")int id, UpdateAppUserFormDto form, BindingResult bindingResult){
        AppUser original = appUserService.findById(id).orElseThrow(IllegalArgumentException::new);
        AppUserRole admin = appUserRoleRepo.findByRole(Role.ADMIN).orElseThrow(IllegalArgumentException::new);
        if (bindingResult.hasErrors()){
            return "update-task";
        }
        original.setFirstName(form.getFirstName());
        original.setLastName(form.getLastName());
        if (form.isAdmin()){
            original.makeAdmin(admin);
        }
        if (!form.isAdmin()){
            original.removeAdmin(admin);
        }
        appUserService.save(original);

        return "redirect:/users/" + original.getUserId();
    }



    @GetMapping("users/userlist")
    public String getUserList(Model model){
        List<AppUser> userList = appUserService.findAll();
        model.addAttribute("users", userList);
        return "user-list";

    }

    @GetMapping("/login")
    public String getLoginForm(){
        return "login-page";
    }
}
