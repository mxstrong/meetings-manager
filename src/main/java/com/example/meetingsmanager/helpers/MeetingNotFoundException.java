package com.example.meetingsmanager.helpers;

public class MeetingNotFoundException extends Exception {
    public MeetingNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
