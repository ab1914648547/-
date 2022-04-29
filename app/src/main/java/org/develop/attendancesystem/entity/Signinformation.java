package org.develop.attendancesystem.entity;

public class Signinformation  {

  private String studentId;
  private long courseId;
  private long teacherId;
  private String state;
  private String signMessage;
  private String signSite;
  private String signTime;
  private long classId;

  @Override
  public String toString() {
    return "Signinformation{" +
            "studentId=" + studentId +
            ", courseId=" + courseId +
            ", teacherId=" + teacherId +
            ", state='" + state + '\'' +
            ", signMessage='" + signMessage + '\'' +
            ", signSite='" + signSite + '\'' +
            ", signTime='" + signTime + '\'' +
            ", classId=" + classId +
            '}';
  }

  public String getStudentId() {
    return studentId;
  }

  public void setStudentId(String studentId) {
    this.studentId = studentId;
  }

  public long getCourseId() {
    return courseId;
  }

  public void setCourseId(long courseId) {
    this.courseId = courseId;
  }

  public long getTeacherId() {
    return teacherId;
  }

  public void setTeacherId(long teacherId) {
    this.teacherId = teacherId;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getSignMessage() {
    return signMessage;
  }

  public void setSignMessage(String signMessage) {
    this.signMessage = signMessage;
  }

  public String getSignSite() {
    return signSite;
  }

  public void setSignSite(String signSite) {
    this.signSite = signSite;
  }

  public String getSignTime() {
    return signTime;
  }

  public void setSignTime(String signTime) {
    this.signTime = signTime;
  }

  public long getClassId() {
    return classId;
  }

  public void setClassId(long classId) {
    this.classId = classId;
  }
}
