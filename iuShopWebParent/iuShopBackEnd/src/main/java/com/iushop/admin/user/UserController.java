package com.iushop.admin.user;

import com.iushop.admin.FileUploadUtil;
import com.iushop.common.entity.Role;
import com.iushop.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
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
        model.addAttribute("pageTitle", "Create New user");
        return "user-form";
    }

    @PostMapping("/users/save")
    public String saveUser(User theUser, RedirectAttributes redirectAttributes,
                           @RequestParam("user-image") MultipartFile multipartFile) throws IOException {

        if(!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            theUser.setPhotos(fileName);
            User savedUser = userService.saveUserToDb(theUser);
            String uploadDir = "user-photos/" + savedUser.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
            redirectAttributes.addFlashAttribute("message_saved", "The user has been saved successfully");
        } else {
            if (theUser.getPhotos().isEmpty())
                theUser.setPhotos(null);
            userService.saveUserToDb(theUser);
        }
        return "redirect:/users";
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable(value = "id") int uid, Model model,
                           RedirectAttributes redirectAttributes){
        User theUser = null;
        try {
            theUser = userService.getUserById(uid);
            List<Role> listRoles = userService.listAllRoles();
            model.addAttribute("pageTitle", "Edit user id_"+uid);
            model.addAttribute("user", theUser);
            model.addAttribute("listRoles", listRoles);
            return "user-form";
        } catch (UserNotFoundException e) {
           redirectAttributes.addFlashAttribute("message_edit", e.getMessage());
           return "redirect:/users";
        }
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable(value = "id") Integer uid,
                             Model model,
                             RedirectAttributes redirectAttributes){
        try {
            userService.deleteUser(uid);
            redirectAttributes.addFlashAttribute("message_edit", "The User ID "+ uid + " has been deleted successfully");
        }
        catch (UserNotFoundException e) {
            redirectAttributes.addFlashAttribute("message_edit", e.getMessage());
        }
        return "redirect:/users";
    }

    @GetMapping("/users/{id}/enabled/{status}")
    public String updateEnabledStatus(@PathVariable(value = "id") Integer uid,
                                      @PathVariable(value="status") boolean status,
                                      RedirectAttributes redirectAttributes){

        userService.updateEnabledStatus(uid, status);
        String isEnabled = status ? "enabled" : "disabled";
        String message = "The userId "+ uid + " is "+ isEnabled + " successfully";
        redirectAttributes.addFlashAttribute("message_enabled" , message);
        return "redirect:/users";
    }


}
