package com.iushop.admin.user;

import com.iushop.common.entity.Role;
import com.iushop.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    public List<User> listAllUsers(){
        return (List<User>) userRepo.findAll();
    }

    public List<Role> listAllRoles(){
        return (List<Role>) roleRepo.findAll();
    }

    public void saveUserToDb(User theUser) {
        userRepo.save(theUser);
    }
}
