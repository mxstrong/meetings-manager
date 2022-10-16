package com.example.meetingsmanager.helpers;

import com.example.meetingsmanager.api.models.MeetingOutDto;
import com.example.meetingsmanager.api.models.NewMeetingDto;
import com.example.meetingsmanager.domain.entities.Category;
import com.example.meetingsmanager.domain.entities.Meeting;
import com.example.meetingsmanager.domain.entities.Person;
import com.example.meetingsmanager.domain.entities.Type;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Mapper {
    public MeetingOutDto toMeetingOutDto(@NotNull Meeting meeting) {
        String name = meeting.getName();
        Person responsiblePerson = meeting.getResponsiblePerson();
        String description = meeting.getDescription();
        Category category = meeting.getCategory();
        Type type = meeting.getType();
        String startDate = meeting.getStartDate().toString();
        String endDate = meeting.getEndDate().toString();
        List<Person> attendees = meeting.getAttendees();
        return new MeetingOutDto(name, responsiblePerson, description, category, type, startDate, endDate, attendees);
    }

    public NewMeetingDto toNewMeetingDto(@NotNull Meeting meeting) {
        String name = meeting.getName();
        Person responsiblePerson = meeting.getResponsiblePerson();
        String description = meeting.getDescription();
        Category category = meeting.getCategory();
        Type type = meeting.getType();
        String startDate = meeting.getStartDate().toString();
        String endDate = meeting.getEndDate().toString();
        Integer secretCode = meeting.getSecretCode();
        List<Person> attendees = meeting.getAttendees();
        return new NewMeetingDto(name, responsiblePerson, description, category, type, startDate, endDate, secretCode, attendees);
    }
}
