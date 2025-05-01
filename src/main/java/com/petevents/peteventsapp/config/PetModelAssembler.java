package com.petevents.peteventsapp.config;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

import com.petevents.peteventsapp.controllers.PetController;
import com.petevents.peteventsapp.models.Pet;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

public class PetModelAssembler implements RepresentationModelAssembler<Pet, EntityModel<Pet>>{
    @Override
    public EntityModel<Pet> toModel(Pet pet) {
        return EntityModel.of(pet,
                linkTo(methodOn(PetController.class).getPetById(pet.getId())).withSelfRel(),
                linkTo(methodOn(PetController.class).getAllPets()).withRel("pets"));
    }
}
