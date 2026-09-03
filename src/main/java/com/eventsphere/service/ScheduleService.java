package com.eventsphere.service;

import com.eventsphere.dto.Request.EventScheduleReqDto;
import com.eventsphere.dto.Response.ApiResponse;
import com.eventsphere.dto.Response.EventScheduleResDto;
import com.eventsphere.entity.ScheduleStatus;

import java.util.List;

public interface ScheduleService {

    ApiResponse createSchedule(EventScheduleReqDto scheduleReqDto);

    EventScheduleResDto getEventSchedule(Long scheduleId);

    List<EventScheduleResDto> getSchedulesOfEvent(Long eventId);

    ApiResponse updateSchedule(Long scheduleId, EventScheduleReqDto scheduleReqDto);

    ApiResponse updateScheduleStatus(Long scheduleId, ScheduleStatus status);
}
