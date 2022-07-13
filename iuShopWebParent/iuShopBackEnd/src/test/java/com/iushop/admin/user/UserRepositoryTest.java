package com.iushop.admin.user;

import com.iushop.common.entity.Role;
import com.iushop.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void createInitialUserTable(){
    }

    @Test
    public void createOneUserWithRole(){
        Role roleUser = testEntityManager.find(Role.class, 1);
        User newUser = new User("ahmoh@iushop.net","pass22#89","Ahmed", "Mohamed");
        newUser.addRole(roleUser);
        userRepo.save(newUser);
        assertThat(newUser.getId()).isGreaterThan(0);
//        User delUser = testEntityManager.find(User.class, 1);
//        User newUser = new User("ahmoh@iudhop.net","pass22#89","Ahmed", "Mohamed");
//        userRepo.delete(delUser);
    }

    @Test
    public void createOneUserWithTwoRoles(){
        User newUserOmar = new User("omarahmed@iushop.net","OmarPass22#89","Omar", "Ahmed");
        Role roleEditor = new Role(3);
        Role roleAssistant = new Role(5);
        newUserOmar.addRole(roleEditor);
        newUserOmar.addRole(roleAssistant);
        User savedUser = userRepo.save(newUserOmar);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void listAllUsers(){
        Iterable<User> listUsers = userRepo.findAll();
        listUsers.forEach(gatedUser -> System.out.println(gatedUser));
    }

    @Test
    public void getUserById(){
        Optional<User> optionalUser = userRepo.findById(2);
        User theGatedUser = optionalUser.get();
        System.out.println(theGatedUser);
        assertThat(theGatedUser.getFirstName()).isEqualTo("Ahmed");
    }

    @Test
    public void updateUserDetails(){
        User updateUser = userRepo.findById(3).get();
        updateUser.setEnabled(true);
        userRepo.save(updateUser);
        assertThat(updateUser.getEnabled()).isTrue();
    }

//    @Test
//    public void updateUserId(){
//        User updateUser = userRepo.findById(4).get();
//        updateUser.setId(1);
//        userRepo.save(updateUser);
//    }

    @Test
    public void updateUserRoles(){
        User updateUserRole = userRepo.findById(3).get();

        Role salespersonRole = new Role(2);
        Role editorRole = new Role(3);

        updateUserRole.addRole(salespersonRole);
        // to remove a role from userId=3
        updateUserRole.getRoles().remove(editorRole);

        userRepo.save(updateUserRole);
    }

    @Test
    public void deleteUser(){
        Integer userId = 2;
        userRepo.deleteById(userId);
    }

    @Test
    public void testGetUserByEmail(){
        String testedEmail = "ahmohamed49u@gmail.com";
        User userByEmail = userRepo.getUserByEmail(testedEmail);
        assertThat(userByEmail).isNotNull();
    }

    @Test
    public void testCountById() {
        int userid = 3;
        long contById = userRepo.countById(userid);
        User theUser = userRepo.findById(userid).get();
        System.out.println(">>>>>>>>: "+contById);
        System.out.println(">>>>>>>>: "+theUser);
        assertThat(contById).isNotNull().isEqualTo(1);
    }

    // Test updateStatus
    @Test
    public void testUpdateEnabledStatus(){
        Integer userId = 2 ;
        userRepo.updateEnabledStatus(userId, false);
    }

    // Test Pagination
    @Test
    public void listFirstPage(){
        int pageNum = 0;
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<User> page = userRepo.findAll(pageable);
        List<User> listUsers = page.getContent();
        listUsers.forEach(user -> System.out.println(user));
        assertThat(listUsers.size()).isEqualTo(pageSize);
    }

}