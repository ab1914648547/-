package org.develop.attendancesystem.entity;
public class ClockInfomation {

  private long courseId;
  private long classId;
  private long teacherId;
  private String clockTime;
  private String clockPlace;

  @Override
  public String toString() {
    return "ClockInfomation{" +
            "courseId=" + courseId +
            ", classId=" + classId +
            ", teacherId=" + teacherId +
            ", clockTime='" + clockTime + '\'' +
            ", clockPlace='" + clockPlace + '\'' +
            '}';
  }

  public long getCourseId() {
    return courseId;
  }

  public void setCourseId(long courseId) {
    this.courseId = courseId;
  }

  public long getClassId() {
    return classId;
  }

  public void setClassId(long classId) {
    this.classId = classId;
  }

  public long getTeacherId() {
    return teacherId;
  }

  public void setTeacherId(long teacherId) {
    this.teacherId = teacherId;
  }

  public String getClockTime() {
    return clockTime;
  }

  public void setClockTime(String clockTime) {
    this.clockTime = clockTime;
  }

  public String getClockPlace() {
    return clockPlace;
  }

  public void setClockPlace(String clockPlace) {
    this.clockPlace = clockPlace;
  }
}
