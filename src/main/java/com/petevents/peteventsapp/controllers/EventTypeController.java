package com.petevents.peteventsapp.controllers;

import com.petevents.peteventsapp.models.EventType;
import com.petevents.peteventsapp.services.EventTypeService;

import jakarta.validation.Valid;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class EventTypeController {
    private final EventTypeService eventTypeService;

    public EventTypeController(EventTypeService eventTypeService) {
        this.eventTypeService = eventTypeService;
    }

    @GetMapping("/event_type")
    public List<EventType> getAllEvents() {
        return eventTypeService.getAllEvents();
    }

    @GetMapping("/event_type/{id}")
    public ResponseEntity<?> getEventById(@PathVariable("id") Long id) {
        Optional<EventType> eventType = eventTypeService.getEventById(id);

        if (eventType.isPresent()) {
            return ResponseEntity.ok(eventType.get());
        } else {
            return ResponseEntity.status(404).body("Event Type with ID " + id + " not found");
        }
    }

    @PostMapping("/event_type/store")
    public ResponseEntity<?> storeEventType(@RequestBody @Valid EventType eventType, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            Optional<EventType> saved = eventTypeService.storeEventType(eventType);
            return ResponseEntity.ok(saved);
        } catch (DataIntegrityViolationException ex) {
            Map<String, String> errorDetails = new HashMap<>();
            errorDetails.put("error", "Error de datos");
            errorDetails.put("message", "Uno o más campos obligatorios están vacíos o contienen valores no válidos.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
        }
    }

    @PutMapping("/event_type/update/{id}")
    public ResponseEntity<?> updateEventType(@PathVariable("id") Long id, @RequestBody @Valid EventType event, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            Optional<EventType> updated = eventTypeService.updateEvent(id, event);

            if (updated.isPresent()) {
                return ResponseEntity.ok(updated.get());
            } else {
                return ResponseEntity.status(404).body("No se encontró el tipo de evento con ID " + id);
            }
        } catch (DataIntegrityViolationException ex) {
            Map<String, String> errorDetails = new HashMap<>();
            errorDetails.put("error", "Error de datos");
            errorDetails.put("message", "Uno o más campos obligatorios están vacíos o contienen valores no válidos.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
        }
    }

    @DeleteMapping("/event_type/delete/{id}")
    public ResponseEntity<?> deleteEventType(@PathVariable("id") Long id) {
        Optional<?> result = eventTypeService.deleteEventType(id);
        if (result.isPresent()) {
            return ResponseEntity.status(201).body("Event Type with ID " + id + " has been deleted");
        } else {
            return ResponseEntity.status(404).body("Event Type with ID " + id + " not found");

        }
    }
}
