package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.entity.UserDetail;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.security.jwt.JwtUtils;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/admin")
public class UserManageController {
    @Autowired
    UserService userService;
    @Autowired
    JwtUtils jwtUtils;
    /*
     * Tat ca cac director da dang ky nhung tai khoan chua dc mo
     * */
    @GetMapping("/getDirector")
    public ResponseEntity<?> directorLookFalse(){
        List<User> listDirector = userService.findDirectorLookFalse();
        return ResponseEntity.ok().body(listDirector);
    }

    /*
     * API unlock tai khoan
     */
    @PutMapping("/getDirector/unlock/{directorId}")
    public ResponseEntity<?> moKhoaTaiKhoan(@PathVariable("directorId") long directorId){
        try {
            userService.UnlockUser(directorId);
        }catch (UserNotFoundException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't find this account on database", e);
        }
        return  ResponseEntity.ok().body("Done unlock");
    }

//   Xem thong tin director
    @GetMapping("/getDirector/view/{directorId}")
    public ResponseEntity<?> viewDirector(@PathVariable("directorId") Long directorId){
        UserDetail directorDetails = userService.findOne(directorId);
        return ResponseEntity.ok().body(directorDetails);
    }


}
