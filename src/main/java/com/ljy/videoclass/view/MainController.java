package com.ljy.videoclass.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("sign-up")
    public String signUp(){
        return "signUp";
    }

    @GetMapping("login")
    public String login(){
        return "login";
    }

    @GetMapping("main")
    public String main(){
        return "main";
    }

    @GetMapping("conference")
    public String conference(){
        return "conference";
    }
}
