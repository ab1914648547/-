package org.develop.attendancesystem.entity;



public class ClassInformation {

  private long classId;
  private String classMessage;
  private long teacherId;

  @Override
  public String toString() {
    return "ClassInformation{" +
            "classId=" + classId +
            ", classMessage='" + classMessage + '\'' +
            ", teacherId=" + teacherId +
            '}';
  }

  public long getClassId() {
    return classId;
  }

  public void setClassId(long classId) {
    this.classId = classId;
  }

  public String getClassMessage() {
    return classMessage;
  }

  public void setClassMessage(String classMessage) {
    this.classMessage = classMessage;
  }

  public long getTeacherId() {
    return teacherId;
  }

  public void setTeacherId(long teacherId) {
    this.teacherId = teacherId;
  }
}
