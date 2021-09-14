package com.ljy.videoclass.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("login")
    public String login(){
        return "login";
    }

    @GetMapping("main")
    public String main(){
        return "index";
    }

    @GetMapping("add-class")
    public String addClass(){
        return "add-class";
    }

    @GetMapping("test")
    public String test(){
        return "test";
    }

    @GetMapping("my-class-list")
    public String myClassList(Model model){
        return "class-list";
    }

    @GetMapping("class")
    public String classroom(Model model){
        return "class";
    }
}
