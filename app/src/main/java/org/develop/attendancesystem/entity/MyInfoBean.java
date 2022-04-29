package org.develop.attendancesystem.entity;

public class MyInfoBean {
    private String courseName;
    private String clockTimeState;
    private String delOrMod;

    @Override
    public String toString() {
        return "MyInfoBean{" +
                "courseName='" + courseName + '\'' +
                ", clockTimeState='" + clockTimeState + '\'' +
                ", delOrMod='" + delOrMod + '\'' +
                '}';
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getClockTimeState() {
        return clockTimeState;
    }

    public void setClockTimeState(String clockTimeState) {
        this.clockTimeState = clockTimeState;
    }

    public String getDelOrMod() {
        return delOrMod;
    }

    public void setDelOrMod(String delOrMod) {
        this.delOrMod = delOrMod;
    }
}
