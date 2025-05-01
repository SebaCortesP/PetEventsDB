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
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@Slf4j
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }
    
    @GetMapping("/events")
    public ResponseEntity<CollectionModel<EntityModel<Event>>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();

        List<EntityModel<Event>> eventResources = events.stream()
                .map(event -> EntityModel.of(event,
                        linkTo(methodOn(EventController.class).getEventById(event.getId())).withSelfRel(),
                        linkTo(methodOn(EventController.class).getAllEvents()).withRel("all-events")))
                .toList();

        CollectionModel<EntityModel<Event>> collection = CollectionModel.of(eventResources,
                linkTo(methodOn(EventController.class).getAllEvents()).withSelfRel());

        return ResponseEntity.ok(collection);
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<?> getEventById(@PathVariable("id") Long id) {
        Optional<Event> event = eventService.getEventById(id);

        if (event.isPresent()) {
            EntityModel<Event> resource = EntityModel.of(event.get(),
                    linkTo(methodOn(EventController.class).getEventById(id)).withSelfRel(),
                    linkTo(methodOn(EventController.class).getAllEvents()).withRel("all-events"));

            return ResponseEntity.ok(resource);
        } else {
            return ResponseEntity.status(404).body("Event with ID " + id + " not found");
        }
    }

    @PostMapping("/events/store")
    public ResponseEntity<?> store(@RequestBody EventDTO dto) {
        try {
            Event saved = eventService.createEventFromDTO(dto);

            EntityModel<Event> resource = EntityModel.of(saved,
                    linkTo(methodOn(EventController.class).getEventById(saved.getId())).withSelfRel(),
                    linkTo(methodOn(EventController.class).getAllEvents()).withRel("all-events"));

            return ResponseEntity.ok(resource);

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
            EntityModel<Event> resource = EntityModel.of(updated.get(),
                    linkTo(methodOn(EventController.class).getEventById(id)).withSelfRel(),
                    linkTo(methodOn(EventController.class).getAllEvents()).withRel("all-events"));

            return ResponseEntity.ok(resource);
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
