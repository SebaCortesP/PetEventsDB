package com.petevents.peteventsapp.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.petevents.peteventsapp.models.Event;


@Repository
public interface EventRepository extends JpaRepository<Event, Long>{
    
}
