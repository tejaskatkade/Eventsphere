package com.eventsphere.repository;

import com.eventsphere.entity.Event;
import com.eventsphere.entity.EventSchedule;
import com.eventsphere.entity.Hall;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<EventSchedule, Long> {
    List<EventSchedule> findAllByEvent(Event event);

    List<EventSchedule> findAllByHall(Hall hall);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM EventSchedule s WHERE s.id = :id")
    Optional<EventSchedule> findByIdWithPessimisticLock(@Param("id") Long id);

    @Query("SELECT COUNT(s) > 0 FROM EventSchedule s " +
           "WHERE s.hall = :hall " +
           "AND s.status != com.eventsphere.entity.ScheduleStatus.CANCELLED " +
           "AND (:excludeScheduleId IS NULL OR s.id != :excludeScheduleId) " +
           "AND s.startTime < :endTime " +
           "AND s.endTime > :startTime")
    boolean existsOverlappingSchedule(
            @Param("hall") Hall hall,
            @Param("startTime") java.time.LocalDateTime startTime,
            @Param("endTime") java.time.LocalDateTime endTime,
            @Param("excludeScheduleId") Long excludeScheduleId
    );
}
