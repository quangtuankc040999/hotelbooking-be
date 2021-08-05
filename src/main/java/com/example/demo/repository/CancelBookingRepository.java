package com.example.demo.repository;

import com.example.demo.entity.CancelBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CancelBookingRepository extends JpaRepository<CancelBooking, Long> {
    @Modifying
    @Query(value = "delete from cancel_booking where room_id = ?", nativeQuery = true)
    void deleteRoomInCancelBookingRoom(Long roomId);

    @Query(value = "select * from cancel_booking where host_id= ?", nativeQuery = true)
    List<CancelBooking> findAllByHost_Id(Long id);

}
