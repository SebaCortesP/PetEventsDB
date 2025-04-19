package com.petevents.peteventsapp.services;
import com.petevents.peteventsapp.models.EventType;
import com.petevents.peteventsapp.repositories.EventTypeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventTypeService {
    private final EventTypeRepository eventTypeRepository;

    @Autowired
    public EventTypeService(EventTypeRepository eventTypeRepository) {
        this.eventTypeRepository = eventTypeRepository;
    }

    // Obtener todos los eventos
    public List<EventType> getAllEvents() {
        return eventTypeRepository.findAll();
    }

    // Obtener un evento por ID
    public Optional<EventType> getEventById(Long id) {
        return eventTypeRepository.findById(id);
    }

    public Optional<EventType> storeEventType(EventType event){
        return Optional.of(eventTypeRepository.save(event));
    }      
    
    public Optional<EventType> updateEvent(Long id, EventType event){
        Boolean existEvent = eventTypeRepository.existsById(id);
        if(existEvent){
            event.setId(id);
            return Optional.of(eventTypeRepository.save(event));
            
        }else{
            return Optional.empty();
        }
    }
    
    public Optional<?> deleteEventType(Long id){
        Boolean existEvent = eventTypeRepository.existsById(id);
        if(existEvent){
            eventTypeRepository.deleteById(id);
            return Optional.of("Deleted");
        }else{
            return Optional.empty();
        }
    }


}
