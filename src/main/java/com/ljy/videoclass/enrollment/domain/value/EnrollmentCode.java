package com.ljy.videoclass.enrollment.domain.value;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class EnrollmentCode implements Serializable {
    private final String code;

    protected EnrollmentCode(){code = null;}

    private EnrollmentCode(String code) {
        this.code = code;
    }

    public static EnrollmentCode create(){
        return new EnrollmentCode(UUID.randomUUID().toString());
    }
}
