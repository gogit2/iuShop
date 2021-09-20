package com.iushop.admin.user;


import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PasswordEncoderTest {

    @Test
    public void testEncodePassword(){
        BCryptPasswordEncoder passEncoder = new BCryptPasswordEncoder();
        String testedPass = "Ahmoh389";
        String encodedPass = passEncoder.encode(testedPass);
        System.out.println(encodedPass);
        boolean matches = passEncoder.matches(testedPass, encodedPass);
        assertThat(matches).isTrue();
    }

}
