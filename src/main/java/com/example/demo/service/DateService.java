package com.example.demo.service;

import com.example.demo.repository.DateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DateService {
    @Autowired
    private DateRepository dateRepository;

    public void deleteBookingByRoom(Long roomId) {
        dateRepository.deleteRoomInBookingRoom(roomId);
    }

}
