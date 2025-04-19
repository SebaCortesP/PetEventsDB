package com.petevents.peteventsapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.petevents.peteventsapp.models.Pet;
import com.petevents.peteventsapp.repositories.PetRepository;

@Service
public class PetService {
    private final PetRepository petRepository;

    @Autowired
    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

     // Obtener todos los eventos
    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    // Obtener un evento por ID
    public Optional<Pet> getPetById(Long id) {
        return petRepository.findById(id);
    }

    public Optional<Pet> updatePet(Long id, Pet pet){
        Boolean existEvent = petRepository.existsById(id);
        if(existEvent){
            pet.setId(id);
            return Optional.of(petRepository.save(pet));
            
        }else{
            return Optional.empty();
        }
    }
    
    public Optional<?> deletePet(Long id){
        Boolean existPet = petRepository.existsById(id);
        if(existPet){
            petRepository.deleteById(id);
            return Optional.of("Deleted");
        }else{
            return Optional.empty();
        }
    }

    public Pet storePet(Pet pet) {
        return petRepository.save(pet);
    }
}
