package com.eventsphere.service.impl;

import com.eventsphere.dto.Request.EventReqDto;
import com.eventsphere.dto.Response.ApiResponse;
import com.eventsphere.dto.Response.EventResDto;
import com.eventsphere.entity.Category;
import com.eventsphere.entity.Event;
import com.eventsphere.entity.EventStatus;
import com.eventsphere.entity.Organiser;
import com.eventsphere.exception.ResourceNotFoundException;
import com.eventsphere.repository.CategoryRepository;
import com.eventsphere.repository.EventRepository;
import com.eventsphere.repository.OrganiserRepository;
import com.eventsphere.service.EventService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    private final OrganiserRepository organiserRepository;

    private final CategoryRepository categoryRepository;

    private final ModelMapper modelMapper;

    public EventServiceImpl(
            EventRepository eventRepository,
            OrganiserRepository organiserRepository,
            CategoryRepository categoryRepository,
            ModelMapper modelMapper
    ) {
        this.eventRepository = eventRepository;
        this.organiserRepository = organiserRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public EventResDto getEventById(Long eventId) {
        return modelMapper.map(findEventById(eventId), EventResDto.class);
    }

    @Override
    public List<EventResDto> getAllEventByOrganiser(Long organiserId) {
        return eventRepository
                .findAllByOrganiser(findOrganiserById(organiserId))
                .stream()
                .map(
                        event -> modelMapper.map(event, EventResDto.class)
                )
                .toList();

    }

    @Override
    public ApiResponse createEvent(EventReqDto eventReqDto) {
        Event event = modelMapper.map(eventReqDto, Event.class);
        event.setCategory(findCategoryById(eventReqDto.getCategoryId()));
        event.setOrganiser(findOrganiserById(eventReqDto.getOrganiserId()));
        event.setStatus(EventStatus.DRAFT);

        eventRepository.save(event);
        return new ApiResponse("Created new event. ID : " + event.getId());
    }

    @Override
    public ApiResponse updateEvent(Long eventId, EventReqDto eventReqDto) {
        Event event = findEventById(eventId);
        event.setCategory(findCategoryById(eventReqDto.getCategoryId()));
        event.setOrganiser(findOrganiserById(eventReqDto.getCategoryId()));
        event.setTitle(eventReqDto.getTitle());
        event.setDescription(eventReqDto.getDescription());
        event.setLanguage(eventReqDto.getLanguage());
        event.setDurationMinutes(eventReqDto.getDurationMinutes());
        event.setMinimumAge(eventReqDto.getMinimumAge());
        event.setBannerUrl(eventReqDto.getBannerUrl());
//        event.setStatus(EventStatus.DRAFT);

        eventRepository.save(event);
        return new ApiResponse("Update event. ID : " + event.getId());
    }

    @Override
    public ApiResponse publishEvent(Long eventId) {
        findEventById(eventId).setStatus(EventStatus.PUBLISHED);
        return new ApiResponse("Successfully PUBLISHED the event. Id : " + eventId);
    }

    @Override
    public ApiResponse cancelEvent(Long eventId) {
        findEventById(eventId).setStatus(EventStatus.CANCELLED);
        return new ApiResponse("Successfully CANCELLED the event. Id : " + eventId);
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
                .findById(eventId)
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
