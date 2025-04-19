package com.petevents.peteventsapp.controllers;

import com.petevents.peteventsapp.models.Pet;
import com.petevents.peteventsapp.services.PetService;

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
import java.util.Optional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class PetController {
    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping("/pets")
    public List<Pet> getAllPets() {
        return petService.getAllPets();
    }

    @GetMapping("/pets/{id}")
    public ResponseEntity<?> getPetById(@PathVariable("id") Long id) {
        Optional<Pet> pet = petService.getPetById(id);

        if (pet.isPresent()) {
            return ResponseEntity.ok(pet.get());
        } else {
            return ResponseEntity.status(404).body("Pet with ID " + id + " not found");
        }
    }

    @PostMapping("/pets/store")
    public ResponseEntity<?> store(@RequestBody @Valid Pet pet, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            Optional<Pet> savedPet = Optional.ofNullable(petService.storePet(pet));
            return ResponseEntity.ok(savedPet);
        } catch (DataIntegrityViolationException ex) {
            Map<String, String> errorDetails = new HashMap<>();
            errorDetails.put("error", "Error de datos");
            errorDetails.put("message", "Uno o más campos obligatorios están vacíos o contienen valores no válidos.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
        }
    }

    @PutMapping("/pets/update/{id}")
    public ResponseEntity<?> updatePet(@PathVariable("id") Long id, @RequestBody @Valid Pet pet, BindingResult result) {
        // Validación de errores en el cuerpo de la solicitud
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errors);
        }    
        try {
            Optional<Pet> updatedPet = petService.updatePet(id, pet);            
            if (updatedPet.isPresent()) {
                return ResponseEntity.ok(updatedPet.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pet with ID " + id + " not found");
            }    
        } catch (DataIntegrityViolationException ex) {
            Map<String, String> errorDetails = new HashMap<>();
            errorDetails.put("error", "Error de datos");
            errorDetails.put("message", "Uno o más campos obligatorios están vacíos o contienen valores no válidos.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
        }
    }    

    @DeleteMapping("/pets/delete/{id}")
    public ResponseEntity<?> deletePet(@PathVariable("id") Long id) {
        Optional<?> result = petService.deletePet(id);

        if (result.isPresent()) {
            return ResponseEntity.status(201).body("Pet with ID " + id + " has been deleted");
        } else {
            return ResponseEntity.status(404).body("Pet with ID " + id + " not found");

        }
    }
}
