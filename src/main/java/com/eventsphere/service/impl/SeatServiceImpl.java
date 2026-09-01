package com.eventsphere.service.impl;

import com.eventsphere.dto.Request.SeatReqDto;
import com.eventsphere.dto.Response.ApiResponse;
import com.eventsphere.dto.Response.SeatResDto;
import com.eventsphere.entity.Hall;
import com.eventsphere.entity.Seat;
import com.eventsphere.exception.ResourceNotFoundException;
import com.eventsphere.repository.HallRepository;
import com.eventsphere.repository.SeatRepository;
import com.eventsphere.service.SeatService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;

    private final HallRepository hallRepository;

    private final ModelMapper modelMapper;

    public SeatServiceImpl(SeatRepository seatRepository, HallRepository hallRepository, ModelMapper modelMapper) {
        this.seatRepository = seatRepository;
        this.hallRepository = hallRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public SeatResDto getSeatById(Long seatId) {
        return modelMapper
                .map(seatRepository
                .findById(seatId)
                .orElseThrow(() -> new ResourceNotFoundException("Seat", seatId)),
                        SeatResDto.class);
    }

    @Override
    public List<SeatResDto> getSeatsOfHall(Long hallId) {
        return seatRepository.findAllByHall(getHallById(hallId))
                .stream()
                .map(
                        seat -> modelMapper.map(seat,SeatResDto.class)
                )
                .toList();
    }

    @Override
    public ApiResponse createSeat(SeatReqDto seatReqDto) {
        Seat seat = modelMapper.map(seatReqDto, Seat.class);
        seat.setHall(getHallById(seatReqDto.getHallId()));
        seatRepository.save(seat);
        return new ApiResponse("Seat created. Id : " + seat.getId());
    }

    private Hall getHallById(Long hallId) {
        return hallRepository.findById(hallId).orElseThrow(() -> new ResourceNotFoundException("Hall", hallId));
    }
}
