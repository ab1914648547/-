package org.develop.attendancesystem.service;

import org.develop.attendancesystem.entity.Signinformation;

import java.util.List;
import java.util.function.Consumer;

public interface SignInfoService {

    public void insertSignInfo(String id, String json);

    public void selectSignInfoStudentId(String studentId, int year, int month, int day, Consumer<List<Signinformation>> consumer);
}
