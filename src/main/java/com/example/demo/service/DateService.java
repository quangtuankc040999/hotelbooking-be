package com.example.demo.service;

import com.example.demo.payload.reponse.ThongKeDatPhongDirector;
import com.example.demo.payload.reponse.ThongKeDoanhThuDirector;
import com.example.demo.entity.BookingRoom;
import com.example.demo.entity.User;
import com.example.demo.payload.reponse.ThongKeDatPhongUser;
import com.example.demo.repository.DateRepository;
import com.example.demo.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class DateService {
    @Autowired
    private DateRepository dateRepository;
    @Autowired
    private RoomRepository roomRepository;


    public LocalDate startDate(String from) {
        DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(from, formatter);
        return localDate;
    }


    public LocalDate endDate(String to) {
        DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(to, formatter);
        return localDate;
    }

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
    public List<BookingRoom> getAllRoomByDateBooking(LocalDate start, LocalDate end) {
        return dateRepository.findRoomByDateBooking(start, end, start, end);
    }
    public void bookRoom1(String from, String to, long id, User user) {
        BookingRoom bookingRoom = new BookingRoom();
        bookingRoom.setStart(startDate(from));
        bookingRoom.setEnd(endDate(to));
        bookingRoom.setRoom(roomRepository.getOne(id));
        bookingRoom.setHost(user);
        dateRepository.save(bookingRoom);
    }
    public void bookRoom(LocalDate from, LocalDate to, long id, User user) {
        BookingRoom bookingRoom = new BookingRoom();
        bookingRoom.setStart(from);
        bookingRoom.setEnd(to);
        bookingRoom.setRoom(roomRepository.getOne(id));
        bookingRoom.setHost(user);
        dateRepository.save(bookingRoom);
    }
    public int numberOfDay(LocalDate end, LocalDate start){
        return  dateRepository.numberOfDay(end, start);
    }
    public int numberOfDay1(String end, String start){
        return  dateRepository.numberOfDay1(end, start);
    }


    public void huyBooking(Long bookingId){dateRepository.huyBooking(bookingId);}

    public  BookingRoom findOneBooking(Long bookingId){ return dateRepository.findBookingById(bookingId);}


    public List<ThongKeDatPhongUser> getAllDateBeforeNow(Long id) {
        return dateRepository.findAllBookingRoomBeforeNow(id);
    }
    public List<ThongKeDatPhongUser> getAllDateAfterNow(Long id) {
        return dateRepository.findAllBookingRoomAfterNow(id);
    }
    public List<ThongKeDatPhongUser> getAllCancelBooking(Long id) {
        return dateRepository.findAllCancelBooking(id);
    }

}
