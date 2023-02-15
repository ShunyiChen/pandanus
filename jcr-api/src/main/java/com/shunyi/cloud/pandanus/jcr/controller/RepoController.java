package com.shunyi.cloud.pandanus.jcr.controller;

import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/jcr")
public class RepoController {

    @GetMapping("/viewRepository")
    public String viewRepository(Principal principal) {
        System.out.println("*****View repository");
        return String.format("View repository");
    }

    @PostMapping("/createRepository")
    public String createRepository(Principal principal) {
        System.out.println("*************Repository was created by "+principal.getName());
        return String.format("Repository was created.");
    }
}
