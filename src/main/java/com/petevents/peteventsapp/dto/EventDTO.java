package com.petevents.peteventsapp.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EventDTO {
    private String name;
    private String description;
    private Long eventTypeId;
    private List<Long> petIds;
    private Date eventDate;   
}
