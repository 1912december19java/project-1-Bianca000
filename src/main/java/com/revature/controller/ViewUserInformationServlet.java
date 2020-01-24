package com.revature.controller;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.revature.dao.DataAccessObject;
import com.revature.model.Employee;
import com.revature.utils.HtmlUtils;

public class ViewUserInformationServlet extends HttpServlet {
  
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

    HtmlUtils hu = new HtmlUtils();

    out.print(hu.createHtmlHeader("User information for " + employee.getFirstName() + " " + employee.getLastName()));

    out.print(hu.getTableHead("center", 1));  
    
    Vector av = new Vector();

    av.addElement("First Name");
    av.addElement(employee.getFirstName());
    av.addElement("Last Name");
    av.addElement(employee.getLastName());
    
    av.addElement("Phone Number");
    av.addElement(employee.getPhoneNumber());
    av.addElement("Email");
    av.addElement(employee.getEmployeemail());
    
    av.addElement("Social");
    av.addElement(employee.getSocial());
    
    out.print(hu.getTableContents("center", av, 2));
    out.print(hu.getHtmlFooter());
  }
}

}

