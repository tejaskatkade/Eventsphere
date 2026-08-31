package com.eventsphere.service;

import com.eventsphere.dto.Request.RoleReqDto;
import com.eventsphere.dto.Response.ApiResponse;
import com.eventsphere.dto.Response.RoleResDto;
import com.eventsphere.entity.Role;
import jakarta.transaction.Transactional;

import java.util.List;

@Transactional
public interface RoleService {
    ApiResponse insertRole(RoleReqDto role);

    List<RoleResDto> getAllRoles();
}
