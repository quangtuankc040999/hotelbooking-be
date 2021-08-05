package com.example.demo.service;

import com.example.demo.entity.Localization;
import com.example.demo.payload.reponse.ThongKeSoLuongKhachSanTheoThanhPho;
import com.example.demo.repository.LocalizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocalizationService {
    @Autowired
    LocalizationRepository localizationRepository;

    public void saveLoacation (Localization localization){
        localizationRepository.save(localization);
    }
    public Localization getLocationById(Long id) {
        return localizationRepository.getOne(id);
    }

    public void deleteLocalizionHotel(Long hotelId) {
        localizationRepository.deleteLocalizationHotel(hotelId);
    }

    public List<ThongKeSoLuongKhachSanTheoThanhPho> countNumberOfHotelInCity(){
        return  localizationRepository.countNumberOfHotel();
    }

}
