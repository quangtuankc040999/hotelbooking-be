package com.example.demo.repository;


import com.example.demo.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository  extends JpaRepository<Room, Long> {
    @Query(value="select * from room where id= ? and hotel_id = ?", nativeQuery=true)
    Room findOneById(Long roomid, Long hotelid);

    @Modifying
    @Query(value ="delete from room where id = ?", nativeQuery=true)
    void deleteRoom(Long roomId);

    @Query(value="select * from room where hotel_id= ?", nativeQuery=true)
    List<Room> findAllRoomByHotelId(Long id);

}
