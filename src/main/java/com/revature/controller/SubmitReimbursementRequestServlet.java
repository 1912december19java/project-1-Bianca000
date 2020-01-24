package com.revature.controller;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import org.apache.commons.io.IOUtils;
import com.revature.dao.DataAccessObject;
import com.revature.model.Employee;
import com.revature.model.ExpenseReceipt;
import com.revature.model.ReimbursementRequest;

@MultipartConfig
public class SubmitReimbursementRequestServlet extends HttpServlet {

  private DataAccessObject dao = new DataAccessObject();
  
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
     throws ServletException, IOException {
    
    HttpSession session=req.getSession(); 
    String accessLevel = (String)session.getAttribute("accesslevel");
    if(accessLevel == null) {
      // Redirect the user back to login page
      resp.sendRedirect("login.html");  
    }
    else if(accessLevel.equals("manager")) {
      // Redirect the user to managerHomepage
      resp.sendRedirect("managerHomepage.html");
    }
    else if(accessLevel.equals("employee")) {
      // serve user
      // get image as bytes
      Part filePart = req.getPart("Uploadimage");
      InputStream filecontent = filePart.getInputStream();
      byte[] fileAsByteArray = IOUtils.toByteArray(filecontent);
      
      // get the other variables
      String PurposeofExpense = req.getParameter("PurposeofExpense");
      String Date = req.getParameter("Date");
      
      ReimbursementRequest rr =  new ReimbursementRequest();
      rr.setTreatmentStatus("pending");
      rr.setApprovalStatus("");
      rr.setPurposeOfExpense(PurposeofExpense);
      rr.setDateExpenseMade(Date);
      
      Employee employee = (Employee)session.getAttribute("profile");
      rr.setEmployeeID(employee.getId());
      rr.setTreatedbymanagerid("");
      
      int reimbursementRequestID = dao.createNewReimbursementRequest(rr);
      
      ExpenseReceipt er = new ExpenseReceipt();
      er.setImage(fileAsByteArray);
      er.setImagename(PurposeofExpense);
      er.setReimbursementrequestID(reimbursementRequestID);
      dao.createNewExpenseReceipt(er);
      
      resp.sendRedirect("employeeHomepage.html");  
    }
  }
}
