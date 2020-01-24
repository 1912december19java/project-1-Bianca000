package com.revature.controller;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.dao.DataAccessObject;
import com.revature.model.Employee;
import com.revature.utils.HtmlUtils;

public class UpdateUserInformationServlet extends HttpServlet {
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
    else if(accessLevel.equals("manager")) {
      // Redirect the user to managerHomepage
      resp.sendRedirect("managerHomepage.html");
    }
    else if(accessLevel.equals("employee")) {
      Employee employee = (Employee)session.getAttribute("profile");
      
      PrintWriter out = resp.getWriter();

      //https://way2java.com/servlets/creating-dynamically-filled-html-form-to-client-in-servlets/
      out.println("<body> <h3> Edit User Information </h3>");
      out.println("<form  method=post action=UpdateUserInformationServlet>");
 
  
        out.println("First Name <input type=text name=FirstName value=" + employee.getFirstName() + "> <br>");           
      
        out.println("Last Name <input type=text name=LastName value=" + employee.getLastName() + "> <br>");
    
        out.println("Phone Number <input type=text name=PhoneNumber value=" + employee.getPhoneNumber() + "> <br>");
    
        out.println("Email <input type=text  name=Email value=" + employee.getEmployeemail() + "> <br>");
        
        out.println("Social <input type=text  name=Social value=" + employee.getSocial() + "> <br>");
      
    
      out.println("<input type=submit  value=Update>");
      out.println("</form> </body>");
      out.close();
    
    }
  }
  
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
      Employee employee = (Employee)session.getAttribute("profile");
      
      String FirstName = req.getParameter("FirstName");
      String LastName = req.getParameter("LastName");
      String PhoneNumber = req.getParameter("PhoneNumber");
      String Email = req.getParameter("Email");
      String Social = req.getParameter("Social");
      
      employee.setFirstName(FirstName);
      employee.setLastName(LastName);
      employee.setPhoneNumber(PhoneNumber);
      employee.setEmployeemail(Email);
      employee.setSocial(Social);
      
      dao.updateEmployee(employee);
      
      session.setAttribute("profile", employee); 
      resp.sendRedirect("employeeHomepage.html");  
    }
    
   
    } 

}
