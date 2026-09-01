package com.eventsphere.service;

import com.eventsphere.dto.Request.OrganiserReqDto;
import com.eventsphere.dto.Response.ApiResponse;
import com.eventsphere.dto.Response.OrganiserResDto;

public interface OrganiserService {

    ApiResponse createOrganiser(Long memberId, OrganiserReqDto organiserReqDto);

    OrganiserResDto getOrganiserById(Long organiserId);
}
