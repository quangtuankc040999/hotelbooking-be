package com.example.demo.controller;

import com.example.demo.entity.BookingRoom;
import com.example.demo.entity.Hotel;
import com.example.demo.entity.Room;
import com.example.demo.payload.reponse.HotelSearchResponse;
import com.example.demo.payload.request.SearchRequest;
import com.example.demo.service.DateService;
import com.example.demo.service.HotelService;
import com.example.demo.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
@RestController
@CrossOrigin
public class HomeController {
    @Autowired
    HotelService hotelService;
    @Autowired
    RoomService roomService;

    @Autowired
    DateService dateService;

    List<HotelSearchResponse> hotelSearchResponseList = new ArrayList<>();

    @PostMapping(value = "/search")
        public ResponseEntity<?> search(@RequestBody SearchRequest searchRequest) {

        List<Hotel> hotels = hotelService.getAllHotelsByCityName(searchRequest.getCityName());
        List<Room> roomList = new ArrayList<>();
        List<BookingRoom> bookingRoomList = dateService.getAllRoomByDateBooking(searchRequest.getStart(), searchRequest.getEnd());

        for (Hotel hotel: hotels) {
            List<Room> rooms = roomService.searchRoomByCapacity(hotel.getId(), searchRequest.getCapacity());
            for (Room room: rooms) {
                roomList.add(room);
                for (BookingRoom bk: bookingRoomList) {
                    if(bk.getRoom().getId() == room.getId()) {
                        roomList.remove(room);
                        break;
                    }
                }
            }
        }
        List<Hotel> hotelList = new ArrayList<>();
        for(Room room : roomList) {
            Hotel hotel = room.getHotel();
            hotelList.add(hotel);
        }
        for (int i = 0; i < hotelList.size(); i++ ) {
            for (int j = i+1; j < hotelList.size(); j++) {
                if (hotelList.get(i).getId() == hotelList.get(j).getId()) {
                    hotelList.remove(hotelList.get(j));
                    j--;
                }
            }
        }
        hotelSearchResponseList.clear();
        for (Hotel hotel: hotelList) {
            HotelSearchResponse hotelSearchResponse = new HotelSearchResponse();
            Hotel hotel1 = hotel;

            List<Room> rooms = new ArrayList<>();
            for(Room room: roomList) {
                if (hotel.getId() == room.getHotel().getId()) {
                    rooms.add(room);
                }
            }
            hotel1.setRooms(rooms);
            hotelSearchResponse.setHotel(hotel1);
            hotelSearchResponseList.add(hotelSearchResponse);
        }
        return ResponseEntity.ok(hotelSearchResponseList);
    }
}
