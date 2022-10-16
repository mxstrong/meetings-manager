package com.example.meetingsmanager.api.models;

import com.example.meetingsmanager.domain.entities.Person;
import lombok.Data;

@Data
public class PersonAndMeetingNameDto {
    private String meetingName;
    private Person person;
}
