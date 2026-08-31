package com.eventsphere.service;

import com.eventsphere.dto.Request.OrganizerReqDto;
import com.eventsphere.dto.Response.ApiResponse;
import com.eventsphere.dto.Response.OrganizerResDto;

public interface OrganizerService {

    ApiResponse createOrganizer(Long memberId, OrganizerReqDto organizerReqDto);

    OrganizerResDto getOrganizerById(Long organizerId);
}
