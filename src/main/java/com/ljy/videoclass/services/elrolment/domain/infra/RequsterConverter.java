package com.ljy.videoclass.services.elrolment.domain.infra;

import com.ljy.videoclass.services.elrolment.domain.value.Requester;

import javax.persistence.AttributeConverter;

public class RequsterConverter implements AttributeConverter<Requester, String> {
    @Override
    public String convertToDatabaseColumn(Requester requester) {
        return requester.get();
    }

    @Override
    public Requester convertToEntityAttribute(String s) {
        return Requester.of(s);
    }
}
