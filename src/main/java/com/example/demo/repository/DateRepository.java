package com.example.demo.repository;

import com.example.demo.entity.BookingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DateRepository extends JpaRepository<BookingRoom, Long> {
}
