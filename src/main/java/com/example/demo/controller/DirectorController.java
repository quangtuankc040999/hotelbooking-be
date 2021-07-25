package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.payload.reponse.MessageResponse;
import com.example.demo.payload.request.HotelRequest;
import com.example.demo.payload.request.RoomRequest;
import com.example.demo.security.jwt.GetUserFromToken;
import com.example.demo.service.HotelService;
import com.example.demo.service.ImageService;
import com.example.demo.service.LocalizationService;
import com.example.demo.service.RoomService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@CrossOrigin
@RequestMapping("/director")

public class DirectorController {
    @Autowired
    private GetUserFromToken getUserFromToken;
    @Autowired
    private ImageService imageService;
    @Autowired
    private LocalizationService localizationService;
    @Autowired
    private HotelService hotelService;
    @Autowired
    private RoomService roomService;

// API thêm khách sạn

    @PostMapping(value = "/hotel/new-hotel", consumes = {"multipart/form-data"})
    public ResponseEntity<?> addHotell(@RequestParam("hotelRequest") String jsonHotel, @RequestParam(name = "images") MultipartFile[] images, @RequestHeader("Authorization") String token){

        try {
            String newToken = token.substring(7);
            User hOwner = getUserFromToken.getUserByUserNameFromJwt(newToken);
            if(hOwner.isLocked()){
                Gson gson = new Gson();
                HotelRequest hotelRequest = gson.fromJson(jsonHotel, HotelRequest.class) ;

                Hotel hotel = new Hotel();
                for(int i = 0; i < images.length; i++) {
                    imageService.save(new Image(images[i].getBytes(), hotel));
                }

                hotel.sethOwner(hOwner);

                hotel.setName(hotelRequest.getName());
                hotel.setStandard(hotelRequest.getStandard());

                Localization localization = new Localization();
                localization.setCity(hotelRequest.getLocalization().getCity());
                localization.setCountry(hotelRequest.getLocalization().getCountry());
                localization.setStreet(hotelRequest.getLocalization().getStreet());
                localization.setHotel(hotel);
                hotel.setAddress(localization);

                localizationService.saveLoacation(localization);
                hotelService.saveHotel(hotel);}
            else{
                return ResponseEntity.ok(new MessageResponse("Your account is not active, Please wait!"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  ResponseEntity.ok(new MessageResponse("add hotel successfully"));
    }

// API thêm phòng
    @PostMapping("/hotel/{hotelId}/new-room")
    public ResponseEntity<?> addRoom(@PathVariable("hotelId") Long hotelId,@RequestParam(name = "images") MultipartFile[] images, @RequestParam("roomRequest") String jsonRoom) {
        try {
            Hotel hotel = hotelService.findHotelById(hotelId);
            Gson gson = new Gson();
            RoomRequest roomRequest = gson.fromJson(jsonRoom, RoomRequest.class);
            Room room = new Room();

            for (int i = 0; i < images.length; i++) {
                imageService.save(new Image(images[i].getBytes(), room));
            }
            room.setHotel(hotel);
            room.setType(roomRequest.getType());
            room.setArea(roomRequest.getArea());
            room.setCapacity(roomRequest.getCapacity());
            room.setDescription(roomRequest.getDescription());
            room.setName(roomRequest.getName());
            room.setPrice(roomRequest.getPrice());
            room.setAdded(LocalDate.now());
            roomService.saveRoom(room);
        }catch (IOException e){
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(new MessageResponse("add room successfully"));
    }

}
