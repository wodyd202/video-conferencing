package com.ljy.videoclass.classroom.command.application.model;

import com.ljy.videoclass.classroom.domain.ChangeClassDateInfo;
import com.ljy.videoclass.classroom.domain.ChangeClassInfo;
import com.ljy.videoclass.classroom.domain.ChangeClassOptionalDateInfo;

import javax.validation.Valid;

public class ChangeClassAll {
    @Valid
    private ChangeClassInfo changeClassInfo;
    @Valid
    private ChangeClassDateInfo changeClassDateInfo;
    @Valid
    private ChangeClassOptionalDateInfo changeClassOptionalDateInfo;

    public ChangeClassInfo getChangeClassInfo() {
        return changeClassInfo;
    }

    public ChangeClassDateInfo getChangeClassDateInfo() {
        return changeClassDateInfo;
    }

    public ChangeClassOptionalDateInfo getChangeClassOptionalDateInfo() {
        return changeClassOptionalDateInfo;
    }
}
