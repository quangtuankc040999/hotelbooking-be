package com.example.demo.payload.reponse;

import java.time.LocalDate;

public interface ThongKeDatPhongUser {
    LocalDate getEnd() ;
    LocalDate getStart();
    String getRoomName();
    String getRoomType();
    String getHotelName();
    Long getTotal();
    String getCity();
    String getCountry();
    String getStreet();
}
