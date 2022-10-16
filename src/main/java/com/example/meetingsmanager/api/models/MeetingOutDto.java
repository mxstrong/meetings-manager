package com.example.meetingsmanager.api.models;

import com.example.meetingsmanager.domain.entities.Category;
import com.example.meetingsmanager.domain.entities.Person;
import com.example.meetingsmanager.domain.entities.Type;
import lombok.Data;

import java.util.List;

@Data
public class MeetingOutDto {
    private String name;
    private Person responsiblePerson;
    private String description;
    private Category category;
    private Type type;
    private String startDate;
    private String endDate;
    private List<Person> attendees;

    public MeetingOutDto(String name, Person responsiblePerson, String description, Category category, Type type, String startDate, String endDate, List<Person> attendees) {
        this.name = name;
        this.responsiblePerson = responsiblePerson;
        this.description = description;
        this.category = category;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.attendees = attendees;
    }
}
