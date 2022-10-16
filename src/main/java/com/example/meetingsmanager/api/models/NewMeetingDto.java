package com.example.meetingsmanager.api.models;

import com.example.meetingsmanager.domain.entities.Category;
import com.example.meetingsmanager.domain.entities.Person;
import com.example.meetingsmanager.domain.entities.Type;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NewMeetingDto extends MeetingOutDto {
    private Integer secretCode;

    public NewMeetingDto(String name, Person responsiblePerson, String description, Category category, Type type, String startDate, String endDate, Integer secretCode, List<Person> attendees) {
        super(name, responsiblePerson, description, category, type, startDate, endDate, attendees);
        this.secretCode = secretCode;
    }
}
