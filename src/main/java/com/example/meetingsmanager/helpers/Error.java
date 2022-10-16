package com.example.meetingsmanager.helpers;

import org.springframework.http.HttpStatus;

public record Error(HttpStatus httpStatus, String message) {
}
