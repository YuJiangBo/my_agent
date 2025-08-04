package com.yu.my_agent.controller;

import com.yu.my_agent.entity.mysql.User;
import com.yu.my_agent.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户管理")
@RestController
@RequestMapping("/api")
public class MainController {
    
    @Autowired
    private UserService userService;
    
    // MySQL操作
    @Operation(summary = "创建用户", description = "创建一个用户")
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }
}
