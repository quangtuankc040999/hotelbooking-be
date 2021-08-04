package com.example.demo.repository;

import com.example.demo.entity.BookingRoom;
import com.example.demo.payload.reponse.ThongKeDatPhongUser;
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

    @Modifying
    @Query(value = "delete from booking_room where id= ?;", nativeQuery = true)
    void huyBooking (Long boookingId);

    @Query(value = "SELECT * FROM booking_room where id = ?1", nativeQuery = true)
    BookingRoom findBookingById (Long bookingId);


    // thong ke
    @Query(value = "select end, start, room.name as roomName, room.type as roomType, hotel.name as hotelName, city, street, country,  datediff(end,start)*room.price as Total\n" +
            "from booking_room \n" +
            "join room on booking_room.room_id = room.id \n" +
            "join hotel on room.hotel_id = hotel.id\n" +
            "join localization on hotel.id = localization.hotel_id\n" +
            "where start <= current_date() and host_id= ?  ", nativeQuery = true)
    List<ThongKeDatPhongUser> findAllBookingRoomBeforeNow(Long id);


    @Query(value = "select end, start, room.name as roomName, room.type as roomType, hotel.name as hotelName, city, street, country,  datediff(end,start)*room.price as Total\n" +
            "from booking_room \n" +
            "join room on booking_room.room_id = room.id \n" +
            "join hotel on room.hotel_id = hotel.id\n" +
            "join localization on hotel.id = localization.hotel_id\n" +
            "where start >= current_date() and host_id= ? ", nativeQuery = true)
    List<ThongKeDatPhongUser> findAllBookingRoomAfterNow(Long id);

    @Query(value = "select end, start, room.name as roomName, room.type as roomType, hotel.name as hotelName, city, street, country,  datediff(end,start)*room.price as Total\n" +
            "from cancel_booking\n" +
            "join room on cancel_booking.room_id = room.id \n" +
            "join hotel on room.hotel_id = hotel.id\n" +
            "join localization on hotel.id = localization.hotel_id\n" +
            "where host_id= ?", nativeQuery = true)
    List<ThongKeDatPhongUser> findAllCancelBooking(Long id);


}
