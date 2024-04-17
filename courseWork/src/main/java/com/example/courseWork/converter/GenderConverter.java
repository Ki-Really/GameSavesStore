package com.example.courseWork.converter;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class GenderConverter {

    public String convert(String value) {
        switch (value) {
            case "male", "m", "Male" -> {
                return "male";
            }
            case "female", "f", "Female" -> {
                return "female";
            }
            default -> throw new IllegalArgumentException("Неизвестный тип данных");
        }
    }
}
