package se.ecutb.todoapplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import se.ecutb.todoapplication.dto.AppUserFormDto;
import se.ecutb.todoapplication.service.AppUserService;

import javax.validation.Valid;
import java.lang.reflect.Field;

@Controller
public class AppUserController {

    private AppUserService appUserService;

    @Autowired
    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("/register")
    public String getRegistrationForm(Model model){
        model.addAttribute("form", new AppUserFormDto());
        return "registration-form";
    }

    @PostMapping("/register")
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
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String getLoginForm(){
        return "login-page";
    }
}
