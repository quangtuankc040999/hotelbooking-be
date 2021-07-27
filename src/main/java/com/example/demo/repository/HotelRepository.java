package com.example.demo.repository;

import com.example.demo.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository  extends JpaRepository<Hotel,Long > {
    Hotel findById (long id);

    @Modifying
    @Query(value ="delete from hotel where hotel.id = ?", nativeQuery=true)
    void deleteHotel(Long id);
}
