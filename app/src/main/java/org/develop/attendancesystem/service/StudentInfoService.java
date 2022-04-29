package org.develop.attendancesystem.service;

import androidx.core.util.Consumer;

import org.develop.attendancesystem.entity.StudentInformation;

public interface StudentInfoService {

    void getStudentId(String studentId, Consumer<StudentInformation> consumer) ;
}
