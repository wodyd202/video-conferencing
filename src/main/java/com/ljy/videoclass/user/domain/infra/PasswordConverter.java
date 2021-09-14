package com.ljy.videoclass.user.command.domain.infra;

import com.ljy.videoclass.user.command.domain.Password;

import javax.persistence.AttributeConverter;

public class PasswordConverter implements AttributeConverter<Password, String> {
    @Override
    public String convertToDatabaseColumn(Password password) {
        return password.get();
    }

    @Override
    public Password convertToEntityAttribute(String s) {
        return Password.of(s);
    }
}
