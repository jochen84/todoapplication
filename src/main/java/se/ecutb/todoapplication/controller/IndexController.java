package se.ecutb.todoapplication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("")
    public String home(){
        return "login-page";
    }

    @GetMapping("/accessdenied")
    public String getAccessDenied(){
        return "access-denied";
    }
}
