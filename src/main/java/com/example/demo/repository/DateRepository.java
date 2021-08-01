package com.example.demo.repository;

import com.example.demo.entity.BookingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DateRepository extends JpaRepository<BookingRoom, Long> {
    @Modifying
    @Query(value = "delete from booking_room where room_id = ?", nativeQuery = true)
    void deleteRoomInBookingRoom(Long roomId);
    @Query(value="select * from booking_room where" +
            "(DATE(end) BETWEEN ? AND ?) or " +
            "(DATE(start) between ? AND ?)", nativeQuery=true)
    List<BookingRoom> findRoomByDateBooking(LocalDate startDate, LocalDate endDate, LocalDate startDate1, LocalDate endDate1);

    @Query(value = "SELECT DATEDIFF(?1, ?2) from booking_room", nativeQuery = true)
    int numberOfDay(LocalDate end, LocalDate start);
    @Query(value = "SELECT DATEDIFF(?1, ?2) from booking_room", nativeQuery = true)
    int numberOfDay1(String  end, String start);
}
