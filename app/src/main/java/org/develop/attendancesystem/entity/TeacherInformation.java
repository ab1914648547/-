package org.develop.attendancesystem.entity;


public class TeacherInformation {

  private long teacherId;
  private String teacherPass;
  private String teacherName;
  private String teacherPhoto;
  private String teacherPhone;
  private String teacherCollege;
  private String teacherMessage;

  @Override
  public String toString() {
    return "TeacherInformation{" +
            "teacherId=" + teacherId +
            ", teacherPass='" + teacherPass + '\'' +
            ", teacherName='" + teacherName + '\'' +
            ", teacherPhoto='" + teacherPhoto + '\'' +
            ", teacherPhone='" + teacherPhone + '\'' +
            ", teacherCollege='" + teacherCollege + '\'' +
            ", teacherMessage='" + teacherMessage + '\'' +
            '}';
  }

  public long getTeacherId() {
    return teacherId;
  }

  public void setTeacherId(long teacherId) {
    this.teacherId = teacherId;
  }

  public String getTeacherPass() {
    return teacherPass;
  }

  public void setTeacherPass(String teacherPass) {
    this.teacherPass = teacherPass;
  }

  public String getTeacherName() {
    return teacherName;
  }

  public void setTeacherName(String teacherName) {
    this.teacherName = teacherName;
  }

  public String getTeacherPhoto() {
    return teacherPhoto;
  }

  public void setTeacherPhoto(String teacherPhoto) {
    this.teacherPhoto = teacherPhoto;
  }

  public String getTeacherPhone() {
    return teacherPhone;
  }

  public void setTeacherPhone(String teacherPhone) {
    this.teacherPhone = teacherPhone;
  }

  public String getTeacherCollege() {
    return teacherCollege;
  }

  public void setTeacherCollege(String teacherCollege) {
    this.teacherCollege = teacherCollege;
  }

  public String getTeacherMessage() {
    return teacherMessage;
  }

  public void setTeacherMessage(String teacherMessage) {
    this.teacherMessage = teacherMessage;
  }
}
