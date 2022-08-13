package com.iushop.admin.user;

import com.iushop.admin.FileUploadUtil;
import com.iushop.admin.security.IuShopUserDetails;
import com.iushop.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class AccountController {

    @Autowired
    private UserService userService;

    @GetMapping("/account")
    public String viewDetails(@AuthenticationPrincipal IuShopUserDetails loggedUser, Model model){
        String email = loggedUser.getUsername();
        User theLoggedUser = userService.getByEmail(email);
        model.addAttribute("user", theLoggedUser);

        return "account-form";
    }

    @PostMapping("/account/update")
    public String saveAccountDetails(User theUser, RedirectAttributes redirectAttributes,
                           @AuthenticationPrincipal IuShopUserDetails loggedUser,
                           @RequestParam("user-image") MultipartFile multipartFile) throws IOException {

        if(!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            theUser.setPhotos(fileName);
            User savedUser = userService.updateAccount(theUser);
            String uploadDir = "user-photos/" + savedUser.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            if (theUser.getPhotos().isEmpty()) {
                theUser.setPhotos(null);
            }
            userService.updateAccount(theUser);
        }

        loggedUser.setFirstName(theUser.getFirstName());
        loggedUser.setLastName(theUser.getLastName());

        redirectAttributes.addFlashAttribute("messageSaved", "Your account details have been updated successfully");

        return "redirect:/account";
    }


}
