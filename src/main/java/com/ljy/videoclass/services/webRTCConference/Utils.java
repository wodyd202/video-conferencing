package com.ljy.videoclass.services.webRTCConference;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ljy.videoclass.services.webRTCConference.model.Message;

final public class Utils {
    private Utils() {}
    private static ObjectMapper objectMapper = new ObjectMapper();
    public static Message getObject(final String message) {
        try{
            return objectMapper.readValue(message, Message.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static String getString(final Message message) {
        try{
            return objectMapper.writeValueAsString(message);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
