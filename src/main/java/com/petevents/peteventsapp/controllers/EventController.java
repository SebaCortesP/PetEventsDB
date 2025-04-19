package com.petevents.peteventsapp.controllers;

import com.petevents.peteventsapp.dto.EventDTO;
import com.petevents.peteventsapp.models.Event;
import com.petevents.peteventsapp.services.EventService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@Slf4j
public class EventController {
    private final EventService eventService;

    
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    // Obtener todos los eventos
    @GetMapping("/events")
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<?> getEventById(@PathVariable("id") Long id) {
        Optional<Event> eventType = eventService.getEventById(id);

        if (eventType.isPresent()) {
            return ResponseEntity.ok(eventType.get());
        } else {
            return ResponseEntity.status(404).body("Event Type with ID " + id + " not found"); 
        }
    }

    @PostMapping("/events/store")
    public ResponseEntity<?> store(@RequestBody EventDTO dto) {
        try {
            Event saved = eventService.createEventFromDTO(dto);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            log.error("Error al crear evento: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Error inesperado al crear evento", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error inesperado"));
        }
    }

    @PutMapping("/events/update/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable("id") Long id, @RequestBody Event event) {
        Optional<Event> updated = eventService.updateEvent(id, event);

        if (updated.isPresent()) {
            return ResponseEntity.ok(updated.get());
        } else {
            return ResponseEntity.status(404).body("Event with ID " + id + " not found");
        }
    }

    @DeleteMapping("/events/delete/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable("id") Long id) {
        Optional<?> result = eventService.deleteEvent(id);
        if (result.isPresent()) {
            return ResponseEntity.status(201).body("Event with ID " + id + " has been deleted");
        } else {
            return ResponseEntity.status(404).body("Event with ID " + id + " not found");
        }
    }

}
