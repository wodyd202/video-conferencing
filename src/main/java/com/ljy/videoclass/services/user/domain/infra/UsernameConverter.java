package com.ljy.videoclass.services.user.domain.infra;

import com.ljy.videoclass.services.user.domain.value.Username;

import javax.persistence.AttributeConverter;

public class UsernameConverter implements AttributeConverter<Username, String> {
    @Override
    public String convertToDatabaseColumn(Username username) {
        return username.get();
    }

    @Override
    public Username convertToEntityAttribute(String s) {
        return Username.of(s);
    }
}
