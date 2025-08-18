package com.example.program.controllers;


import com.example.program.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user")
public class UserController {

    @GetMapping("/dashboard")
    public String userDashboard() {
        return "return users most accessed pages";
    }

    // /profile
    // /accountsettings

}
