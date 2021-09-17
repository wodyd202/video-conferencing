package com.ljy.videoclass.user.domain.infra;

import com.ljy.videoclass.user.domain.value.Image;

import javax.persistence.AttributeConverter;

public class ImageConverter implements AttributeConverter<Image, String> {

    @Override
    public String convertToDatabaseColumn(Image image) {
        return image.get();
    }

    @Override
    public Image convertToEntityAttribute(String s) {
        return Image.path(s);
    }
}
