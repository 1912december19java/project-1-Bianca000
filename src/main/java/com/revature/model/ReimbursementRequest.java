package com.revature.model;
//Creating a model for the SQL table ReimbursementRequest. Copy paste all SQL values into the Maven.
public class ReimbursementRequest {
  
  private int id;
  private String treatmentStatus;
  private String approvalStatus;
  private String purposeOfExpense;
  private String dateExpenseMade;
  private int employeeID;
  private String treatedbymanagerid;
  
  public String getTreatedbymanagerid() {
    return treatedbymanagerid;
  }
  public void setTreatedbymanagerid(String treatedbymanagerid) {
    this.treatedbymanagerid = treatedbymanagerid;
  }
  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }
  public String getTreatmentStatus() {
    return treatmentStatus;
  }
  public void setTreatmentStatus(String treatmentStatus) {
    this.treatmentStatus = treatmentStatus;
  }
  public String getApprovalStatus() {
    return approvalStatus;
  }
  public void setApprovalStatus(String approvalStatus) {
    this.approvalStatus = approvalStatus;
  }
  public String getPurposeOfExpense() {
    return purposeOfExpense;
  }
  public void setPurposeOfExpense(String purposeOfExpense) {
    this.purposeOfExpense = purposeOfExpense;
  }
  public String getDateExpenseMade() {
    return dateExpenseMade;
  }
  public void setDateExpenseMade(String dateExpenseMade) {
    this.dateExpenseMade = dateExpenseMade;
  }
  public int getEmployeeID() {
    return employeeID;
  }
  public void setEmployeeID(int employeeID) {
    this.employeeID = employeeID;
  }
  
  

}
