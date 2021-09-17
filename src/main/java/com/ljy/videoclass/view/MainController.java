package com.ljy.videoclass.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("main")
    public String main(){
        return "main";
    }

    @GetMapping("my-class")
    public String myClass(){
        return "my-class";
    }

    @GetMapping("my-class-list")
    public String myClassList(){
        return "class-config";
    }

    @GetMapping("update-class")
    public String updateClass(){
        return "update-class";
    }

    @GetMapping("joiner-list")
    public String joinerList(){
        return "joiner-list";
    }

    @GetMapping("add-class")
    public String addClass(){
        return "add-class";
    }

    @GetMapping("join-class")
    public String joinClass(){
        return "join-class";
    }
}
