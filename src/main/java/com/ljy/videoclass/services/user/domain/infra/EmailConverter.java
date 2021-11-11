package com.ljy.videoclass.services.user.domain.infra;

import com.ljy.videoclass.services.user.domain.value.Email;

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
