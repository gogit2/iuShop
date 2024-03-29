package com.iushop.admin.user.controller;

import com.iushop.admin.FileUploadUtil;
import com.iushop.admin.user.UserNotFoundException;
import com.iushop.admin.user.UserService;
import com.iushop.admin.user.export.UserCsvExporter;
import com.iushop.admin.user.export.UserExcelExporter;
import com.iushop.admin.user.export.UserPdfExporter;
import com.iushop.common.entity.Role;
import com.iushop.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String listFirstPage(Model model){
        return listByPage(1, model, "id", "asc", null);
    }

    @GetMapping("/users/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum") int pageNum ,Model model,
                             @Param("sortField") String sortField, @Param("sortDir") String sortDir,
                             @Param("keyword") String keyword){
        Page<User> page = userService.listByPage(pageNum, sortField, sortDir, keyword);
        List<User> listUsers = page.getContent();

        long startCount = ((pageNum - 1) * userService.USERS_BER_PAGE) + 1;
        long endCount = startCount + userService.USERS_BER_PAGE - 1;
        if (endCount > page.getTotalElements())
            endCount  = page.getTotalElements();
        int totalPages = page.getTotalPages();
        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc" ;

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listAllUsers", listUsers);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("keyword", keyword);
        return "users/users";
    }

    @GetMapping("/users/new")
    public String addNewUser(Model model){
        User theUser = new User();
        List<Role> listRoles = userService.listAllRoles();
        model.addAttribute("user", theUser);
        model.addAttribute("listRoles", listRoles);
        model.addAttribute("pageTitle", "Create New user");
        return "users/user-form";
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
        return getRedirectURLtoAffectedUser(theUser);
    }

    public String getRedirectURLtoAffectedUser(User user){
        String firstPartOfUniqueEmail = user.getEmail().split("@")[0];
        return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword="+firstPartOfUniqueEmail;
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
            return "users/user-form";
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

    @GetMapping("/users/export/csv")
    public void exportToCsv (HttpServletResponse response) throws IOException {
        List<User> listUsers = userService.listAllUsers();
        UserCsvExporter exporter = new UserCsvExporter();
        exporter.export(listUsers, response);
    }

    @GetMapping("/users/export/excel")
    public void exportToExcel (HttpServletResponse response) throws IOException {
        List<User> listUsers = userService.listAllUsers();
        UserExcelExporter exporter = new UserExcelExporter();
        exporter.export(listUsers, response);
    }

    @GetMapping("/users/export/pdf")
    public void exportToPdf (HttpServletResponse response) throws IOException {
        List<User> listUsers = userService.listAllUsers();
        UserPdfExporter exporter = new UserPdfExporter();
        exporter.export(listUsers, response);
    }

}
