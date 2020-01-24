package com.revature.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.revature.dao.DataAccessObject;
import com.revature.model.ExpenseReceipt;
import com.revature.model.Manager;
import com.revature.model.ReimbursementRequest;

public class ViewImageServlet  extends HttpServlet {

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
      
      String rr = req.getParameter("rr");
      int rr_id = Integer.parseInt(rr);
      List<ExpenseReceipt> ers = dao.findExpenseReceipts(rr_id);
      if(ers.get(0) != null) {
        ExpenseReceipt er = ers.get(0);
        byte[] imageBytes = er.getImage();
        resp.setContentType("image/gif");             // see different MIME type
        ServletOutputStream sos = resp.getOutputStream();// for binary stream of image files
        
        sos.write(imageBytes);
        sos.close();
      }
    }
  }



}
