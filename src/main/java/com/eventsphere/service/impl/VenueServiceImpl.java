package com.eventsphere.service.impl;

import com.eventsphere.dto.Response.ApiResponse;
import com.eventsphere.entity.Venue;
import com.eventsphere.exception.ResourceNotFoundException;
import com.eventsphere.repository.VenueRepository;
import com.eventsphere.service.VenueService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VenueServiceImpl implements VenueService {

    private final VenueRepository venueRepository;

    public VenueServiceImpl(VenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    @Override
    public ApiResponse createVenue(Venue venue) {
        return new ApiResponse( "Venue added. ID : " + venueRepository.save(venue).getId());
    }

    @Override
    public Venue getVenueById(Long venueId) {
        return venueRepository.findById(venueId).orElseThrow(() -> new ResourceNotFoundException("Venue", venueId));
    }

    @Override
    public ApiResponse updateVenue(Long venueId, Venue newVenue) {

        Venue venue = venueRepository.findById(venueId).orElseThrow(() -> new ResourceNotFoundException("Venue", venueId));

        venue.setName(newVenue.getName());
        venue.setDescription(newVenue.getDescription());
        venue.setAddress(newVenue.getAddress());
        venue.setCity(newVenue.getCity());
        venue.setCountry(newVenue.getCountry());
        venue.setState(newVenue.getState());
        venue.setPostalCode(newVenue.getPostalCode());
        venue.setLatitude(newVenue.getLatitude());
        venue.setLongitude(newVenue.getLongitude());

        venueRepository.save(venue);

        return new ApiResponse("Venue udpated successfully");
    }
}
