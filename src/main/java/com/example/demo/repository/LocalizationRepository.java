package com.example.demo.repository;

import com.example.demo.entity.Localization;
import com.example.demo.payload.reponse.ThongKeSoLuongKhachSanTheoThanhPho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocalizationRepository  extends JpaRepository<Localization, Long> {
    @Modifying
    @Query(value ="delete from localization where hotel_id = ?", nativeQuery=true)
    void deleteLocalizationHotel(Long hotelid);

    @Query(value = "SELECT city as cityName, count(city) as numberOfHotel FROM localization group by city", nativeQuery = true)
    List<ThongKeSoLuongKhachSanTheoThanhPho> countNumberOfHotel();
}
