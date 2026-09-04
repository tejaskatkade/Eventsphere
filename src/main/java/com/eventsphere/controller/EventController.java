package com.eventsphere.controller;

import com.eventsphere.dto.Request.EventReqDto;
import com.eventsphere.dto.Response.ApiResponse;
import com.eventsphere.dto.Response.EventResDto;
import com.eventsphere.entity.Category;
import com.eventsphere.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/{eventId}")
    ResponseEntity<EventResDto> getEvent(@PathVariable Long eventId) {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getEventById(eventId));
    }

    @GetMapping("/organiser/{organiserId}")
    ResponseEntity<List<EventResDto>> getEventsByOrganiser(@PathVariable Long organiserId) {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getAllEventByOrganiser(organiserId));
    }

    @GetMapping
    ResponseEntity<List<EventResDto>> getAllEvents() {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getAllEvents());
    }

    @GetMapping("/city/{city}")
    ResponseEntity<List<EventResDto>> getEventsByCity(@PathVariable String city) {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getAllEventsByCity(city));
    }

    @GetMapping("/category")
    ResponseEntity<List<EventResDto>> getEventsByCategory(@RequestParam String category) {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getAllEventsByCategory(category));
    }

    @PostMapping
    ResponseEntity<ApiResponse> createEvent(@RequestBody EventReqDto eventReqDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.createEvent(eventReqDto));
    }

    @PutMapping("/{eventId}")
    ResponseEntity<ApiResponse> updateEvent(@PathVariable Long eventId, @RequestBody EventReqDto eventReqDto) {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.updateEvent(eventId, eventReqDto));

    }

    @PutMapping("/publish/{eventId}")
    ResponseEntity<ApiResponse> publishEvent(@PathVariable Long eventId) {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.publishEvent(eventId));

    }

    @PutMapping("/cancel/{eventId}")
    ResponseEntity<ApiResponse> cancelEvent(@PathVariable Long eventId ) {
        return ResponseEntity.status(HttpStatus.OK).body(eventService.cancelEvent(eventId));

    }

}
