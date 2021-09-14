package com.ljy.videoclass.enrollment.domain.infra;

import com.ljy.videoclass.enrollment.domain.value.Requester;

import javax.persistence.AttributeConverter;

public class RequesterConverter implements AttributeConverter<Requester, String> {
    @Override
    public String convertToDatabaseColumn(Requester requester) {
        return requester.get();
    }

    @Override
    public Requester convertToEntityAttribute(String s) {
        return Requester.of(s);
    }
}
