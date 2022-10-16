package com.example.meetingsmanager.domain.repositories;

import com.example.meetingsmanager.domain.entities.Meeting;
import com.example.meetingsmanager.domain.entities.Person;
import com.example.meetingsmanager.helpers.ConflictingScheduleException;
import com.example.meetingsmanager.helpers.IllegalActionException;
import com.example.meetingsmanager.helpers.MeetingAlreadyExistsException;
import com.example.meetingsmanager.helpers.MeetingNotFoundException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Repository
public class MeetingsRepository {
    public List<Meeting> getAllMeetings(Map<String, String> params) {
        File dataFile = getDataFile();
        try {
            List<Meeting> meetings = getMeetings(dataFile);
            if (params.get("description") != null) {
                meetings = meetings.stream().filter(meeting -> meeting.getDescription().toLowerCase().contains(params.get("description").toLowerCase())).collect(Collectors.toList());
            }
            if (params.get("responsible") != null) {
                meetings = meetings.stream().filter(meeting -> meeting.getResponsiblePerson().getFullName().toLowerCase().contains(params.get("responsible").toLowerCase())).collect(Collectors.toList());
            }
            if (params.get("category") != null) {
                meetings = meetings.stream().filter(meeting -> meeting.getCategory().toString().toLowerCase().contains(params.get("category").toLowerCase())).collect(Collectors.toList());
            }
            if (params.get("type") != null) {
                meetings = meetings.stream().filter(meeting -> meeting.getType().toString().toLowerCase().contains(params.get("type").toLowerCase())).collect(Collectors.toList());
            }
            if (params.get("attendees") != null) {
                meetings = meetings.stream().filter(meeting -> meeting.getAttendees().size() >= Integer.parseInt(params.get("attendees"))).collect(Collectors.toList());
            }
            if (params.get("startDate") != null) {
                meetings = meetings.stream().filter(meeting -> meeting.getStartDate().compareTo(LocalDate.parse(params.get("startDate")).atStartOfDay()) > 0).collect(Collectors.toList());
            }
            if (params.get("endDate") != null) {
                meetings = meetings.stream().filter(meeting -> meeting.getEndDate().compareTo(LocalDate.parse(params.get("endDate")).atStartOfDay()) < 0).collect(Collectors.toList());
            }
            return meetings;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Meeting add(Meeting meeting) throws MeetingAlreadyExistsException {
        Random rand = new Random();
        int code = rand.nextInt(10000000, 99999999);
        File dataFile = getDataFile();
        try {
            List<Meeting> meetings = getMeetings(dataFile);
            boolean meetingAlreadyExists = meetings.stream().anyMatch(existingMeeting -> existingMeeting.getName().equals(meeting.getName()));
            if (meetingAlreadyExists) {
                throw new MeetingAlreadyExistsException("Meeting with the same name as the one you are trying to add already exists");
            }
            meeting.setSecretCode(code);
            List<Person> attendees = new ArrayList<>() {
            };
            attendees.add(meeting.getResponsiblePerson());
            meeting.setAttendees(attendees);
            meetings.add(meeting);
            writeMeetingsToFile(dataFile, meetings);
            return meeting;
        } catch(IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void deleteMeeting(Integer secretCode) {
        File dataFile = getDataFile();
        try {
            ArrayList<Meeting> meetings = (ArrayList<Meeting>) getMeetings(dataFile);
            boolean removedSuccessfully = meetings.removeIf(meeting -> (Objects.equals(meeting.getSecretCode(), secretCode)));
            if (removedSuccessfully) {
                writeMeetingsToFile(dataFile, meetings);
            }
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public Meeting addPersonToMeeting(Person person, String meetingName) throws ConflictingScheduleException, MeetingNotFoundException {
        File dataFile = getDataFile();
        try {
            List<Meeting> meetings = getMeetings(dataFile);
            Meeting foundMeeting = meetings.stream().filter(meeting -> Objects.equals(meeting.getName(), meetingName)).findFirst().orElse(null);
            if (foundMeeting == null) {
                throw new MeetingNotFoundException("Meeting not found");
            }
            List<Person> attendees = foundMeeting.getAttendees();
            boolean alreadyAdded = attendees != null && !attendees.isEmpty() && attendees.stream().anyMatch(attendee -> attendee.getFullName().equals(person.getFullName()));
            if (alreadyAdded) {
                throw new ConflictingScheduleException("A person you are trying to add to a meeting is already added");
            }
            boolean conflictingMeetingsExist = meetings.stream().anyMatch(meeting -> meeting.getAttendees().stream().anyMatch(attendee -> (Objects.equals(attendee.getFullName(), person.getFullName()) && meeting.getStartDate().compareTo(foundMeeting.getEndDate()) < 0 && meeting.getEndDate().compareTo(foundMeeting.getStartDate()) > 0)));
            if (conflictingMeetingsExist) {
                throw new ConflictingScheduleException("A person you are trying to add to a meeting is already in a meeting at that time");
            }
            attendees.add(person);
            foundMeeting.setAttendees(attendees);
            meetings = meetings.stream().filter(meeting -> !Objects.equals(meeting.getName(), meetingName)).collect(Collectors.toList());
            meetings.add(foundMeeting);
            writeMeetingsToFile(dataFile, meetings);
            return foundMeeting;
        } catch(IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Meeting removePersonFromMeeting(Person person, String meetingName) throws MeetingNotFoundException, IllegalActionException {
        File dataFile = getDataFile();
        try {
            List<Meeting> meetings = getMeetings(dataFile);
            Meeting foundMeeting = meetings.stream().filter(meeting -> Objects.equals(meeting.getName(), meetingName)).findFirst().orElse(null);
            if (foundMeeting == null) {
                throw new MeetingNotFoundException("Meeting not found");
            }
            if (foundMeeting.getResponsiblePerson().getFullName().equals(person.getFullName())) {
                throw new IllegalActionException("It's not allowed to remove responsible person from a meeting");
            }
            List<Person> attendees = foundMeeting.getAttendees();
            attendees.removeIf(attendee -> attendee.getFullName().equals(person.getFullName()));
            foundMeeting.setAttendees(attendees);
            List<Meeting> otherMeetings = meetings.stream().filter(meeting -> !meeting.getName().equals(meetingName)).collect(Collectors.toList());
            otherMeetings.add(foundMeeting);
            writeMeetingsToFile(dataFile, otherMeetings);
            return foundMeeting;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private static List<Meeting> getMeetings(@NotNull File dataFile) throws IOException {
        boolean isNewFile = dataFile.createNewFile();
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        List<Meeting> meetings;
        if (!isNewFile && dataFile.length() != 0) {
            meetings = objectMapper.readValue(dataFile, new TypeReference<ArrayList<Meeting>>() {});
        } else {
            meetings = new ArrayList<>();
        }
        return meetings;
    }

    private void writeMeetingsToFile(File dataFile, List<Meeting> list) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        objectMapper.writeValue(dataFile, list);
    }

    private File getDataFile() {
        return new File("meetingsmanager/src/main/data.json");
    }
}
