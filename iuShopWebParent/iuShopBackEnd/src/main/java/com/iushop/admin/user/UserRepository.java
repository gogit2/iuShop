package com.iushop.admin.user;

import com.iushop.common.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE u.email = :email")
    public User getUserByEmail(@Param("email") String email);

//    public User getUserById();

    @Override
    Optional<User> findById(Integer id);

    Long countById(Integer id);

    @Query("update User u set u.enabled= ?2 where u.id = ?1")
    @Modifying
    public void updateEnabledStatus(Integer userId, boolean enabled);

}

