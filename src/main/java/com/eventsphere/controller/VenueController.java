package com.eventsphere.controller;

import com.eventsphere.dto.Response.ApiResponse;
import com.eventsphere.entity.Venue;
import com.eventsphere.service.VenueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/venue")
public class VenueController {

    private final VenueService venueService;

    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    @GetMapping("/{venueId}")
    ResponseEntity<Venue> getVenuById(@PathVariable Long venueId) {
        return ResponseEntity.status(HttpStatus.OK).body(venueService.getVenueById(venueId));
    }

    @PostMapping
    ResponseEntity<ApiResponse> createVenue(@RequestBody Venue venue) {
        return ResponseEntity.status(HttpStatus.CREATED).body(venueService.createVenue(venue));
    }

    @PutMapping("/{venueId}")
    ResponseEntity<ApiResponse> updateVenueById(@PathVariable Long venueId, @RequestBody Venue venue) {
        return ResponseEntity.status(HttpStatus.OK).body(venueService.updateVenue(venueId, venue));
    }
}
