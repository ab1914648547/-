package org.develop.attendancesystem.service;

import org.develop.attendancesystem.entity.CourseInformation;
import org.develop.attendancesystem.entity.TeacherInformation;

import java.util.function.Consumer;

public interface TeacherInfoService {
    public void selectTeacherInfoTeacherId(String teacherId, Consumer<TeacherInformation> consumer);
}
