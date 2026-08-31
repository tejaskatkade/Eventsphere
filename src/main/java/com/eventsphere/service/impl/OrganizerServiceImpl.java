package com.eventsphere.service.impl;

import com.eventsphere.dto.Request.OrganizerReqDto;
import com.eventsphere.dto.Response.ApiResponse;
import com.eventsphere.dto.Response.OrganizerResDto;
import com.eventsphere.entity.Member;
import com.eventsphere.entity.Organizer;
import com.eventsphere.exception.ResourceNotFoundException;
import com.eventsphere.repository.MemberRepository;
import com.eventsphere.repository.OrganizerRepository;
import com.eventsphere.service.OrganizerService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class OrganizerServiceImpl implements OrganizerService {

    private final OrganizerRepository organizerRepository;

    private final MemberRepository memberRepository;

    private final ModelMapper modelMapper;

    public OrganizerServiceImpl(OrganizerRepository organizerRepository, MemberRepository memberRepository, ModelMapper modelMapper) {
        this.organizerRepository = organizerRepository;
        this.memberRepository = memberRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ApiResponse createOrganizer(Long memberId, OrganizerReqDto organizerReqDto) {
        Organizer organizer = modelMapper.map(organizerReqDto, Organizer.class);
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new ResourceNotFoundException("Member", memberId));
        organizer.setMember(member);
        organizerRepository.save(organizer);
        return new ApiResponse("Created Organizer");
    }

    @Override
    public OrganizerResDto getOrganizerById(Long organizerId) {
        Organizer organizer = organizerRepository.findById(organizerId).orElseThrow(() -> new ResourceNotFoundException("Organizer", organizerId));
        return modelMapper.map(organizer, OrganizerResDto.class);
    }
}

