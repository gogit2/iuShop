package com.iushop.admin.user;

import com.iushop.common.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE u.email = :email")
    public User getUserByEmail(@Param("email") String email);

//    public User getUserById();

    @Override
    Optional<User> findById(Integer id);

    @Query("select u from User u where concat(u.id, ' ', u.email, ' ', u.firstName, ' ', u.lastName) like %?1%")
    public Page<User> findAll(String keyword, Pageable pageable);

    Long countById(Integer id);

    @Query("update User u set u.enabled= ?2 where u.id = ?1")
    @Modifying
    public void updateEnabledStatus(Integer userId, boolean enabled);

}

