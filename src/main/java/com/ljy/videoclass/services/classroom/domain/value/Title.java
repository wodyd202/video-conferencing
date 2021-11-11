package com.ljy.videoclass.services.classroom.domain.value;

import org.springframework.util.StringUtils;

import javax.persistence.Embeddable;
import java.util.Objects;
import java.util.regex.Pattern;

@Embeddable
public class Title {
    private final String title;

    protected Title() {title = null;}

    private Title(String title) {
        verifyNotEmptyTitle(title);
        validation(title);
        this.title = title.trim();
    }

    private void verifyNotEmptyTitle(String title) {
        if(!StringUtils.hasText(title)){
            throw new IllegalArgumentException("수업명을 입력해주세요.");
        }
    }

    public static final String PATTERN_STR = "^[가-힣\\da-zA-Z\\s]{1,20}$";
    private static final Pattern PATTERN = Pattern.compile(PATTERN_STR);
    private void validation(String title) {
        if(!PATTERN.matcher(title).matches()){
            throw new IllegalArgumentException("수업명은 [한글, 숫자, 영어(대,소문자)] 만 허용하며 1자 이상 20자 이하로 입력해주세요.");
        }
    }

    public static Title of(String title){
        return new Title(title);
    }

    public String get() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Title title1 = (Title) o;
        return Objects.equals(title, title1.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }
}
