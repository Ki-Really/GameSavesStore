package com.example.courseWork.converter;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class TimeConverter {
    public String convert(String type,String value) {
            switch (type) {
                case "time_minutes" -> {
                    return String.valueOf(Double.parseDouble(value) * 60);
                }
                case "time_hours" -> {
                    return String.valueOf(Double.parseDouble(value) * 3600);
                }
                case "time_ms" -> {
                    return  String.valueOf((Double.parseDouble(value) / 1000));
                }
                case "time_seconds" -> {
                    return  String.valueOf(Double.parseDouble(value));
                }
                case "time_date_format" -> {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                    Date date;
                    try {
                        date = dateFormat.parse(value);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    return String.valueOf(date.getTime() / 1000);
                }
                default -> throw new IllegalArgumentException("Неизвестный тип данных");
            }
    }
}
