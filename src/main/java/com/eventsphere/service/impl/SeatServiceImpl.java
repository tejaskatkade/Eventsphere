package com.eventsphere.service.impl;

import com.eventsphere.dto.Request.SeatReqDto;
import com.eventsphere.dto.Response.ApiResponse;
import com.eventsphere.dto.Response.SeatResDto;
import com.eventsphere.entity.Hall;
import com.eventsphere.entity.Seat;
import com.eventsphere.exception.ConflictException;
import com.eventsphere.exception.ResourceNotFoundException;
import com.eventsphere.repository.HallRepository;
import com.eventsphere.repository.SeatRepository;
import com.eventsphere.repository.TicketRepository;
import com.eventsphere.service.SeatService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;

    private final HallRepository hallRepository;

    private final TicketRepository ticketRepository;

    private final ModelMapper modelMapper;

    public SeatServiceImpl(SeatRepository seatRepository, HallRepository hallRepository, TicketRepository ticketRepository, ModelMapper modelMapper) {
        this.seatRepository = seatRepository;
        this.hallRepository = hallRepository;
        this.ticketRepository = ticketRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public SeatResDto getSeatById(Long seatId) {
        log.debug("Fetching seat details for ID: {}", seatId);
        Seat seat = findSeatById(seatId);
        SeatResDto dto = modelMapper.map(seat, SeatResDto.class);
        dto.setId(seat.getId());
        return dto;
    }

    @Override
    public List<SeatResDto> getSeatsOfHall(Long hallId) {
        log.debug("Fetching seats for Hall ID: {}", hallId);
        return seatRepository.findAllByHall(getHallById(hallId))
                .stream()
                .map(seat -> {
                    SeatResDto dto = modelMapper.map(seat, SeatResDto.class);
                    dto.setId(seat.getId());
                    return dto;
                })
                .toList();
    }

    @Override
    public ApiResponse createSeat(SeatReqDto seatReqDto) {
        log.info("Creating seat in Hall ID: {}, Row: {}, SeatNumber: {}",
                seatReqDto.getHallId(), seatReqDto.getRowName(), seatReqDto.getSeatNumber());
        Seat seat = modelMapper.map(seatReqDto, Seat.class);
        seat.setHall(getHallById(seatReqDto.getHallId()));
        if (seat.getIsActive() == null) {
            seat.setIsActive(true);
        }
        seatRepository.save(seat);
        log.info("Seat created with ID: {}", seat.getId());
        return new ApiResponse("Seat created. Id : " + seat.getId());
    }

    @Override
    public ApiResponse toggleSeatStatus(Long seatId) {
        Seat seat = findSeatById(seatId);
        boolean newStatus = !Boolean.TRUE.equals(seat.getIsActive());
        seat.setIsActive(newStatus);
        seatRepository.save(seat);
        log.info("Seat ID: {} active status toggled to {}", seatId, newStatus);
        return new ApiResponse("Seat ID " + seatId + " status updated to " + (newStatus ? "ACTIVE" : "INACTIVE"));
    }

    @Override
    public ApiResponse updateSeat(Long seatId, SeatReqDto seatReqDto) {
        log.info("Updating seat ID: {}", seatId);
        Seat seat = findSeatById(seatId);
        if (seatReqDto.getHallId() != null) {
            seat.setHall(getHallById(seatReqDto.getHallId()));
        }
        seat.setRowName(seatReqDto.getRowName());
        seat.setSeatNumber(seatReqDto.getSeatNumber());
        seat.setSeatType(seatReqDto.getSeatType());
        if (seatReqDto.getIsActive() != null) {
            seat.setIsActive(seatReqDto.getIsActive());
        }
        seatRepository.save(seat);
        log.info("Seat ID: {} successfully updated", seatId);
        return new ApiResponse("Seat updated successfully. ID: " + seatId);
    }

    @Override
    public ApiResponse deleteSeat(Long seatId) {
        log.info("Attempting to delete seat ID: {}", seatId);
        Seat seat = findSeatById(seatId);
        if (Boolean.TRUE.equals(ticketRepository.existsBySeat(seat))) {
            log.warn("Cannot delete seat ID: {} because it has associated booking tickets", seatId);
            throw new ConflictException("Cannot delete seat ID: " + seatId + " because existing booking tickets are associated with it. Consider deactivating it instead.");
        }
        seatRepository.delete(seat);
        log.info("Seat ID: {} deleted successfully", seatId);
        return new ApiResponse("Seat deleted successfully. ID: " + seatId);
    }

    private Seat findSeatById(Long seatId) {
        return seatRepository.findById(seatId)
                .orElseThrow(() -> new ResourceNotFoundException("Seat", seatId));
    }

    private Hall getHallById(Long hallId) {
        return hallRepository.findById(hallId)
                .orElseThrow(() -> new ResourceNotFoundException("Hall", hallId));
    }
}
