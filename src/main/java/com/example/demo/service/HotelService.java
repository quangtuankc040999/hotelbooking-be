package com.example.demo.service;

import com.example.demo.entity.Hotel;
import com.example.demo.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelService {
    @Autowired
    private HotelRepository hotelRepository;

    public void saveHotel(Hotel hotel){hotelRepository.save(hotel);}
    public  Hotel  findHotelById (long id){
        return  hotelRepository.findById(id);
    }
    public void deleteHotel(Long id) {
        hotelRepository.deleteHotel(id);
    }

    public List<Hotel> findAllHotelByDirectorId(Long directorId) {
        return hotelRepository.getAllHotelByDirectorId(directorId);
    public List<Hotel> getAllHotelsByCityName(String cityName) {
        return hotelRepository.findAllByCityName(cityName);
    }

}
