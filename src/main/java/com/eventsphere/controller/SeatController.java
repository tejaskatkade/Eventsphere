package com.eventsphere.controller;

import com.eventsphere.dto.Request.SeatReqDto;
import com.eventsphere.dto.Response.ApiResponse;
import com.eventsphere.dto.Response.SeatResDto;
import com.eventsphere.service.SeatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seat")
public class SeatController {

    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @GetMapping("/{seatId}")
    ResponseEntity<SeatResDto> getSeatById(@PathVariable Long seatId) {
        return ResponseEntity.status(HttpStatus.OK).body(seatService.getSeatById(seatId));
    }

    @GetMapping("/hall/{hallId}")
    ResponseEntity<List<SeatResDto>> getSeatsOfHall(@PathVariable Long hallId) {
        return ResponseEntity.status(HttpStatus.OK).body(seatService.getSeatsOfHall(hallId));
    }

    @PostMapping
    ResponseEntity<ApiResponse> createSeat(@RequestBody SeatReqDto seatReqDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(seatService.createSeat(seatReqDto));
    }
}
