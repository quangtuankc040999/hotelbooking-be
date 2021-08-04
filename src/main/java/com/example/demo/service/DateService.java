package com.example.demo.service;

import com.example.demo.payload.reponse.ThongKeDatPhongDirector;
import com.example.demo.payload.reponse.ThongKeDoanhThuDirector;
import com.example.demo.repository.DateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DateService {
    @Autowired
    private DateRepository dateRepository;

    public void deleteBookingByRoom(Long roomId) {
        dateRepository.deleteRoomInBookingRoom(roomId);
    }
    // Thong ke director
    public List<ThongKeDatPhongDirector> getAllBookingInMonth(Long month, Long idDirector){
        return dateRepository.getAllBookingInMonth(month, idDirector);
    }


    public List<ThongKeDatPhongDirector> getAllBookingInMonthOfHotel(Long month, Long idDirector, Long hotelId){
        return dateRepository.getAllBookingInMonthOfHotel(month, idDirector, hotelId);
    }

    public List<ThongKeDoanhThuDirector> getTotalAllHotelInMonth(Long month, Long idDirector){
        return  dateRepository.getTotalAllHotelInMonth(month, idDirector);
    }

}
