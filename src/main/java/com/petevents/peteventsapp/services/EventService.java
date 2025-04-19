package com.petevents.peteventsapp.services;
import com.petevents.peteventsapp.dto.EventDTO;
import com.petevents.peteventsapp.models.Event;
import com.petevents.peteventsapp.models.EventType;
import com.petevents.peteventsapp.models.Pet;
import com.petevents.peteventsapp.repositories.EventRepository;
import com.petevents.peteventsapp.repositories.EventTypeRepository;
import com.petevents.peteventsapp.repositories.PetRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class EventService {
    private final EventRepository eventRepository;
    @Autowired
    private EventTypeRepository eventTypeRepository;

    @Autowired
    private PetRepository petRepository;
    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }
    // Obtener todos los eventos
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    // Obtener un evento por ID
    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    public Optional<Event> updateEvent(Long id, Event event){
        Boolean existEvent = eventRepository.existsById(id);
        if(existEvent){
            event.setId(id);
            return Optional.of(eventRepository.save(event));
            
        }else{
            return Optional.empty();
        }
    }
    
    public Optional<?> deleteEvent(Long id){
        Boolean existEvent = eventRepository.existsById(id);
        if(existEvent){
            eventRepository.deleteById(id);
            return Optional.of("Deleted");
        }else{
            return Optional.empty();
        }
    }

    public Optional<Event> storeEvent(Event event){
        return Optional.of(eventRepository.save(event));
    }

    public Event createEventFromDTO(EventDTO dto) {
        // Validaciones iniciales
        if (dto.getEventTypeId() == null) {
            log.warn("El campo 'eventTypeId' es null.");
            throw new IllegalArgumentException("El campo 'eventTypeId' es obligatorio.");
        }

        if (dto.getPetIds() == null || dto.getPetIds().isEmpty()) {
            log.warn("La lista 'petIds' es null o está vacía.");
            throw new IllegalArgumentException("Debe seleccionar al menos una mascota.");
        }

        // Buscar EventType
        EventType eventType = eventTypeRepository.findById(dto.getEventTypeId())
                .orElseThrow(() -> {
                    log.warn("EventType con ID {} no encontrado", dto.getEventTypeId());
                    return new IllegalArgumentException("El tipo de evento no existe.");
                });

        // Buscar pets
        List<Pet> pets = petRepository.findAllById(dto.getPetIds());

        if (pets.size() != dto.getPetIds().size()) {
            log.warn("Una o más mascotas no fueron encontradas. IDs enviados: {}", dto.getPetIds());
            throw new IllegalArgumentException("Una o más mascotas no existen.");
        }

        // Crear el evento
        Event event = new Event();
        event.setName(dto.getName());
        event.setDescription(dto.getDescription());
        event.setEventType(eventType);
        event.setPets(pets);
        event.setEvent_date(dto.getEventDate());

        log.info("Evento creado correctamente con {} mascotas", event.getName(), pets.size());
        return eventRepository.save(event);
    }
}
