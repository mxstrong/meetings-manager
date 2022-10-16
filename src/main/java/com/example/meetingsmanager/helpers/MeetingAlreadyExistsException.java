package com.example.meetingsmanager.helpers;

public class MeetingAlreadyExistsException extends Exception {
    public MeetingAlreadyExistsException(String message) {
        super(message);
    }
}
