package com.iushop.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("")
    public String main(){
        return "index";
    }

//    @GetMapping("/h")
//    public String h2(){
//        return "templates/index.html";
//    }
}
