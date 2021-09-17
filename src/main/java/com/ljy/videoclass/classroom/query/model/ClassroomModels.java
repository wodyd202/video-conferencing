package com.ljy.videoclass.classroom.query.model;

import com.ljy.videoclass.classroom.domain.read.ClassroomModel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ClassroomModels {
    private List<ClassroomModel> classrooms;
    private long totalElement;

    @Builder
    public ClassroomModels(List<ClassroomModel> classrooms, long totalElement) {
        this.classrooms = classrooms;
        this.totalElement = totalElement;
    }
}
