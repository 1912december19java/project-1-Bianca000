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

public class AllEmployeesServlet extends HttpServlet {

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
      
      
      
      
      
      List<Employee> employees = dao.fetchAllEmployees();
      
      out.println("<P ALIGN='center'><TABLE BORDER=1>");
      // table header
      out.println("<TR>");
      
      out.println("<TH>Employee Name</TH>");
    
      out.println("</TR>");
      // the data
     for(Employee emp : employees) {    
       String name = "";
       if(emp != null) {
         name = emp.getFirstName() + " " + emp.getLastName();
       }
       out.println("<TR>");
       out.println("<TD><a href=ViewReimbursementReqForEmployee?empid=" + emp.getId() + ">" + name + "</a></TD>");  
         
       out.println("</TR>");
      }
      out.println("</TABLE></P>");
   
      
    }
  }

}
