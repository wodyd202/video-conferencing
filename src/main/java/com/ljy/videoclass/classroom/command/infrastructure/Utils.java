package com.ljy.videoclass.classroom.command.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Utils {
    private Utils() {
    }

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static SignalMessage getObject(final String message) throws Exception {
        return objectMapper.readValue(message, SignalMessage.class);
    }

    public static String getString(final SignalMessage message) throws Exception {
        return objectMapper.writeValueAsString(message);
    }
}
