package com.example.demo.controller;

import com.example.demo.entity.ERole;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.entity.UserDetail;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.payload.reponse.MessageResponse;
import com.example.demo.payload.reponse.ThongKeKhachSanTheoThanhPhoAdmin;
import com.example.demo.payload.reponse.ThongKeSoLuongKhachSanTheoThanhPho;
import com.example.demo.payload.request.SignupRequest;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.jwt.JwtUtils;
import com.example.demo.service.HotelService;
import com.example.demo.service.LocalizationService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping("/admin")
public class UserManageController {
    @Autowired
    UserService userService;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    HotelService hotelService;
    @Autowired
    LocalizationService localizationService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder encoder;
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



    // API tao 1 tai khoan Admin
    @PostMapping("/createAdmin")
    public ResponseEntity<?> createAdmin(@Valid @RequestBody SignupRequest signUpRequest) {

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
            Role userRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found "));
            roles.add(userRole);
        }

        user.setRoles(roles);
        userDetail.setUser(user);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("successfull"));
    }

    // API thong ke Admin
    @GetMapping("/thongke/{cityName}")
    public ResponseEntity<?> getAllHotelByCityName(@PathVariable("cityName") String cityName){
        List<ThongKeKhachSanTheoThanhPhoAdmin> thongkeAdmins = hotelService.getAllHotelByCityNameAdmin(cityName);
        return ResponseEntity.ok().body(thongkeAdmins);
    }

    @GetMapping("/thongke")
    public ResponseEntity<?> countNumberOfHotelInCity(){
        List<ThongKeSoLuongKhachSanTheoThanhPho> thongkeAdmins = localizationService.countNumberOfHotelInCity();
        return ResponseEntity.ok().body(thongkeAdmins);
    }

}
