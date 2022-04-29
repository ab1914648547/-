package org.develop.attendancesystem.entity;

public class StudentInformation {

  private long studentId;
  private String studentName;
  private String studentSex;
  private String studentHome;
  private String homePhone;
  private String studentDorm;
  private String studentMessage;
  private long classId;
  private String studentPass;
  private long studentAge;
  private String studentPhone;

  @Override
  public String toString() {
    return "Studentinformation{" +
            "studentId=" + studentId +
            ", studentName='" + studentName + '\'' +
            ", studentSex='" + studentSex + '\'' +
            ", studentHome='" + studentHome + '\'' +
            ", homePhone='" + homePhone + '\'' +
            ", studentDorm='" + studentDorm + '\'' +
            ", studentMessage='" + studentMessage + '\'' +
            ", classId=" + classId +
            ", studentPass='" + studentPass + '\'' +
            ", studentAge=" + studentAge +
            ", studentPhone='" + studentPhone + '\'' +
            '}';
  }

  public long getStudentId() {
    return studentId;
  }

  public void setStudentId(long studentId) {
    this.studentId = studentId;
  }


  public String getStudentName() {
    return studentName;
  }

  public void setStudentName(String studentName) {
    this.studentName = studentName;
  }


  public String getStudentSex() {
    return studentSex;
  }

  public void setStudentSex(String studentSex) {
    this.studentSex = studentSex;
  }


  public String getStudentHome() {
    return studentHome;
  }

  public void setStudentHome(String studentHome) {
    this.studentHome = studentHome;
  }


  public String getHomePhone() {
    return homePhone;
  }

  public void setHomePhone(String homePhone) {
    this.homePhone = homePhone;
  }


  public String getStudentDorm() {
    return studentDorm;
  }

  public void setStudentDorm(String studentDorm) {
    this.studentDorm = studentDorm;
  }


  public String getStudentMessage() {
    return studentMessage;
  }

  public void setStudentMessage(String studentMessage) {
    this.studentMessage = studentMessage;
  }


  public long getClassId() {
    return classId;
  }

  public void setClassId(long classId) {
    this.classId = classId;
  }


  public String getStudentPass() {
    return studentPass;
  }

  public void setStudentPass(String studentPass) {
    this.studentPass = studentPass;
  }


  public long getStudentAge() {
    return studentAge;
  }

  public void setStudentAge(long studentAge) {
    this.studentAge = studentAge;
  }


  public String getStudentPhone() {
    return studentPhone;
  }

  public void setStudentPhone(String studentPhone) {
    this.studentPhone = studentPhone;
  }

}
