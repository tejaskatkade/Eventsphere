package com.eventsphere.service;

import com.eventsphere.dto.Request.SeatReqDto;
import com.eventsphere.dto.Response.ApiResponse;
import com.eventsphere.dto.Response.SeatResDto;

import java.util.List;

public interface SeatService {

    SeatResDto getSeatById(Long seatId);

    List<SeatResDto> getSeatsOfHall(Long hallId);

    ApiResponse createSeat(SeatReqDto seatReqDto);

    ApiResponse toggleSeatStatus(Long seatId);

    ApiResponse updateSeat(Long seatId, SeatReqDto seatReqDto);

    ApiResponse deleteSeat(Long seatId);
}
