package com.revature.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.dao.DataAccessObject;
import com.revature.model.Employee;
import com.revature.model.Manager;



public class LoginServlet extends HttpServlet {

 private static ObjectMapper om = new ObjectMapper();
 private DataAccessObject dao = new DataAccessObject();
  
  @Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp)
   throws ServletException, IOException {           
    
  
 System.out.println("Test is working");
 String Username = req.getParameter("Username");
 String Password = req.getParameter("Password");

 String passvalue = om.writeValueAsString(Username +" "+ Password);
 resp.getWriter().println(passvalue);
 
  } 
  
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
     throws ServletException, IOException {
      
    
   System.out.println("Test is working");
   String Username = req.getParameter("Username");
   String Password = req.getParameter("Password");
   
   // Call the loginEmployee method in DataAccessObject to check if login details is for an Employee
   Employee employee = dao.loginEmployee(Username, Password);
   if(employee != null) {
       // If true, create a Session for this Employee
      //https://www.javatpoint.com/http-session-in-session-tracking
     HttpSession session=req.getSession();  
     session.setAttribute("accesslevel", "employee");
     session.setAttribute("profile", employee); 
       // Redirect the user to employeeHomepage
     //https://www.javatpoint.com/sendRedirect()-method
     resp.sendRedirect("employeeHomepage.html");  
   }
   else {
   // Call the loginManager method in DataAccessObject to check if login details is for an Manager
     Manager manager = dao.loginManager(Username, Password);
     if(manager != null) {
       // If true, create a Session for this Manager
       HttpSession session=req.getSession();  
       session.setAttribute("accesslevel", "manager");
       session.setAttribute("profile", manager); 
       // Redirect the user to managerHomepage
       resp.sendRedirect("managerHomepage.html");  
     }
     else {
       // Redirect the user back to login page
       resp.sendRedirect("login.html");  
     }
     }
   } 
}
