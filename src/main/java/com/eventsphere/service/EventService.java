package com.eventsphere.service;

import com.eventsphere.dto.Request.EventReqDto;
import com.eventsphere.dto.Response.ApiResponse;
import com.eventsphere.dto.Response.EventResDto;
import com.eventsphere.entity.Category;

import java.util.List;

public interface EventService {

    EventResDto getEventById(Long eventId);

    List<EventResDto> getAllEventByOrganiser(Long organiserId);

    List<EventResDto> getAllEvents();

    List<EventResDto> getAllEventsByCity(String city);

    List<EventResDto> getAllEventsByCategory(String categoryName);

    ApiResponse createEvent(EventReqDto eventReqDto);

    ApiResponse updateEvent(Long eventId, EventReqDto eventReqDto);

    ApiResponse publishEvent(Long eventId);

    ApiResponse cancelEvent(Long eventId);
}
