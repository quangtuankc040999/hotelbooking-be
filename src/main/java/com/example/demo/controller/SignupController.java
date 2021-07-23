package com.example.demo.controller;

import com.example.demo.entity.ERole;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.entity.UserDetail;
import com.example.demo.payload.reponse.MessageResponse;
import com.example.demo.payload.request.SignupRequest;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;


@CrossOrigin(origins = "*")
@RestController
public class SignupController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.ok(new MessageResponse("username is taken"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.ok(new MessageResponse("email is taken"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));
        UserDetail userDetail = new UserDetail();
        userDetail.setNameUserDetail(signUpRequest.getUserDetail().getNameUserDetail());
        userDetail.setBirth(signUpRequest.getUserDetail().getBirth());
        userDetail.setPhoneNumber(signUpRequest.getUserDetail().getPhoneNumber());
        user.setUserDetail(userDetail);


        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found "));
            roles.add(userRole);
        } else {

            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        System.out.println("admin");
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        roles.add(adminRole);
                        break;
                    case "director":
                        Role dirRole = roleRepository.findByName(ERole.ROLE_DIRECTOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        roles.add(dirRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        userDetail.setUser(user);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("successfull"));
    }
}