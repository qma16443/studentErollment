package com.example.restservice.Converter;

import org.springframework.core.convert.converter.Converter;
import java.util.UUID;

public class StringToUUIDConverter implements Converter<String, UUID> {
    @Override
    public UUID convert(String source) {
        if (source == null || source.isEmpty()) {
            // Handle null or empty string, you can return null or throw an exception
            return null;
        }

        try {
            return UUID.fromString(source);
        } catch (IllegalArgumentException e) {
            // Handle invalid UUID format, you can log the error, return null or throw a custom exception

            return null;
        }
    }
}
