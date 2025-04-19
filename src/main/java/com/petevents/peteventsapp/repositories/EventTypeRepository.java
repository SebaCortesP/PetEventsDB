package com.petevents.peteventsapp.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.petevents.peteventsapp.models.EventType;


@Repository
public interface EventTypeRepository extends JpaRepository<EventType, Long>{

}
