package com.revature.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.revature.dao.DataAccessObject;
import com.revature.model.Manager;
import com.revature.model.ReimbursementRequest;

public class ApproveServlet extends HttpServlet {

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
      String rr = req.getParameter("rr");
      int rr_id = Integer.parseInt(rr);
      ReimbursementRequest rere = dao.findReimbursementRequestByID(rr_id);
      if(rere != null) {
        System.out.println("Found RR" + rere.getPurposeOfExpense());
        rere.setApprovalStatus("approved");
        rere.setTreatmentStatus("treated");
        rere.setTreatedbymanagerid(manager.getIdBoss());
        dao.updateReimbursementRequest(rere);        
      }
      else {
        System.out.println("Didnt Found RR");
      }
      resp.sendRedirect("PendingReimbursementRequestsAllEmployeesServlet");
    }
  }

}
