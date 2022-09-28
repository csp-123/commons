package com.commons.auth;

import com.commons.auth.controller.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AuthApplicationTests {
    @Autowired
    UserController userController;

    @Test
    void contextLoads() {

    }

}
