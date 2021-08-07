package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.payload.reponse.MessageResponse;
import com.example.demo.payload.reponse.ThongKeDatPhongUser;
import com.example.demo.payload.request.BookingRequest;
import com.example.demo.payload.request.CommentRequest;
import com.example.demo.payload.request.HotelRequest;
import com.example.demo.security.jwt.GetUserFromToken;
import com.example.demo.service.*;
import com.google.gson.Gson;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    GetUserFromToken getUserFromToken;
    @Autowired
    RoomService roomService;
    @Autowired
    DateService dateService;

    @Autowired
    EmailSenderService emailSenderService;
    @Autowired
    CancelBookingService cancelBookingService;
    @Autowired
    CommentService commentService;


    // User page
    @GetMapping(value = "/")
    public ResponseEntity<?> getUser(@RequestHeader(name ="Authorization") String token) {
        String newToken = token.substring(7);
        User user = getUserFromToken.getUserByUserNameFromJwt(newToken);
        return ResponseEntity.ok().body(user);
    }

    //API booking
    @PostMapping("/booking")
    public ResponseEntity<?> bookingRoom(@RequestBody BookingRequest bookingRequest, @RequestHeader(name ="Authorization") String token) {

        long idRoom = bookingRequest.getIdRoom();
        LocalDate from = bookingRequest.getStart();
        LocalDate to = bookingRequest.getEnd();
        Room room = roomService.findOne(idRoom);
        User user = getUserFromToken.getUserByUserNameFromJwt(token.substring(7));
        dateService.bookRoom(from, to, idRoom, user); // luu vao bang bôking room
        roomService.saveRoom(room);

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");

        // Create the email
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete booking room ");
        mailMessage.setText("Dear Mr/Ms " + user.getUserDetail().getNameUserDetail() + ",\n" +
                "\n" +
                "This email is to confirm your booking on " + formatter.format(date) + " for the room  at the " + room.getHotel().getName() + ". The check-in date shall be on " + from + " and the check-out date shall be on " + to + ".\n" +
                "\n" +
                "Further details of your booking are listed below:\n" +
                "\n" +
                "Number of guests: " + room.getCapacity() + " peoples. " +
                "\n" +
                "Room type: " + room.getType() +
                "\n" +
                "Total: " + (dateService.numberOfDay(to, from)+1) * room.getPrice() + " VND" +
                "\n" +
                "If you have any inquiries, please do not hesitate to contact me or call the hotel directly.\n" +
                "\n" +
                "We are looking forward to your visit and hope that you enjoy your stay.\n" +
                "\n" +
                "Best Regards");

        // Send the email
        emailSenderService.sendEmail(mailMessage);
        return ResponseEntity.ok("Done booking");
    }

    /////////////////////////////////////////////////////////////////////////
    @PostMapping("/book/{idRoom}/{from}/{to}")
    public ResponseEntity<?> booking(@PathVariable("idRoom") long idRoom, @PathVariable("from") String from, @PathVariable("to") String to,@RequestHeader(name ="Authorization") String token) {

        Room room = roomService.findOne(idRoom);
        User user = getUserFromToken.getUserByUserNameFromJwt(token.substring(7));
        dateService.bookRoom1(from, to, idRoom, user); // luu vao bang bôking room

        roomService.saveRoom(room);

        // Create the email
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete booking room ");
        mailMessage.setText("Dear Mr/Ms " + user.getUserDetail().getNameUserDetail() + ",\n" +
                "\n" +
                "This email is to confirm your booking on " + from +" for the room  at the "+ room.getHotel().getName() +". The check-in date shall be on " + from +" and the check-out date shall be on " + to +".\n" +
                "\n" +
                "Further details of your booking are listed below:\n" +
                "\n" +
                "Number of guests: " + room.getCapacity() + " peoples. "+
                "\n" +
                "Room type: " + room.getType() +
                "\n" +
                "Total: " + (dateService.numberOfDay1(to,from) +1)  * room.getPrice()+" VND" +
                "\n" +
                "If you have any inquiries, please do not hesitate to contact me or call the hotel directly.\n" +
                "\n" +
                "We are looking forward to your visit and hope that you enjoy your stay.\n" +
                "\n" +
                "Best Regards");

        // Send the email
        emailSenderService.sendEmail(mailMessage);
        return ResponseEntity.ok("Done booking");
    }



    // API cancel booking

    @GetMapping(value = "/cancelBooking")
    public ResponseEntity<?> cancelBooking(@RequestHeader("Authorization") String token) {
        List<CancelBooking> cancelBookings = cancelBookingService.getCancelByHostId((getUserFromToken.getUserByUserNameFromJwt(token.substring(7))).getId());
        return ResponseEntity.ok().body(cancelBookings);
    }

    @Transactional
    @DeleteMapping(value = "/cancelBooking/{bookingId}")
    public ResponseEntity<?> cancelBooking(@PathVariable("bookingId") Long bookingId, @RequestHeader("Authorization") String token){
        User user = getUserFromToken.getUserByUserNameFromJwt(token.substring(7));
        BookingRoom bookingRoom = dateService.findOneBooking(bookingId);
        Room room = roomService.findOne(bookingRoom.getRoom().getId());

        CancelBooking cancelBooking = new CancelBooking();
        cancelBooking.setRoom(room);
        cancelBooking.setStart(bookingRoom.getStart());
        cancelBooking.setEnd(bookingRoom.getEnd());
        cancelBooking.setHost(user);
        System.out.println("-------------" + cancelBooking.getId());
        cancelBookingService.saveCancelBooking(cancelBooking);
        // Create the email
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete cancel booking room ");
        mailMessage.setText(
                "Dear Mr/Ms " + user.getUserDetail().getNameUserDetail() +",\n"
                        + "You complete cancel the room " + room.getName() + " at the " + room.getHotel().getName() + " from: " + bookingRoom.getStart() + " to: " + bookingRoom.getEnd() +"\n"
                        + "We hope you enjoy when you use my serviece, Thank you!"

        );
        // Send the email
        emailSenderService.sendEmail(mailMessage);

        dateService.huyBooking(bookingId);

        return ResponseEntity.ok("complete Cancel");
    }


    // Thong ke user
    @GetMapping(value = "/history-booking-before")
    public ResponseEntity<?> historyBookingBefore(@RequestHeader("Authorization") String token) {
        List<ThongKeDatPhongUser> historyBooking = dateService.getAllDateBeforeNow((getUserFromToken.getUserByUserNameFromJwt(token.substring(7))).getId());
        return ResponseEntity.ok().body(historyBooking);
    }

    @GetMapping(value = "/history-booking-after")
    public ResponseEntity<?> historyBookingAfter(@RequestHeader("Authorization") String token) {
        List<ThongKeDatPhongUser> historyBooking = dateService.getAllDateAfterNow((getUserFromToken.getUserByUserNameFromJwt(token.substring(7))).getId());
        return ResponseEntity.ok().body(historyBooking);
    }

    @GetMapping(value = "/history-cancel-booking")
    public ResponseEntity<?> historyCancelBooking(@RequestHeader("Authorization") String token) {
        List<ThongKeDatPhongUser> historyBooking = dateService.getAllCancelBooking((getUserFromToken.getUserByUserNameFromJwt(token.substring(7))).getId());
        return ResponseEntity.ok().body(historyBooking);
    }


//    API chức năng comment
    @PostMapping(value ="/comment/{hotelId}/{roomId}/post")
    public  ResponseEntity<?> writeComment(@RequestHeader("Authorization") String token, @RequestParam("commentRequest") String jsonComment, @PathVariable("hotelId")Long hotelId, @PathVariable("roomId") Long roomId){

            String newToken = token.substring(7);
            User user = getUserFromToken.getUserByUserNameFromJwt(newToken);
            boolean check = false;
            List<Long> listRoomBookedByUser = roomService.getAllRoomBookedByUser(user.getId());
            for(Long i : listRoomBookedByUser){
                if(roomId == i){
                    check = true;
                    break;
                }else {
                    check = false;
                }
            }
            if(check) {
                Gson gson = new Gson();
                CommentRequest commentRequest = gson.fromJson(jsonComment, CommentRequest.class);
                Comment comment = new Comment();
                comment.setHotelId(hotelId);
                comment.setRoomId(roomId);
                comment.setMessenger(commentRequest.getMessenger());
                comment.setUserName(user.getUserDetail().getNameUserDetail());
                comment.setUserId(user.getId());
                commentService.saveComment(comment);
                return  ResponseEntity.ok(new MessageResponse("comment successfully"));

            }else {
                return  ResponseEntity.ok(new MessageResponse("Bạn chưa từng đặt qua phòng này"));
            }
    }




}
