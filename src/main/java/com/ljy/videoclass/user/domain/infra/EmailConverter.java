package com.ljy.videoclass.user.domain.infra;

import com.ljy.videoclass.user.domain.value.Email;

import javax.persistence.AttributeConverter;

public class EmailConverter implements AttributeConverter<Email,String> {
    @Override
    public String convertToDatabaseColumn(Email email) {
        return email.get();
    }

    @Override
    public Email convertToEntityAttribute(String s) {
        return Email.of(s);
    }
}
