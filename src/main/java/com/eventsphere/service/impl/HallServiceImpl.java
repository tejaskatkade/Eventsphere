package com.eventsphere.service.impl;

import com.eventsphere.dto.Request.HallReqDto;
import com.eventsphere.dto.Response.ApiResponse;
import com.eventsphere.dto.Response.HallResDto;
import com.eventsphere.entity.Hall;
import com.eventsphere.entity.Venue;
import com.eventsphere.exception.ResourceNotFoundException;
import com.eventsphere.repository.HallRepository;
import com.eventsphere.repository.VenueRepository;
import com.eventsphere.service.HallService;
import jakarta.validation.constraints.Null;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class HallServiceImpl implements HallService {

    private final HallRepository hallRepository;

    private final VenueRepository venueRepository;

    private final ModelMapper modelMapper;

    public HallServiceImpl(HallRepository hallRepository, VenueRepository venueRepository, ModelMapper modelMapper) {
        this.hallRepository = hallRepository;
        this.venueRepository = venueRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public HallResDto getHallById(Long hallId) {
        return modelMapper
                .map(hallRepository.findById(hallId)
                        .orElseThrow(
                                () -> new ResourceNotFoundException("Hall", hallId)
                        ),
                        HallResDto.class
                );
    }

    @Override
    public ApiResponse updateHallById(Long hallId, HallReqDto hallReqDto) {
        Hall hall = hallRepository.findById(hallId).orElseThrow(() -> new ResourceNotFoundException("Hall", hallId));

        hall.setName(hallReqDto.getName());
        hall.setCapacity(hallReqDto.getCapacity());

        hall.setVenue(getVenueById(hallReqDto.getVenueId()));

        hallRepository.save(hall);
        return new ApiResponse("Hall Updated successfully");
    }

    @Override
    public List<HallResDto> getHallsOfVenue(Long venueId) {
        return hallRepository.findAllByVenue(getVenueById(venueId))
                .stream()
                .map((hall) -> modelMapper.map(hall, HallResDto.class))
                .toList();
    }

    @Override
    public ApiResponse createHall(HallReqDto hallReqDto) {
        Hall hall = modelMapper.map(hallReqDto, Hall.class);
        hall.setVenue(getVenueById(hallReqDto.getVenueId()));
        hallRepository.save(hall);
        return new ApiResponse("Hall created successfully. ID : " + hall.getId());
    }

    private Venue getVenueById(Long venueId) {
        return venueRepository
                .findById(venueId)
                .orElseThrow(() -> new ResourceNotFoundException("Venue", venueId));
    }
}
