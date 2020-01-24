package com.revature.controller;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.revature.dao.DataAccessObject;
import com.revature.model.Employee;
import com.revature.model.Manager;
import com.revature.model.ReimbursementRequest;

public class PendingReimbursementRequestsAllEmployeesServlet extends HttpServlet {

  private DataAccessObject dao = new DataAccessObject();
  
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
     throws ServletException, IOException {
    
    HttpSession session=req.getSession(); 
    String accessLevel = (String)session.getAttribute("accesslevel");
    if(accessLevel == null) {
      // Redirect the user back to login page
      resp.sendRedirect("login.html");  
    }
    else if(accessLevel.equals("employee")) {
      // Redirect the user to managerHomepage
      resp.sendRedirect("employeeHomepage.html");
    }
    else if(accessLevel.equals("manager")) {
      Manager manager = (Manager)session.getAttribute("profile");      
      
      PrintWriter out = resp.getWriter();
      List<ReimbursementRequest> rrs = dao.findPendingReimbursementRequestsForAllEmployees();
      //https://www.rgagnon.com/javadetails/java-0378.html
      out.println("<P ALIGN='center'><TABLE BORDER=1>");
      // table header
      out.println("<TR>");
      
      out.println("<TH>Employee Name</TH>");
        out.println("<TH>Purpose Of Expense</TH>");
        out.println("<TH>Date Expense was made</TH>");
        out.println("<TH>Approve/TH>");
        out.println("<TH>Reject</TH>");
        out.println("<TH>View Receipt</TH>");
    
      out.println("</TR>");
      // the data
     for(ReimbursementRequest rr : rrs) {
       Employee emp = dao.findEmployee(rr.getEmployeeID());
       String name = "";
       if(emp != null) {
         name = emp.getFirstName() + " " + emp.getLastName();
       }
       out.println("<TR>");
       out.println("<TD>" + name + "</TD>"); 
       out.println("<TD>" + rr.getPurposeOfExpense() + "</TD>");
       out.println("<TD>" + rr.getDateExpenseMade() + "</TD>");
       out.println("<TD><a href=ApproveServlet?rr=" + rr.getId() + ">Approve</a> </TD>"); 
       out.println("<TD><a href=RejectServlet?rr=" + rr.getId() + ">Reject</a> </TD>"); 
       out.println("<TD><a href=ViewImageServlet?rr=" + rr.getId() + ">View Image</a> </TD>"); 
         
       out.println("</TR>");
      }
      out.println("</TABLE></P>");
    }
  }
}

