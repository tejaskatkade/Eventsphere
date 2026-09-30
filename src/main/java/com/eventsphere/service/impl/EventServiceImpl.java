package com.eventsphere.service.impl;

import com.eventsphere.dto.Request.EventReqDto;
import com.eventsphere.dto.Response.ApiResponse;
import com.eventsphere.dto.Response.EventResDto;
import com.eventsphere.dto.Response.EventScheduleResDto;
import com.eventsphere.entity.*;
import com.eventsphere.exception.ResourceNotFoundException;
import com.eventsphere.repository.*;
import com.eventsphere.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    private final OrganiserRepository organiserRepository;

    private final CategoryRepository categoryRepository;

    private final VenueRepository venueRepository;

    private final HallRepository hallRepository;

    private final ScheduleRepository eventScheduleRepository;

    private final ModelMapper modelMapper;

    public EventServiceImpl(
            EventRepository eventRepository,
            OrganiserRepository organiserRepository,
            CategoryRepository categoryRepository,
            VenueRepository venueRepository,
            HallRepository hallRepository,
            ScheduleRepository eventScheduleRepository,
            ModelMapper modelMapper
    ) {
        this.eventRepository = eventRepository;
        this.organiserRepository = organiserRepository;
        this.categoryRepository = categoryRepository;
        this.venueRepository = venueRepository;
        this.hallRepository = hallRepository;
        this.eventScheduleRepository = eventScheduleRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public EventResDto getEventById(Long eventId) {
        return mapEventToDto(findEventById(eventId));
    }

    @Override
    public List<EventResDto> getAllEventByOrganiser(Long organiserId) {
        return eventRepository
                .findAllByOrganiserIdWithDetails(organiserId)
                .stream()
                .map(this::mapEventToDto)
                .toList();

    }

    @Override
    public List<EventResDto> getAllEvents() {
        return eventRepository
                .findAllWithDetails()
                .stream()
                .map(this::mapEventToDto)
                .toList();
    }

    @Override
    public List<EventResDto> getAllEventsByCity(String city) {
        return eventRepository
                .findAllByCity(city)
                .stream()
                .map(this::mapEventToDto)
                .toList();
    }

    @Override
    public List<EventResDto> getAllEventsByCategory(String categoryName) {
        return eventRepository
                .findAllByCategoryNameWithDetails(categoryName)
                .stream()
                .map(this::mapEventToDto)
                .toList();
    }

    @Override
    public ApiResponse createEvent(EventReqDto eventReqDto) {
        log.info("Creating new event with title: '{}', categoryId: {}", eventReqDto.getTitle(), eventReqDto.getCategoryId());
        Event event = modelMapper.map(eventReqDto, Event.class);
        event.setCategory(findCategoryById(eventReqDto.getCategoryId()));
        event.setOrganiser(findOrganiserById(eventReqDto.getOrganiserId()));
        event.setStatus(EventStatus.DRAFT);

        eventRepository.save(event);
        log.info("Event created successfully with ID: {}", event.getId());
        return new ApiResponse("Created new event. ID : " + event.getId());
    }

    @Override
    public ApiResponse updateEvent(Long eventId, EventReqDto eventReqDto) {
        log.info("Updating event ID: {}", eventId);
        Event event = findEventById(eventId);
        event.setCategory(findCategoryById(eventReqDto.getCategoryId()));
        event.setOrganiser(findOrganiserById(eventReqDto.getOrganiserId()));
        event.setTitle(eventReqDto.getTitle());
        event.setDescription(eventReqDto.getDescription());
        event.setLanguage(eventReqDto.getLanguage());
        event.setDurationMinutes(eventReqDto.getDurationMinutes());
        event.setMinimumAge(eventReqDto.getMinimumAge());
        event.setBannerUrl(eventReqDto.getBannerUrl());

        eventRepository.save(event);
        log.info("Event ID: {} updated successfully", event.getId());
        return new ApiResponse("Update event. ID : " + event.getId());
    }

    @Override
    public ApiResponse publishEvent(Long eventId) {
        log.info("Publishing event ID: {}", eventId);
        findEventById(eventId).setStatus(EventStatus.PUBLISHED);
        log.info("Event ID: {} is now PUBLISHED", eventId);
        return new ApiResponse("Successfully PUBLISHED the event. Id : " + eventId);
    }

    @Override
    public ApiResponse cancelEvent(Long eventId) {
        log.info("Cancelling event ID: {}", eventId);
        findEventById(eventId).setStatus(EventStatus.CANCELLED);
        log.info("Event ID: {} is now CANCELLED", eventId);
        return new ApiResponse("Successfully CANCELLED the event. Id : " + eventId);
    }

    private EventResDto mapEventToDto(Event event) {
        EventResDto eventResDto = modelMapper.map(event, EventResDto.class);
        eventResDto.setId(event.getId());
        if (event.getCategory() != null) {
            eventResDto.setCategoryId(event.getCategory().getId());
            eventResDto.setCategoryName(event.getCategory().getName());
        }
        if (event.getOrganiser() != null) {
            eventResDto.setOrganiserId(event.getOrganiser().getId());
        }
        if (event.getEventSchedules() != null) {
            eventResDto.setEventSchedule(event.getEventSchedules().stream().map(
                    schedule -> {
                        EventScheduleResDto eventScheduleResDto = modelMapper.map(schedule, EventScheduleResDto.class);
                        eventScheduleResDto.setId(schedule.getId());
                        eventScheduleResDto.setEventId(event.getId());
                        if (schedule.getHall() != null) {
                            eventScheduleResDto.setHallId(schedule.getHall().getId());
                            eventScheduleResDto.setHallName(schedule.getHall().getName());
                            if (schedule.getHall().getVenue() != null) {
                                eventScheduleResDto.setVenueName(schedule.getHall().getVenue().getName());
                            }
                        }
                        return eventScheduleResDto;
                    }
            ).toList());
        }
        return eventResDto;
    }

    private Category findCategoryById(Long categoryId) {
        return categoryRepository
                .findById(categoryId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Category", categoryId)
                );
    }

    private Event findEventById(Long eventId) {
        return eventRepository
                .findByIdWithDetails(eventId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Event", eventId)
                );
    }

    private Organiser findOrganiserById(Long organiserId) {
        return organiserRepository
                .findById(organiserId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Organiser", organiserId)
                );
    }
}
