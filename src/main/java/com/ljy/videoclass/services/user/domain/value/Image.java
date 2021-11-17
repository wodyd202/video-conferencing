package com.ljy.videoclass.services.user.domain.value;

import com.ljy.videoclass.services.user.domain.exception.InvalidImageException;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Image {
    private final String path;

    protected Image(){ path = null;}

    private Image(String path) {
        if(path.isEmpty()){
            throw new InvalidImageException("사용자 이미지 경로를 입력해주세요.");
        }
        this.path = path;
    }

    public static Image path(String image){
        return new Image(image);
    }

    public String get() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return Objects.equals(path, image.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }
}
