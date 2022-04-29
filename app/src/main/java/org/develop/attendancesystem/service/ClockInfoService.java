package org.develop.attendancesystem.service;

import org.develop.attendancesystem.entity.ClockInfomation;

import java.util.List;
import java.util.function.Consumer;

public interface ClockInfoService {
    public void selectClockInfo(String url, List<String> param, Consumer<List<ClockInfomation>> consumer);
}
