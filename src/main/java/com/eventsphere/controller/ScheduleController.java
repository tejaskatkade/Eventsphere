package com.eventsphere.controller;

import com.eventsphere.dto.Request.EventScheduleReqDto;
import com.eventsphere.entity.ScheduleStatus;
import com.eventsphere.service.ScheduleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<?> getScheduleById(@PathVariable Long scheduleId) {
        return ResponseEntity.ok(scheduleService.getEventSchedule(scheduleId));
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<?> getSchedulesByEventId(@PathVariable Long eventId) {
        return ResponseEntity.ok(scheduleService.getSchedulesOfEvent(eventId));
    }

    @PostMapping
    public ResponseEntity<?> createSchedule(@RequestBody @Valid EventScheduleReqDto scheduleReqDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleService.createSchedule(scheduleReqDto));
    }

    @PutMapping("/{scheduleId}")
    public ResponseEntity<?> updateSchedule(@PathVariable Long scheduleId, @RequestBody @Valid EventScheduleReqDto scheduleReqDto) {
        return ResponseEntity.ok(scheduleService.updateSchedule(scheduleId, scheduleReqDto));
    }

    @PutMapping("/{scheduleId}/status")
    public ResponseEntity<?> updateScheduleStatus(@PathVariable Long scheduleId, @RequestParam ScheduleStatus status) {
        return ResponseEntity.ok(scheduleService.updateScheduleStatus(scheduleId, status));
    }
}
