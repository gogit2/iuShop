package com.iushop.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("")
    public String main(){
        return "index";
    }

    @GetMapping("/login")
    public String viewLoginPage(){
        return "login";
    }
}
