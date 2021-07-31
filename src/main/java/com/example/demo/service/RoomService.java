package com.example.demo.service;

import com.example.demo.entity.Room;
import com.example.demo.repository.CancelBookingRepository;
import com.example.demo.repository.DateRepository;
import com.example.demo.repository.ImageRepository;
import com.example.demo.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private DateService dateService;
    @Autowired
    private DateRepository dateRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private CancelBookingRepository cancelBookingRepository;

    public void saveRoom(Room room){roomRepository.save(room);}
    public Room getRoomById(Long roomid, Long hotelid) {
        return roomRepository.findOneById(roomid, hotelid);
    }
    public void deleteRoom(Long roomId){
        cancelBookingRepository.deleteRoomInCancelBookingRoom(roomId);
        dateRepository.deleteRoomInBookingRoom(roomId);
        imageRepository.deleteImgRoom(roomId);
        roomRepository.deleteRoom(roomId);
    }
    public List<Room> getAllRoomByHotelId(Long id) {
        return roomRepository.findAllRoomByHotelId(id);
    }

}
