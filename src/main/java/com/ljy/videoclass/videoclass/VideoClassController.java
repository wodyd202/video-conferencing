package com.ljy.videoclass.videoclass;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VideoClassController {
    @GetMapping("index")
    public String index(){
        return "test";
    }
}
