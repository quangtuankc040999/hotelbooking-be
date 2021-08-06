package com.example.demo.repository;

import com.example.demo.entity.CancelBooking;
import com.example.demo.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(value = "SELECT * FROM bookingbe.comment where hotel_id = ? and room_id = ?", nativeQuery = true)
    List<Comment> getRoomComment (Long hotelId, Long roomId);
}
