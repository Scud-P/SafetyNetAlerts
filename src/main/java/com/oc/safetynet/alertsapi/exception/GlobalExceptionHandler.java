package com.oc.safetynet.alertsapi.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(MedicalRecordNotFoundException.class)
    public ResponseEntity<Object> handleMedicalRecordNotFoundException(
            MedicalRecordNotFoundException ex, WebRequest request) {
        String bodyOfResponse = "No medical record was found for that person";
        return new ResponseEntity<>(bodyOfResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PersonNotFoundException.class)
    public ResponseEntity<Object> handlePersonNotFoundException(
            PersonNotFoundException ex, WebRequest request) {
        String bodyOfResponse = "No person with that name was found";
        return new ResponseEntity<>(bodyOfResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FireStationNotFoundException.class)
    public ResponseEntity<Object> handleFireStationNotFoundException(
            FireStationNotFoundException ex, WebRequest request) {
        String bodyOfResponse = "No Fire Station was found for that address";
        return new ResponseEntity<>(bodyOfResponse, HttpStatus.NOT_FOUND);
    }
}
