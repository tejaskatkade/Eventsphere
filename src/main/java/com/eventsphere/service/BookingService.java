package com.eventsphere.service;

import com.eventsphere.dto.Request.BookingReqDto;
import com.eventsphere.dto.Response.ApiResponse;
import com.eventsphere.dto.Response.BookingResDto;

import java.util.List;

public interface BookingService {

    ApiResponse bookEvent(BookingReqDto bookingReqDto);

    BookingResDto getBookingById(Long bookingId);

    List<BookingResDto> getBookingsByMember(Long memberId);

}
