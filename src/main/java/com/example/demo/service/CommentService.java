package com.example.demo.service;

import com.example.demo.entity.Comment;
import com.example.demo.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;
     public void saveComment(Comment comment){
        commentRepository.save(comment);
     }
     public List<Comment> getAllComment(Long hotelId, Long roomId){
        return commentRepository.getRoomComment(hotelId, roomId);
     }
}
