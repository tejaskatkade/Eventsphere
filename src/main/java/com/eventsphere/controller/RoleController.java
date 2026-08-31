package com.eventsphere.controller;

import com.eventsphere.dto.Request.RoleReqDto;
import com.eventsphere.dto.Response.ApiResponse;
import com.eventsphere.dto.Response.RoleResDto;
import com.eventsphere.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/role")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    ResponseEntity<?> createRole(@RequestBody RoleReqDto roleReqDto) {
        return  ResponseEntity.status(HttpStatus.CREATED).body(roleService.insertRole(roleReqDto));
    }

    @GetMapping
    ResponseEntity<?> getAllRoles() {
        return ResponseEntity.status(HttpStatus.OK).body(roleService.getAllRoles());
    }
}
