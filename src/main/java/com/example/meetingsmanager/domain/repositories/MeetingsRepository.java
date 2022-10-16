package com.example.meetingsmanager.domain.repositories;

import com.example.meetingsmanager.domain.entities.Meeting;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;


@Repository
public class MeetingsRepository {
    public List<Meeting> getAllMeetings() {
        File dataFile = new File("meetingsmanager/src/main/data.json");
        try {
            return getMeetings(dataFile);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Meeting add(Meeting meeting) {
        Random rand = new Random();
        int code = rand.nextInt(10000000, 99999999);
        File dataFile = new File("meetingsmanager/src/main/data.json");
        try {
            List<Meeting> meetings = getMeetings(dataFile);
            meeting.setSecretCode(code);
            meetings.add(meeting);
            writeMeetingsToFile(dataFile, meetings);
            return meeting;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void deleteMeeting(Integer secretCode) {
        File dataFile = new File("meetingsmanager/src/main/data.json");
        try {
            ArrayList<Meeting> meetings = (ArrayList<Meeting>) getMeetings(dataFile);
            boolean removedSuccessfully = meetings.removeIf(meeting -> (Objects.equals(meeting.getSecretCode(), secretCode)));
            if (removedSuccessfully) {
                writeMeetingsToFile(dataFile, meetings);
            }
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static List<Meeting> getMeetings(@NotNull File dataFile) throws IOException {
        boolean isNewFile = dataFile.createNewFile();
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        List<Meeting> meetings;
        if (!isNewFile && dataFile.length() != 0) {
            meetings = objectMapper.readValue(dataFile, new TypeReference<ArrayList<Meeting>>() {});
        } else {
            meetings = new ArrayList<Meeting>();
        }
        return meetings;
    }

    private void writeMeetingsToFile(File dataFile, List<Meeting> list) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        objectMapper.writeValue(dataFile, list);
    }
}
