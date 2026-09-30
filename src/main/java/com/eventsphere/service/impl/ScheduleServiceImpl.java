package com.eventsphere.service.impl;

import com.eventsphere.dto.Request.EventScheduleReqDto;
import com.eventsphere.dto.Response.ApiResponse;
import com.eventsphere.dto.Response.EventScheduleResDto;
import com.eventsphere.entity.Event;
import com.eventsphere.entity.EventSchedule;
import com.eventsphere.entity.Hall;
import com.eventsphere.entity.ScheduleStatus;
import com.eventsphere.exception.ApiException;
import com.eventsphere.exception.ConflictException;
import com.eventsphere.exception.ResourceNotFoundException;
import com.eventsphere.repository.EventRepository;
import com.eventsphere.repository.HallRepository;
import com.eventsphere.repository.ScheduleRepository;
import com.eventsphere.repository.TicketRepository;
import com.eventsphere.service.ScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@Slf4j
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;

    private final EventRepository eventRepository;

    private final HallRepository hallRepository;

    private final TicketRepository ticketRepository;

    private final ModelMapper modelMapper;

    public ScheduleServiceImpl(
            ScheduleRepository scheduleRepository,
            EventRepository eventRepository,
            HallRepository hallRepository,
            TicketRepository ticketRepository,
            ModelMapper modelMapper
    ) {
        this.scheduleRepository = scheduleRepository;
        this.eventRepository = eventRepository;
        this.hallRepository = hallRepository;
        this.ticketRepository = ticketRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ApiResponse createSchedule(EventScheduleReqDto scheduleReqDto) {
        log.info("Creating schedule for Event ID: {}, Hall ID: {}", scheduleReqDto.getEventId(), scheduleReqDto.getHallId());
        validateScheduleTimes(scheduleReqDto.getStartTime(), scheduleReqDto.getEndTime());

        Event event = findEventById(scheduleReqDto.getEventId());
        Hall hall = findHallById(scheduleReqDto.getHallId());

        // Validate overlapping schedule for the same hall
        if (scheduleRepository.existsOverlappingSchedule(hall, scheduleReqDto.getStartTime(), scheduleReqDto.getEndTime(), null)) {
            log.warn("Schedule conflict detected for Hall '{}' between {} and {}", hall.getName(), scheduleReqDto.getStartTime(), scheduleReqDto.getEndTime());
            throw new ConflictException("Hall '" + hall.getName() + "' already has an active event scheduled during the specified time slot.");
        }

        EventSchedule eventSchedule = modelMapper.map(scheduleReqDto, EventSchedule.class);
        eventSchedule.setEvent(event);
        eventSchedule.setHall(hall);
        if (eventSchedule.getStatus() == null) {
            eventSchedule.setStatus(ScheduleStatus.AVAILABLE);
        }

        scheduleRepository.save(eventSchedule);
        log.info("Schedule created successfully with ID: {}", eventSchedule.getId());
        return new ApiResponse("Create schedule successfully. Id : " + eventSchedule.getId());
    }

    @Override
    public EventScheduleResDto getEventSchedule(Long scheduleId) {
        log.debug("Fetching schedule ID: {}", scheduleId);
        EventSchedule schedule = findScheduleById(scheduleId);
        return mapScheduleToDto(schedule);
    }

    @Override
    public List<EventScheduleResDto> getSchedulesOfEvent(Long eventId) {
        log.debug("Fetching schedules for Event ID: {}", eventId);
        return scheduleRepository
                .findAllByEvent(findEventById(eventId))
                .stream()
                .map(this::mapScheduleToDto)
                .toList();
    }

    private EventScheduleResDto mapScheduleToDto(EventSchedule schedule) {
        EventScheduleResDto dto = modelMapper.map(schedule, EventScheduleResDto.class);
        dto.setId(schedule.getId());
        if (schedule.getEvent() != null) {
            dto.setEventId(schedule.getEvent().getId());
        }
        if (schedule.getHall() != null) {
            dto.setHallId(schedule.getHall().getId());
            dto.setHallName(schedule.getHall().getName());
            if (schedule.getHall().getVenue() != null) {
                dto.setVenueName(schedule.getHall().getVenue().getName());
            }
        }
        return dto;
    }

    @Override
    public ApiResponse updateSchedule(Long scheduleId, EventScheduleReqDto scheduleReqDto) {
        log.info("Updating schedule ID: {}", scheduleId);
        validateScheduleTimes(scheduleReqDto.getStartTime(), scheduleReqDto.getEndTime());

        EventSchedule eventSchedule = findScheduleById(scheduleId);
        Hall hall = findHallById(scheduleReqDto.getHallId());

        // Validate overlapping schedule for the same hall excluding this schedule
        if (scheduleRepository.existsOverlappingSchedule(hall, scheduleReqDto.getStartTime(), scheduleReqDto.getEndTime(), scheduleId)) {
            log.warn("Schedule conflict detected on update for Hall '{}' between {} and {}", hall.getName(), scheduleReqDto.getStartTime(), scheduleReqDto.getEndTime());
            throw new ConflictException("Hall '" + hall.getName() + "' already has an active event scheduled during the specified time slot.");
        }

        eventSchedule.setEvent(findEventById(scheduleReqDto.getEventId()));
        eventSchedule.setHall(hall);
        eventSchedule.setStartTime(scheduleReqDto.getStartTime());
        eventSchedule.setEndTime(scheduleReqDto.getEndTime());
        eventSchedule.setStatus(scheduleReqDto.getStatus());
        eventSchedule.setTicketPrice(scheduleReqDto.getTicketPrice());

        scheduleRepository.save(eventSchedule);
        log.info("Schedule ID: {} updated successfully", scheduleId);
        return new ApiResponse("Event Schedule updated successfully.");
    }

    @Override
    public ApiResponse updateScheduleStatus(Long scheduleId, ScheduleStatus status) {
        log.info("Updating schedule ID: {} status to {}", scheduleId, status);
        EventSchedule eventSchedule = findScheduleById(scheduleId);
        eventSchedule.setStatus(status);

        scheduleRepository.save(eventSchedule);
        return new ApiResponse("EventSchedule status updated to " + status.name());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> getBookedSeatIds(Long scheduleId) {
        log.debug("Fetching booked seat IDs for Schedule ID: {}", scheduleId);
        return ticketRepository.findBookedSeatIdsByScheduleId(scheduleId);
    }

    private void validateScheduleTimes(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null || endTime == null) {
            throw new ApiException("Schedule start time and end time are required.");
        }
        if (!endTime.isAfter(startTime)) {
            throw new ApiException("Schedule end time must be after the start time.");
        }
        if (startTime.isBefore(LocalDateTime.now())) {
            throw new ApiException("Cannot schedule an event in the past.");
        }
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
