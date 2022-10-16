package com.example.meetingsmanager.domain.entities;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Meeting {
    private String name;
    private Person responsiblePerson;
    private String description;
    private Category category;
    private Type type;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer secretCode;
    private List<Person> attendees;
}


