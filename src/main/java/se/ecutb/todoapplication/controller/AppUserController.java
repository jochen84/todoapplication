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
import se.ecutb.todoapplication.dto.AppUserFormDto;
import se.ecutb.todoapplication.entity.AppUser;
import se.ecutb.todoapplication.service.AppUserService;

import javax.validation.Valid;
import java.util.List;

@Controller
public class AppUserController {

    private AppUserService appUserService;

    @Autowired
    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
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
        return "redirect:/login";                               //Denna skall användas när det är klart, kommer till Login sidan när man skapat en ny användare!
        //return "redirect:/users/"+newUser.getUserId();        //Använder denna när jag testar utan Login o Security
    }

    @GetMapping("users/{id}")
    public String getUserView(@PathVariable(name = "id")int id, Model model){
        AppUser appUser = appUserService.findById(id).orElseThrow(IllegalArgumentException::new); //findBy.get() istället?
        model.addAttribute("user", appUser);
        return "user-view";
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
