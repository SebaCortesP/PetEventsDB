package com.petevents.peteventsapp.config;

import com.petevents.peteventsapp.models.Event;
import com.petevents.peteventsapp.models.EventType;
import com.petevents.peteventsapp.models.Pet;
import com.petevents.peteventsapp.models.Pet.PetType;
import com.petevents.peteventsapp.repositories.EventRepository;
import com.petevents.peteventsapp.repositories.EventTypeRepository;
import com.petevents.peteventsapp.repositories.PetRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Configuration
public class DataInitializer {  
    @Bean
    CommandLineRunner initData(PetRepository petRepository, EventTypeRepository eventTypeRepository,
            EventRepository eventRepository) {
        return args -> {
            // Inicialización de mascotas
            if (petRepository.count() == 0) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                List<Pet> pets = List.of(
                        new Pet("Firulais", sdf.parse("2020-03-01"), PetType.PERRO, "Carlos"),
                        new Pet("Michi", sdf.parse("2019-06-15"), PetType.GATO, "Laura"),
                        new Pet("Pelusa", sdf.parse("2021-01-10"), PetType.CONEJO, "Pedro"),
                        new Pet("Dori", sdf.parse("2022-12-05"), PetType.PEZ, "Ana"),
                        new Pet("Piolín", sdf.parse("2023-05-20"), PetType.AVE, "Luis"));

                petRepository.saveAll(pets);
                System.out.println("✔️ Mascotas inicializadas correctamente.");
            }

            // Inicialización de tipos de evento
            if (eventTypeRepository.count() == 0) {
                List<EventType> eventTypes = List.of(
                        new EventType("Competición", "Eventos donde se compite por un premio"),
                        new EventType("Carrera", "Eventos de velocidad entre mascotas"),
                        new EventType("Concurso de belleza", "Eventos para evaluar la apariencia de las mascotas"),
                        new EventType("Pelea", "Enfrentamientos organizados entre mascotas"));

                eventTypeRepository.saveAll(eventTypes);
                System.out.println("Tipos de evento inicializados correctamente.");
            }

            // Inicialización de eventos
            if (eventRepository.count() == 0) {
                System.out.println("Iniciando creación de eventos...");

                List<Pet> allPets = petRepository.findAll();
                List<EventType> eventTypes = eventTypeRepository.findAll();
                System.out.println("Mascotas disponibles: " + allPets.size());
                System.out.println("Tipos de eventos disponibles: " + eventTypes.size());
                if (allPets.size() < 3 || eventTypes.size() < 2) {
                    System.out.println("No hay suficientes mascotas o tipos de evento.");
                    return;
                }

                Event event1 = new Event(
                        "Carrera de velocidad",
                        "Carrera de mascotas en el parque",
                        eventTypes.stream().filter(e -> e.getName().equalsIgnoreCase("Carrera")).findFirst()
                                .orElse(null),
                        List.of(allPets.get(0), allPets.get(1)),
                        new Date());

                System.out.println("Evento 1: " + event1.getName());

                Event event2 = new Event(
                        "Concurso de belleza",
                        "Concurso de mascotas más lindas",
                        eventTypes.stream().filter(e -> e.getName().equalsIgnoreCase("Concurso de belleza"))
                                .findFirst()
                                .orElse(null),
                        List.of(allPets.get(2), allPets.get(3)),
                        new Date());

                System.out.println("Evento 2: " + event2.getName());

                Event event3 = new Event(
                        "Competencia libre",
                        "Competencia donde todas participan",
                        eventTypes.stream().filter(e -> e.getName().equalsIgnoreCase("Competición")).findFirst()
                                .orElse(null),
                        allPets, // todas las mascotas
                        new Date());

                System.out.println("Evento 3: " + event3.getName());

                eventRepository.saveAll(List.of(event1, event2, event3));

                System.out.println("Eventos creados correctamente.");
            } else {
                System.out.println("Los eventos ya están creados.");
            }
        };
    }
}
