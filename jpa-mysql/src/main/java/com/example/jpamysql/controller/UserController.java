package com.example.jpamysql.controller;

import com.example.jpamysql.entity.User;
import com.example.jpamysql.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

/**
 * 创建控制器
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping(path = "/add")
    public String addNewUser(@RequestParam String name
            , @RequestParam String email) {

        User n = new User();
        n.setName(name);
        n.setEmail(email);
        userRepository.save(n);
        return "Saved";
    }

    @GetMapping(path = "/all")
    public Iterable<User> getAllUsers() {
        // This returns a JSON or XML with the users
        return userRepository.findAll();
    }

}
