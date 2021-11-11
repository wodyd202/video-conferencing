package com.ljy.videoclass.services.classroom.command.application.model;

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
