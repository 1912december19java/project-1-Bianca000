package com.revature.model;
//Creating a model for the SQL table ExpenseRec. Copy paste all SQL values into the Maven.
public class ExpenseReceipt {

  private String imagename;
  private byte[] image;
  private int reimbursementrequestID;
  
  public String getImagename() {
    return imagename;
  }
  public void setImagename(String imagename) {
    this.imagename = imagename;
  }
  public byte[] getImage() {
    return image;
  }
  public void setImage(byte[] image) {
    this.image = image;
  }
  public int getReimbursementrequestID() {
    return reimbursementrequestID;
  }
  public void setReimbursementrequestID(int reimbursementrequestID) {
    this.reimbursementrequestID = reimbursementrequestID;
  }
  
  
}
