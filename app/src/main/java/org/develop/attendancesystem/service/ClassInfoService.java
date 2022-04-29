package org.develop.attendancesystem.service;

import androidx.core.util.Consumer;

import org.develop.attendancesystem.entity.ClassInformation;

public interface ClassInfoService {

    void getClassInfoId(String classId, Consumer<ClassInformation> consumer);
}
