package com.eventsphere.controller;

import com.eventsphere.dto.Request.HallReqDto;
import com.eventsphere.dto.Response.ApiResponse;
import com.eventsphere.dto.Response.HallResDto;
import com.eventsphere.service.HallService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hall")
public class HallController {

    private final HallService hallService;

    public HallController(HallService hallService) {
        this.hallService = hallService;
    }

    @GetMapping("/{hallId}")
    ResponseEntity<HallResDto> getHallById(@PathVariable Long hallId) {
        return ResponseEntity.status(HttpStatus.OK).body(hallService.getHallById(hallId));
    }

    @GetMapping("/venue/{venueId}")
    ResponseEntity<List<HallResDto>> getHallsByVenue(@PathVariable Long venueId) {
        return ResponseEntity.status(HttpStatus.OK).body(hallService.getHallsOfVenue(venueId));
    }

    @PostMapping
    ResponseEntity<ApiResponse> createHall(@RequestBody HallReqDto hallReqDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(hallService.createHall(hallReqDto));
    }

    @PutMapping("/{hallId}")
    ResponseEntity<ApiResponse> updateHall(@PathVariable Long hallId, @RequestBody HallReqDto hallReqDto) {
        return ResponseEntity.status(HttpStatus.OK).body(hallService.updateHallById(hallId,hallReqDto));
    }
}
