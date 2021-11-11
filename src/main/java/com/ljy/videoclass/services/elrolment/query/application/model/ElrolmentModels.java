package com.ljy.videoclass.services.elrolment.query.application.model;

import com.ljy.videoclass.services.classroom.domain.read.ElrolmentModel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ElrolmentModels {
    private List<ElrolmentModel> elrolmentModels;
    private long totalElement;

    @Builder
    private ElrolmentModels(List<ElrolmentModel> elrolmentModels, long totalElement) {
        this.elrolmentModels = elrolmentModels;
        this.totalElement = totalElement;
    }
}
