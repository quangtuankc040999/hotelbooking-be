package com.example.demo.service;

import com.example.demo.entity.CancelBooking;
import com.example.demo.repository.CancelBookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CancelBookingService {
    @Autowired
    CancelBookingRepository cancelBookingRepository;


    public void deleteBookingByRoom(Long roomId) {
        cancelBookingRepository.deleteRoomInCancelBookingRoom(roomId);
    }
}
