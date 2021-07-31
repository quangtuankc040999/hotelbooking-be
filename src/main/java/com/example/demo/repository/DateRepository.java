package com.example.demo.repository;

import com.example.demo.entity.BookingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DateRepository extends JpaRepository<BookingRoom, Long> {
    @Modifying
    @Query(value = "delete from booking_room where room_id = ?", nativeQuery = true)
    void deleteRoomInBookingRoom(Long roomId);
}
