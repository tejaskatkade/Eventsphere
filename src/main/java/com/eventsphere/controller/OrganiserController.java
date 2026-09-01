package com.eventsphere.controller;

import com.eventsphere.dto.Request.OrganiserReqDto;
import com.eventsphere.service.OrganiserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/organiser")
public class OrganiserController {

    private final OrganiserService organiserService;

    public OrganiserController(OrganiserService organiserService) {
        this.organiserService = organiserService;
    }

    @GetMapping("/{organiserId}")
    ResponseEntity<?> fetchOrganiserById(@PathVariable Long organiserId) {
        return ResponseEntity.status(HttpStatus.OK).body(organiserService.getOrganiserById(organiserId));
    }

    @PostMapping("/member/{memberId}")
    ResponseEntity<?> createOrganiser(@PathVariable Long memberId, @RequestBody OrganiserReqDto organiserReqDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(organiserService.createOrganiser(memberId, organiserReqDto));
    }
}
