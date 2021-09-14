package com.ljy.videoclass.classroom;

import javax.persistence.AttributeConverter;
import java.time.DayOfWeek;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DayOfWeeksConverter implements AttributeConverter<Set<DayOfWeek>, String> {

    @Override
    public String convertToDatabaseColumn(Set<DayOfWeek> dayOfWeeks) {
        StringBuilder dayOfWeeksToString = new StringBuilder();
        dayOfWeeks.stream().forEach(c->{
            dayOfWeeksToString.append(c + ",");
        });
        return dayOfWeeksToString.substring(0, dayOfWeeksToString.toString().length() - 1);
    }

    @Override
    public Set<DayOfWeek> convertToEntityAttribute(String s) {
        return Stream.of(s.split(","))
                .map(DayOfWeek::valueOf)
                .collect(Collectors.toSet());
    }
}
