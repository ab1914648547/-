package org.develop.attendancesystem.service;

import org.develop.attendancesystem.entity.CourseInformation;

import java.util.List;
import java.util.function.Consumer;

public interface CourseInfoService {

    public void selectCourseInfoClassId(String classId, Consumer<CourseInformation> consumer);
    public void selectCourseInfoClass(String classId, Consumer<List<CourseInformation>> consumer);
    public void selectCourseInfoId(String courseId, Consumer<CourseInformation> consumer);
}
