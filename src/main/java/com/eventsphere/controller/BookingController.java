package com.eventsphere.controller;

import com.eventsphere.dto.Request.BookingReqDto;
import com.eventsphere.dto.Response.ApiResponse;
import com.eventsphere.dto.Response.BookingResDto;
import com.eventsphere.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/booking")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingResDto> getBookingById(@PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.getBookingById(bookingId));
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<?> getBookingsByMember(@PathVariable Long memberId) {
        return ResponseEntity.ok(bookingService.getBookingsByMember(memberId));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createBooking(@RequestBody @Valid BookingReqDto bookingReqDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.bookEvent(bookingReqDto));
    }
}
