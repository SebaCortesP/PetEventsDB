package com.petevents.peteventsapp.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.petevents.peteventsapp.models.Pet;


@Repository
public interface PetRepository extends JpaRepository<Pet, Long>{

}
