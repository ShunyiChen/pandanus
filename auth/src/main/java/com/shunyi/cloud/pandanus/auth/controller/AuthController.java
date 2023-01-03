package com.shunyi.cloud.pandanus.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @ClassName AuthController
 * @Description TODO
 * @Author QD291NB
 * @Date 2022/12/29 15:44
 **/
@RestController
public class AuthController {

    @GetMapping("/user")
    public String index(Principal principal) {
        return principal.getName();
    }
}
