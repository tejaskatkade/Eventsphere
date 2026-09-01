package com.eventsphere.repository;

import com.eventsphere.entity.Organiser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganiserRepository extends JpaRepository<Organiser, Long> {
}
