package com.yu.my_agent.service;

import com.yu.my_agent.entity.mysql.User;
import com.yu.my_agent.mapper.mysql.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    public User createUser(User user) {
        userMapper.insert(user);
        return user;
    }
    
    public User getUserById(Long id) {
        return userMapper.selectById(id);
    }
    
    public List<User> getAllUsers() {
        return userMapper.selectList(null);
    }
    
    public List<User> searchUsersByEmail(String email) {
        return userMapper.selectUsersByEmailContaining(email);
    }
    
    public void updateUser(User user) {
        userMapper.updateById(user);
    }
    
    public void deleteUser(Long id) {
        userMapper.deleteById(id);
    }
}
