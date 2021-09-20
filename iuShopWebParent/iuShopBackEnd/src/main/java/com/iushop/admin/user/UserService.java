package com.iushop.admin.user;

import com.iushop.common.entity.Role;
import com.iushop.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> listAllUsers(){
        return (List<User>) userRepo.findAll();
    }

    public List<Role> listAllRoles(){
        return (List<Role>) roleRepo.findAll();
    }

    public void saveUserToDb(User theUser) {
        encodePassword(theUser);
        userRepo.save(theUser);
    }

    private void encodePassword(User user){
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    public boolean isEmailUnique(Integer id,String email){
        User userByEmail = userRepo.getUserByEmail(email);
        if (userByEmail == null) return true;
        if (userByEmail.getId() == id) return true;
        else return false;
    }

    public User getUserById(Integer id) throws UserNotFoundException {
        Optional<User> userById = userRepo.findById(id);
        if (userById.isPresent()) {
            User user = userById.get();
            return user;
        }else {
            throw new UserNotFoundException("Could not find any user with id "+id);
        }
    }

    public void deleteUser (Integer id) throws UserNotFoundException{
        Long countById = userRepo.countById(id);
        if (countById == null | countById == 0){
            throw new UserNotFoundException("Could not find any user with id: "+id +" to delete");
        }else {
            userRepo.deleteById(id);
        }
    }


}
