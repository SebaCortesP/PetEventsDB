package com.petevents.peteventsapp.models;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "pets")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "El nombre de la mascota es obligatorio")
    private String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @Column(name = "birth", nullable = false)
    @NotNull(message = "La fecha de nacimiento es obligatoria") 
    @Temporal(TemporalType.DATE)
    private Date birth;

    @Column(name = "type", nullable = false)
    @NotBlank(message = "El tipo de mascota es obligatorio")
    @Enumerated(EnumType.STRING)
    private PetType type;

    @Column(name = "owner", nullable = false)
    @NotBlank(message = "El nombre del dueño es obligatorio")
    private String owner;

    public enum PetType {
        PERRO("Perro"),
        GATO("Gato"),
        GALLINA("Gallina"),
        CONEJO("Conejo"),
        PEZ("Pez"),
        AVE("Ave");

        private final String name;

        PetType(String name) {
            this.name = name;
        }

        public String getPetType() {
            return name;
        }


    }

    public Pet(String name, Date birth, PetType type, String owner) {
        this.name = name;
        this.birth = birth;
        this.type = type;
        this.owner = owner;
    }
    
}
