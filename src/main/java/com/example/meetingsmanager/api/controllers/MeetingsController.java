package com.example.meetingsmanager.api.controllers;

import com.example.meetingsmanager.api.models.MeetingOutDto;
import com.example.meetingsmanager.api.models.NewMeetingDto;
import com.example.meetingsmanager.api.models.PersonAndMeetingNameDto;
import com.example.meetingsmanager.domain.entities.Meeting;
import com.example.meetingsmanager.domain.repositories.MeetingsRepository;
import com.example.meetingsmanager.helpers.ConflictingScheduleException;
import com.example.meetingsmanager.helpers.Mapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class MeetingsController {
    private final MeetingsRepository repository;
    private final Mapper mapper;

    public MeetingsController(MeetingsRepository repository, Mapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @GetMapping("/meetings")
    public List<MeetingOutDto> getAllMeetings(@RequestParam Map<String, String> params) {
        List<Meeting> meetings = repository.getAllMeetings(params);
        if (meetings == null) {
            return null;
        }
        return meetings.stream().map(mapper::toMeetingOutDto).collect(Collectors.toList());
    }

    @PostMapping("/meetings")
    public NewMeetingDto newMeeting(@RequestBody Meeting newMeeting) {
        Meeting addedMeeting = repository.add(newMeeting);
        if (addedMeeting == null) {
            return null;
        }
        return mapper.toNewMeetingDto(addedMeeting);
    }

    @DeleteMapping("/meetings/{code}")
    public void deleteMeeting(@PathVariable Integer code) {
        repository.deleteMeeting(code);
    }

    @PostMapping("/meetings/person/add")
    public MeetingOutDto addPersonToMeeting(@RequestBody PersonAndMeetingNameDto personAndMeetingNameDto) throws ConflictingScheduleException {
        Meeting updatedMeeting = repository.addPersonToMeeting(personAndMeetingNameDto.getPerson(), personAndMeetingNameDto.getMeetingName());
        if (updatedMeeting == null) {
            return null;
        }
        return mapper.toMeetingOutDto(updatedMeeting);
    }

    @PostMapping("/meetings/person/remove")
    public MeetingOutDto removePersonFromMeeting(@RequestBody PersonAndMeetingNameDto personAndMeetingNameDto) {
        Meeting updatedMeeting = repository.removePersonFromMeeting(personAndMeetingNameDto.getPerson(), personAndMeetingNameDto.getMeetingName());
        if (updatedMeeting == null) {
            return null;
        }
        return mapper.toMeetingOutDto(updatedMeeting);
    }
}

