package com.eventsphere.service.impl;

import com.eventsphere.dto.Request.OrganiserReqDto;
import com.eventsphere.dto.Response.ApiResponse;
import com.eventsphere.dto.Response.OrganiserResDto;
import com.eventsphere.entity.Member;
import com.eventsphere.entity.Organiser;
import com.eventsphere.exception.ResourceNotFoundException;
import com.eventsphere.repository.MemberRepository;
import com.eventsphere.repository.OrganiserRepository;
import com.eventsphere.repository.RoleRepository;
import com.eventsphere.service.OrganiserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrganiserServiceImpl implements OrganiserService {

    private final OrganiserRepository organiserRepository;

    private final MemberRepository memberRepository;

    private final RoleRepository roleRepository;

    private final ModelMapper modelMapper;

    public OrganiserServiceImpl(OrganiserRepository organiserRepository, MemberRepository memberRepository, RoleRepository roleRepository, ModelMapper modelMapper) {
        this.organiserRepository = organiserRepository;
        this.memberRepository = memberRepository;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ApiResponse createOrganiser(Long memberId, OrganiserReqDto organiserReqDto) {
        Organiser organiser = modelMapper.map(organiserReqDto, Organiser.class);
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new ResourceNotFoundException("Member", memberId));
        organiser.setMember(member);
        member.addRole(roleRepository.findByName("ORGANISER").orElseThrow(() -> new ResourceNotFoundException("ORGANISER role")));
        organiserRepository.save(organiser);
        return new ApiResponse("Created Organiser");
    }

    @Override
    public OrganiserResDto getOrganiserById(Long organiserId) {
        Organiser organiser = organiserRepository.findById(organiserId).orElseThrow(() -> new ResourceNotFoundException("Organiser", organiserId));
        OrganiserResDto organiserResDto = modelMapper.map(organiser, OrganiserResDto.class);
        organiserResDto.setMemberId(organiser.getMember().getId());
        return organiserResDto;
    }
}

