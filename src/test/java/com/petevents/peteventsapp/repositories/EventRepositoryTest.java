package com.petevents.peteventsapp.repositories;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.petevents.peteventsapp.models.Event;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EventRepositoryTest {
    @Autowired
    private EventRepository eventRepository;
    
    @Test
    void storeEventTest() {
        Event e1 = new Event();
        e1.setName("Concurso de Belleza");
        e1.setDescription("Evento de belleza");
        LocalDate localDate = LocalDate.of(2025, 5, 1);
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        e1.setEvent_date(date);

        Event result = eventRepository.save(e1);
        assertNotNull(result.getId());
        assertEquals("Concurso de Belleza", result.getName());
    }

    @Test
    void getEventByIdTest() {
        Event e1 = new Event();
        e1.setName("Pelea de Perros");
        e1.setDescription("Evento de pelea de Perros");
        LocalDate localDate = LocalDate.of(2025, 5, 1);
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        e1.setEvent_date(date);

        Event saved = eventRepository.save(e1);
        Optional<Event> found = eventRepository.findById(saved.getId());

        assertTrue(found.isPresent(), "El evento debería existir");
        assertEquals("Pelea de Perros", found.get().getName());
        assertEquals("Evento de pelea de Perros", found.get().getDescription());
    }

    @Test
    void deleteEventTest() {
        Event e1 = new Event();
        e1.setName("Evento para borrar");
        e1.setDescription("Este evento será eliminado");
        LocalDate localDate = LocalDate.of(2025, 5, 1);
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        e1.setEvent_date(date);

        Event saved = eventRepository.save(e1);
        Long id = saved.getId();

        eventRepository.deleteById(id);
        Optional<Event> deleted = eventRepository.findById(id);

        assertFalse(deleted.isPresent(), "El evento debería haber sido eliminado");
    }
}
