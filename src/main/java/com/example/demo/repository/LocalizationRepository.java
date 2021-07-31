package com.example.demo.repository;

import com.example.demo.entity.Localization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LocalizationRepository  extends JpaRepository<Localization, Long> {
    @Modifying
    @Query(value ="delete from localization where hotel_id = ?", nativeQuery=true)
    void deleteLocalizationHotel(Long hotelid);
}
