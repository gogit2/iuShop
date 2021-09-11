package com.iushop.admin.user;

import com.iushop.common.entity.Role;
import com.iushop.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String getAllUsers(Model model){
        List<User> listUsers = userService.listAllUsers();
        model.addAttribute("listAllUsers",listUsers);
        return "users";
    }

    @GetMapping("/users/new")
    public String addNewUser(Model model){
        User theUser = new User();
        List<Role> listRoles = userService.listAllRoles();
        model.addAttribute("user", theUser);
        model.addAttribute("listRoles", listRoles);
        return "user-form";
    }

    @PostMapping("/users/save")
    public String saveUser(User theUser, RedirectAttributes redirectAttributes){
//        User theUser = new User();
        userService.saveUserToDb(theUser);
        redirectAttributes.addFlashAttribute("message", "The user has been saved successfully");
        System.out.println(theUser.toString());
        return "redirect:/users";
    }

}
