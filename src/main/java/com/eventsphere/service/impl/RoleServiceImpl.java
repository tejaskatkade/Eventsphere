package com.eventsphere.service.impl;

import com.eventsphere.dto.Request.RoleReqDto;
import com.eventsphere.dto.Response.ApiResponse;
import com.eventsphere.dto.Response.RoleResDto;
import com.eventsphere.entity.Role;
import com.eventsphere.exception.ApiException;
import com.eventsphere.repository.RoleRepository;
import com.eventsphere.service.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    private final ModelMapper modelMapper;

    public RoleServiceImpl(RoleRepository roleRepository, ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ApiResponse insertRole(RoleReqDto roleReqDto) {
        if (roleRepository.existsByName(roleReqDto.getName())) {
            throw new ApiException("Role with name : " + roleReqDto.getName() +" already exists ");
        }
         roleRepository.save(modelMapper.map(roleReqDto, Role.class));
         return new ApiResponse("Role with name : " + roleReqDto.getName() + "added successfully !!!");
    }

    @Override
    public List<RoleResDto> getAllRoles() {
        return roleRepository.findAll().stream().map( (role) ->
                modelMapper.map(role, RoleResDto.class)
        ).toList();
    }
}
