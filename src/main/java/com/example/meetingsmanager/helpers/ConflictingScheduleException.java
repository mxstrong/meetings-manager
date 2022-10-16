package com.example.meetingsmanager.helpers;

public class ConflictingScheduleException extends Exception {
    public ConflictingScheduleException(String errorMessage) {
        super(errorMessage);
    }
}
