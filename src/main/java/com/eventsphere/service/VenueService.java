package com.eventsphere.service;

import com.eventsphere.dto.Response.ApiResponse;
import com.eventsphere.entity.Venue;

import java.util.List;

public interface VenueService {

    ApiResponse createVenue(Venue venue);

    Venue getVenueById(Long venueId);

    ApiResponse updateVenue(Long venueId, Venue venue);
}
