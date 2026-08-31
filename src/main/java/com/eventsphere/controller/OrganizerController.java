package com.eventsphere.controller;

import com.eventsphere.dto.Request.OrganizerReqDto;
import com.eventsphere.service.OrganizerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/organizer")
public class OrganizerController {

    private final OrganizerService organizerService;

    public OrganizerController(OrganizerService organizerService) {
        this.organizerService = organizerService;
    }

    @GetMapping("/{organizerId}")
    ResponseEntity<?> fetchOrganizerById(@PathVariable Long organizerId) {
        return ResponseEntity.status(HttpStatus.OK).body(organizerService.getOrganizerById(organizerId));
    }

    @PostMapping("/member/{memberId}")
    ResponseEntity<?> createOrganizer(@PathVariable Long memberId, @RequestBody OrganizerReqDto organizerReqDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(organizerService.createOrganizer(memberId, organizerReqDto));
    }
}
