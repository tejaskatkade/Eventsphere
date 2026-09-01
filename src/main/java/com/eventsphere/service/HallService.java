package com.eventsphere.service;

import com.eventsphere.dto.Request.HallReqDto;
import com.eventsphere.dto.Response.ApiResponse;
import com.eventsphere.dto.Response.HallResDto;

import java.util.List;

public interface HallService {

    HallResDto getHallById(Long hallId);

    ApiResponse updateHallById(Long hallId, HallReqDto hallReqDto);

    List<HallResDto> getHallsOfVenue(Long venueId);

    ApiResponse createHall(HallReqDto hallReqDto);
}
