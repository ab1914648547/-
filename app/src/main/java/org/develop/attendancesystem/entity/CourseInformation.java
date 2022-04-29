package org.develop.attendancesystem.entity;

public class CourseInformation {

  private long courseId;
  private String courseName;
  private String courseInformation;
  private long teacherId;
  private String venue;
  private long classId;
  private String clockTime;
  private String clockPlace;

  @Override
  public String toString() {
    return "CourseInformation{" +
            "courseId=" + courseId +
            ", courseName='" + courseName + '\'' +
            ", courseInformation='" + courseInformation + '\'' +
            ", teacherId=" + teacherId +
            ", venue='" + venue + '\'' +
            ", classId=" + classId +
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

  public String getCourseName() {
    return courseName;
  }

  public void setCourseName(String courseName) {
    this.courseName = courseName;
  }

  public String getCourseInformation() {
    return courseInformation;
  }

  public void setCourseInformation(String courseInformation) {
    this.courseInformation = courseInformation;
  }

  public long getTeacherId() {
    return teacherId;
  }

  public void setTeacherId(long teacherId) {
    this.teacherId = teacherId;
  }

  public String getVenue() {
    return venue;
  }

  public void setVenue(String venue) {
    this.venue = venue;
  }

  public long getClassId() {
    return classId;
  }

  public void setClassId(long classId) {
    this.classId = classId;
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
