package com.iushop.admin.user;

import com.iushop.common.entity.Role;
import com.iushop.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    public static final int USERS_BER_PAGE = 5;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User getByEmail(String email){
        return userRepo.getUserByEmail(email);
    }

    public List<User> listAllUsers(){
        return (List<User>) userRepo.findAll();
    }

    public List<Role> listAllRoles(){
        return (List<Role>) roleRepo.findAll();
    }

    public Page<User> listByPage (int pageNum, String sortFiled, String sortDir, String keyword){

        Sort sort = Sort.by(sortFiled);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNum - 1 , USERS_BER_PAGE, sort);

        if (keyword != null)
            return userRepo.findAll(keyword, pageable);

        return userRepo.findAll(pageable);
    }

    public User saveUserToDb(User theUser) {
        boolean isUpdatingUser = (theUser.getId() != null);

        if (isUpdatingUser){
            User existingUser = userRepo.findById(theUser.getId()).get();
            if(theUser.getPassword().isEmpty()){
                theUser.setPassword(existingUser.getPassword());
            }else {
                encodePassword(theUser);
            }
        }else {
            encodePassword(theUser);
        }

        return userRepo.save(theUser);
    }

    public User updateAccount(User userInForm) {
        User userInDb = userRepo.findById(userInForm.getId()).get();

        if (!userInForm.getPassword().isEmpty()){
            userInDb.setPassword(userInForm.getPassword());
            encodePassword(userInDb);
        }

        if (userInForm.getPhotos() != null){
            userInDb.setPhotos(userInForm.getPhotos());
        }

        userInDb.setFirstName(userInForm.getFirstName());
        userInDb.setLastName(userInForm.getLastName());

        return userRepo.save(userInDb);
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

    public void updateEnabledStatus(Integer userId, boolean status){
        userRepo.updateEnabledStatus(userId, status);
    }


}
