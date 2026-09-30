package com.eventsphere.repository;

import com.eventsphere.entity.Category;
import com.eventsphere.entity.Event;
import com.eventsphere.entity.Organiser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT DISTINCT e FROM Event e " +
           "JOIN FETCH e.category " +
           "JOIN FETCH e.organiser " +
           "JOIN e.eventSchedules es " +
           "JOIN es.hall h " +
           "JOIN h.venue v " +
           "WHERE LOWER(v.city) = LOWER(:city)")
    List<Event> findAllByCity(@Param("city") String city);

    @Query("SELECT DISTINCT e FROM Event e " +
           "JOIN FETCH e.category " +
           "JOIN FETCH e.organiser")
    List<Event> findAllWithDetails();

    @Query("SELECT DISTINCT e FROM Event e " +
           "JOIN FETCH e.category " +
           "JOIN FETCH e.organiser " +
           "WHERE e.id = :id")
    Optional<Event> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT DISTINCT e FROM Event e " +
           "JOIN FETCH e.category " +
           "JOIN FETCH e.organiser o " +
           "WHERE o.id = :organiserId")
    List<Event> findAllByOrganiserIdWithDetails(@Param("organiserId") Long organiserId);

    @Query("SELECT DISTINCT e FROM Event e " +
           "JOIN FETCH e.category c " +
           "JOIN FETCH e.organiser " +
           "WHERE LOWER(c.name) = LOWER(:categoryName)")
    List<Event> findAllByCategoryNameWithDetails(@Param("categoryName") String categoryName);

    List<Event> findAllByOrganiser(Organiser organiser);

    List<Event> findAllByCategory(Category category);
}
