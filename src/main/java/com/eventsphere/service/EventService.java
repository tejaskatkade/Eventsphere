package com.eventsphere.service;

import com.eventsphere.dto.Request.EventReqDto;
import com.eventsphere.dto.Response.ApiResponse;
import com.eventsphere.dto.Response.EventResDto;

import java.util.List;

public interface EventService {

    EventResDto getEventById(Long eventId);

    List<EventResDto> getAllEventByOrganiser(Long organiserId);

    ApiResponse createEvent(EventReqDto eventReqDto);

    ApiResponse updateEvent(Long eventId, EventReqDto eventReqDto);

    ApiResponse publishEvent(Long eventId);

    ApiResponse cancelEvent(Long eventId);
}
