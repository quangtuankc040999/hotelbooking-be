package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.entity.UserDetail;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.repository.UserDetailRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserDetailRepository userDetailRepository;

    public User findByUserName(String username){
        return  userRepository.findByUsername(username);
    }

    public User getUserById(Long id){
        return userRepository.getOne(id);
    }

    public boolean lockUser(Long userId) throws UserNotFoundException {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("Khong ton tai tai khoan");
        }
        User user = userRepository.getOne(userId);
        user.setLocked(true);
        userRepository.save(user);
        return true;
    }

    public boolean UnlockUser(Long userId) throws UserNotFoundException {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("Khong ton tai tai khoan");
        }
        User user = userRepository.getOne(userId);
        user.setLocked(true);
        userRepository.save(user);
        return true;
    }

    public User findUserByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail);
    }

    // Get directer da dang ky nhung tai khoan chua duoc mo
    public List<User> findDirectorLookFalse() { return userRepository.findDirectorLookedFalse();}


    public UserDetail findOne (Long userId){
        return userDetailRepository.findOne(userId);
    }
}
