package com.revature.model;
//Creating a model for the SQL table Employee. Copy paste all SQL values into the Maven.
public class Employee {
  
  private int id;
  private String firstName;
  private String lastName;
  private String username;
  private String userpassword;
  private String phoneNumber;
  private String employeemail;
  private String social;
  private String idBoss;
  
  //generate getters and setters.
  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }
  public String getFirstName() {
    return firstName;
  }
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }
  public String getLastName() {
    return lastName;
  }
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
  public String getUsername() {
    return username;
  }
  public void setUsername(String username) {
    this.username = username;
  }
  public String getUserpassword() {
    return userpassword;
  }
  public void setUserpassword(String userpassword) {
    this.userpassword = userpassword;
  }
  public String getPhoneNumber() {
    return phoneNumber;
  }
  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }
  public String getEmployeemail() {
    return employeemail;
  }
  public void setEmployeemail(String employeemail) {
    this.employeemail = employeemail;
  }
  public String getSocial() {
    return social;
  }
  public void setSocial(String social) {
    this.social = social;
  }
  public String getIdBoss() {
    return idBoss;
  }
  public void setIdBoss(String idBoss) {
    this.idBoss = idBoss;
  }

}
