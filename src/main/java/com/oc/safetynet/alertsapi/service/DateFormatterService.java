package com.oc.safetynet.alertsapi.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateFormatterService {

    private LocalDate formatLocalDate(LocalDate dateToFormat) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String formattedDateString = dateToFormat.format(formatter);
        return LocalDate.parse(formattedDateString, formatter);
    }
}
