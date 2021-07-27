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
import org.springframework.transaction.annotation.Transactional;
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
/*
*  API FOR HOTEL
* */
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

    // API update hotel

    @GetMapping(value = "/hotel/{hotelId}/update")
    public ResponseEntity<?> updateHotel(@PathVariable("hotelId") Long hotelId) {
        return ResponseEntity.ok().body(hotelService.findHotelById(hotelId));
    }


    @Transactional
    @PostMapping(value = "/hotel/{hotelId}/update/save")
    public ResponseEntity<?> SaveUpdateHotel(@RequestParam("hotelRequest") String jsonHotel, @PathVariable("hotelId") Long hotelId,@RequestParam(name = "images") MultipartFile[] images ) {
        try {
            Gson gson = new Gson();
            HotelRequest hotelRequest = gson.fromJson(jsonHotel, HotelRequest.class) ;

            Hotel hotel = hotelService.findHotelById(hotelId);
            hotel.setStandard(hotelRequest.getStandard());
            hotel.setName(hotelRequest.getName());

            Localization localization = localizationService.getLocationById(hotel.getAddress().getId());
            localization.setCity(hotelRequest.getLocalization().getCity());
            localization.setCountry(hotelRequest.getLocalization().getCountry());
            localization.setStreet(hotelRequest.getLocalization().getStreet());
            hotel.setAddress(localization);

            localizationService.saveLoacation(localization);
            hotel.setAddress(localization);
            imageService.deleteImgHotel(hotelId);
            for(int i = 0; i < images.length; i++) {
                imageService.save(new Image(images[i].getBytes(), hotel));
            }
            hotelService.saveHotel(hotel);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(new MessageResponse("Save changes"));
    }

    /*
     *  API FOR ROOM
     * */

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


    // API update room
    @GetMapping(value = "/hotel/{hotelId}/{roomId}/update")
    public ResponseEntity<?> updateRoom(@PathVariable("roomId") Long roomId, @PathVariable("hotelId") Long hotelId) {
        return ResponseEntity.ok().body(roomService.getRoomById(roomId, hotelId));
    }
    @Transactional
    @PostMapping(value = "/hotel/{hotelId}/{roomId}/update/save")
    public ResponseEntity<?> SaveUpdateRoom(@RequestParam("roomRequest") String jsonRoom, @PathVariable("hotelId") Long hotelId,@PathVariable("roomId") Long roomId, @RequestParam(required = false, name = "images") MultipartFile[] images ) {
        try {
            Gson gson = new Gson();
            RoomRequest roomRequest = gson.fromJson(jsonRoom, RoomRequest.class) ;

            Room room = roomService.getRoomById(roomId, hotelId);
            room.setName(roomRequest.getName());
            room.setType(roomRequest.getType());
            room.setPrice(roomRequest.getPrice());
            room.setDescription(roomRequest.getDescription());
            room.setCapacity(roomRequest.getCapacity());
            room.setArea(roomRequest.getArea());
            imageService.deleteImgRoom(roomId);
            for(int i = 0; i < images.length; i++) {
                imageService.save(new Image(images[i].getBytes(), room));
            }
            roomService.saveRoom(room);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().body(new MessageResponse("Save changes"));
    }


}
