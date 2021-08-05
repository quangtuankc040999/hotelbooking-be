package com.example.demo.repository;

import com.example.demo.entity.Hotel;
import com.example.demo.payload.reponse.ThongKeKhachSanTheoThanhPhoAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository  extends JpaRepository<Hotel,Long > {
    Hotel findById (long id);

    @Modifying
    @Query(value ="delete from hotel where hotel.id = ?", nativeQuery=true)
    void deleteHotel(Long id);

    @Query(value = "SELECT * FROM hotel where h_owner_id = ?", nativeQuery = true)
    List<Hotel> getAllHotelByDirectorId (Long idDirector);
    @Query(value="SELECT * FROM hotel  join localization on hotel.id = localization.hotel_id where localization.city = ?", nativeQuery=true)
    List<Hotel> findAllByCityName (String cityName);


    //Thong ke Amin
    @Query(value = "select hotel.name as hotelName, city as city, street as street, user_detail.name_user_detail as directorName, email as directorEmail\n" +
            "from hotel\n" +
            "join localization on hotel.id = localization.hotel_id\n" +
            "join user on hotel.h_owner_id = user.id\n" +
            "join user_detail on user_detail.user_id = user.id \n" +
            "where city like %?%" , nativeQuery = true)
    List<ThongKeKhachSanTheoThanhPhoAdmin> getAllHotelInCity(String cityName);

}
