package com.petevents.peteventsapp.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.ZoneId;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petevents.peteventsapp.config.SecurityConfig;
import com.petevents.peteventsapp.dto.EventDTO;
import com.petevents.peteventsapp.models.Event;
import com.petevents.peteventsapp.services.EventService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import java.util.*;

@WebMvcTest(EventController.class)
@WithMockUser
@Import(SecurityConfig.class)
public class EventControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    private List<Event> events;

    @BeforeEach
    public void setUp() {
        Event e1 = new Event();
        e1.setId(1L);
        e1.setName("Evento 1");
        e1.setDescription("Descripción 1");
        e1.setEvent_date(Date.from(LocalDate.of(2025, 5, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        Event e2 = new Event();
        e2.setId(2L);
        e2.setName("Evento 2");
        e2.setDescription("Descripción 2");
        e2.setEvent_date(Date.from(LocalDate.of(2025, 6, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        events = Arrays.asList(e1, e2);
    }

    @Test
    void getAllEventsTest() throws Exception {
        when(eventService.getAllEvents()).thenReturn(events);

        mockMvc.perform(MockMvcRequestBuilders.get("/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.is(2)))
                .andExpect(jsonPath("$._embedded.eventList[0].name", Matchers.is("Evento 1")))
                .andExpect(jsonPath("$._embedded.eventList[1].name", Matchers.is("Evento 2")));
    }

    @Test
    void getEventByIdTest() throws Exception {
        Event e = events.get(0);
        when(eventService.getEventById(e.getId())).thenReturn(Optional.of(e));

        mockMvc.perform(MockMvcRequestBuilders.get("/events/" + e.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.is("Evento 1")))
                .andExpect(jsonPath("$.description", Matchers.is("Descripción 1")));
    }

    @WithMockUser
    @Test
    void storeEventTest() throws Exception {
        EventDTO dto = new EventDTO();
        dto.setName("Nuevo evento");
        dto.setDescription("Descripción del nuevo evento");
        dto.setEventDate(Date.from(LocalDate.of(2025, 9, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        Event saved = new Event();
        saved.setId(10L);
        saved.setName(dto.getName());
        saved.setDescription(dto.getDescription());
        saved.setEvent_date(dto.getEventDate());

        when(eventService.createEventFromDTO(any(EventDTO.class))).thenReturn(saved);

        mockMvc.perform(MockMvcRequestBuilders.post("/events/store")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(10)))
                .andExpect(jsonPath("$.name", Matchers.is("Nuevo evento")));
    }
}
