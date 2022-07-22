package com.iushop.admin.security;

import com.iushop.admin.user.UserRepository;
import com.iushop.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class IuShopUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User userByEmail = userRepo.getUserByEmail(email);

        if (userByEmail != null){
            return new IuShopUserDetails(userByEmail);
        }

        throw new UsernameNotFoundException("Could not find user with email: " + email);
    }
}
