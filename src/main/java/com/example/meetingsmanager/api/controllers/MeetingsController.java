package com.example.meetingsmanager.api.controllers;

import com.example.meetingsmanager.api.models.MeetingOutDto;
import com.example.meetingsmanager.api.models.NewMeetingDto;
import com.example.meetingsmanager.domain.entities.Meeting;
import com.example.meetingsmanager.domain.repositories.MeetingsRepository;
import com.example.meetingsmanager.helpers.Mapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public List<MeetingOutDto> getAllMeetings() {
        return repository.getAllMeetings().stream().map(mapper::toMeetingOutDto).collect(Collectors.toList());
    }

    @PostMapping("/meetings")
    public NewMeetingDto newMeeting(@RequestBody Meeting newMeeting) {
        Meeting addedMeeting = repository.add(newMeeting);
        return mapper.toNewMeetingDto(addedMeeting);
    }

    @DeleteMapping("/meetings/{code}")
    public void deleteMeeting(@PathVariable Integer code) {
        repository.deleteMeeting(code);
    }
}
