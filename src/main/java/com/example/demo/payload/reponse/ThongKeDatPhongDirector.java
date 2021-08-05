package com.example.demo.payload.reponse;

import java.time.LocalDate;

public interface ThongKeDatPhongDirector {
    LocalDate getEnd() ;
    LocalDate getStart();
    String getCity();
    String getHotelName();
    String getRoomName();
    String getRoomType();
    Long getBookingId();
    String getUserName();
    String getUserEmail();
    Long getTotal();
}
