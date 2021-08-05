package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.payload.reponse.MessageResponse;
import com.example.demo.payload.reponse.ThongKeDatPhongDirector;
import com.example.demo.payload.reponse.ThongKeDoanhThuDirector;
import com.example.demo.payload.reponse.ThongKeTatCaDoanhThuCuaKhachSanTheoThang;
import com.example.demo.payload.request.HotelRequest;
import com.example.demo.payload.request.RoomRequest;
import com.example.demo.security.jwt.GetUserFromToken;
import com.example.demo.service.*;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

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
    @Autowired
    private DateService dateService;
    @Autowired
    private CancelBookingService cancelBookingService;
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
    //API delete hotel
    @Transactional
    @DeleteMapping(value = "/hotel/{hotelId}/delete")
    public ResponseEntity<?> deleteHotel(@PathVariable("hotelId") Long hotelId) {

        List<Room> roomList = roomService.getAllRoomByHotelId(hotelId);
        for(Room room: roomList) {
            roomService.deleteRoom(room.getId());
        }
        localizationService.deleteLocalizionHotel(hotelId);
        imageService.deleteImgHotel(hotelId);
        hotelService.deleteHotel(hotelId);
        return ResponseEntity.ok().body(new MessageResponse("Delete hotel successful"));
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

    // API Delete room
    @Transactional
    @DeleteMapping("/hotel/{hotelId}/{roomId}/delete")
    public ResponseEntity<?> deleteRoom(@PathVariable("roomId") Long roomId){
        dateService.deleteBookingByRoom(roomId);
        cancelBookingService.deleteBookingByRoom(roomId);
        imageService.deleteImgRoom(roomId);
        roomService.deleteRoom(roomId);
        return  ResponseEntity.ok().body(new MessageResponse("Delete room successful"));
    }



// API thong ke Director
    @GetMapping("/thongke/{month}")
    public ResponseEntity<?> getAllBookingInMonth(@RequestHeader("Authorization") String token, @PathVariable("month") Long month){
        Long idDirector = getUserFromToken.getUserByUserNameFromJwt(token.substring(7)).getId();
        List<ThongKeDatPhongDirector> thongKeDirectors = dateService.getAllBookingInMonth(month, idDirector);
    return  ResponseEntity.ok().body(thongKeDirectors);
    }
    @GetMapping("/thongke/{month}/{hotelId}")
    public ResponseEntity<?> getAllBookingInMonthOfHotel(@RequestHeader("Authorization") String token, @PathVariable("month") Long month, @PathVariable("hotelId") Long hotelId){
        Long idDirector = getUserFromToken.getUserByUserNameFromJwt(token.substring(7)).getId();
        List<ThongKeDatPhongDirector> thongKeDirectors = dateService.getAllBookingInMonthOfHotel(month, idDirector, hotelId);
        return  ResponseEntity.ok().body(thongKeDirectors);
    }

    // API thongke doanh thu cua tat ca khach san trong thang
    @GetMapping("/thongke/total/allHotel/{month}")
    public ResponseEntity<?> getTotalAllHotelInMonth(@RequestHeader("Authorization") String token, @PathVariable("month") Long month){
        Long idDirector = getUserFromToken.getUserByUserNameFromJwt(token.substring(7)).getId();
        List<ThongKeDoanhThuDirector> thongKeDirectors = dateService.getTotalAllHotelInMonth(month, idDirector);
        return  ResponseEntity.ok().body(thongKeDirectors);
    }

    // Thong ke doanh thu cua Khach san A trong tung thang
    @GetMapping("/thongke/total/eachHotel/{hotelId}")
    public ResponseEntity<?> getTotalOfHotelEachMonth(@RequestHeader("Authorization") String token, @PathVariable("hotelId") Long hotelId){
        Long idDirector = getUserFromToken.getUserByUserNameFromJwt(token.substring(7)).getId();
        List<ThongKeTatCaDoanhThuCuaKhachSanTheoThang> thongKeDirectors = dateService.getTotalOfHotelEachMonth(hotelId, idDirector);
        return  ResponseEntity.ok().body(thongKeDirectors);
    }


    // API get all hotel of director
    @GetMapping("/all-hotel")
    public ResponseEntity<?> getAllHotel(@RequestHeader("Authorization") String token){
        Long idDirector = getUserFromToken.getUserByUserNameFromJwt(token.substring(7)).getId();
        List<Hotel> hotels = hotelService.findAllHotelByDirectorId(idDirector);
        return  ResponseEntity.ok().body(hotels);
    }

    @GetMapping("/{hotelId}/all-room")
    public  ResponseEntity<?> getAllRoom(@PathVariable("hotelId") Long hotelId){
        List<Room> rooms = roomService.getAllRoomByHotelId(hotelId);
        return  ResponseEntity.ok().body(rooms);
    }


}
