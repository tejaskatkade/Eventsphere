package com.eventsphere.service.impl;

import com.eventsphere.dto.Request.EventScheduleReqDto;
import com.eventsphere.dto.Response.ApiResponse;
import com.eventsphere.dto.Response.EventScheduleResDto;
import com.eventsphere.entity.Event;
import com.eventsphere.entity.EventSchedule;
import com.eventsphere.entity.Hall;
import com.eventsphere.entity.ScheduleStatus;
import com.eventsphere.exception.ResourceNotFoundException;
import com.eventsphere.repository.EventRepository;
import com.eventsphere.repository.HallRepository;
import com.eventsphere.repository.ScheduleRepository;
import com.eventsphere.service.ScheduleService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;

    private final EventRepository eventRepository;

    private final HallRepository hallRepository;

    private final ModelMapper modelMapper;

    public ScheduleServiceImpl(
            ScheduleRepository scheduleRepository,
            EventRepository eventRepository,
            HallRepository hallRepository,
            ModelMapper modelMapper
    ) {
        this.scheduleRepository = scheduleRepository;
        this.eventRepository = eventRepository;
        this.hallRepository = hallRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ApiResponse createSchedule(EventScheduleReqDto scheduleReqDto) {
        EventSchedule eventSchedule = modelMapper.map(scheduleReqDto, EventSchedule.class);
        eventSchedule.setEvent(findEventById(scheduleReqDto.getEventId()));
        eventSchedule.setHall(findHallById(scheduleReqDto.getHallId()));

        scheduleRepository.save(eventSchedule);
        return new ApiResponse("Create schedule successfully. Id : " + eventSchedule.getId());
    }

    @Override
    public EventScheduleResDto getEventSchedule(Long scheduleId) {
        return modelMapper.map(findScheduleById(scheduleId), EventScheduleResDto.class);

    }

    @Override
    public List<EventScheduleResDto> getSchedulesOfEvent(Long eventId) {
        return scheduleRepository
                .findAllByEvent(findEventById(eventId))
                .stream()
                .map(
                        schedule -> modelMapper
                                .map(
                                        schedule,
                                        EventScheduleResDto.class
                                )
                )
                .toList();
    }

    @Override
    public ApiResponse updateSchedule(Long scheduleId, EventScheduleReqDto scheduleReqDto) {
        EventSchedule eventSchedule = findScheduleById(scheduleId);
        eventSchedule.setEvent(findEventById(scheduleReqDto.getEventId()));
        eventSchedule.setHall(findHallById(scheduleReqDto.getHallId()));
        eventSchedule.setStartTime(scheduleReqDto.getStartTime());
        eventSchedule.setEndTime(scheduleReqDto.getEndTime());
        eventSchedule.setStatus(scheduleReqDto.getStatus());
        eventSchedule.setTicketPrice(scheduleReqDto.getTicketPrice());

        scheduleRepository.save(eventSchedule);
        return new ApiResponse("Event Schedule updated successfully.");
    }

    @Override
    public ApiResponse updateScheduleStatus(Long scheduleId, ScheduleStatus status) {
        EventSchedule eventSchedule = findScheduleById(scheduleId);
        eventSchedule.setStatus(status);

        scheduleRepository.save(eventSchedule);

        return new ApiResponse("EventSchedule status updated to "+ status.name());
    }

    private EventSchedule findScheduleById(Long scheduleId) {
        return scheduleRepository
                .findById(scheduleId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Schedule", scheduleId)
                );
    }

    private Event findEventById(Long eventId) {
        return eventRepository
                .findById(eventId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Event", eventId)
                );
    }

    private Hall findHallById(Long hallId) {
        return hallRepository
                .findById(hallId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Hall", hallId)
                );
    }
}
