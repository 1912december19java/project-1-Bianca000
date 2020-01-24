package com.revature.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.revature.dao.DataAccessObject;
import com.revature.model.Manager;
import com.revature.model.ReimbursementRequest;
import com.revature.utils.HtmlUtils;

public class ViewReimbursementReqForEmployee extends HttpServlet {

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
      
      
      String empid = req.getParameter("empid");
      int emp_id_int = Integer.parseInt(empid);
      List<ReimbursementRequest> rrs = dao.findReimbursementRequestForEmployee(emp_id_int);
      
      HtmlUtils hu = new HtmlUtils();
      
      Vector av = new Vector();

      out.print(hu.createHtmlHeader("Reimbursement Requests for Employee"));

      out.print(hu.getTableHead("center", 1));
      out.print(hu.getTH("center", "Purpose Of Expense"));
      out.print(hu.getTH("center", "Date Expense was made"));
      out.print(hu.getTH("center", "Treatment status"));
      out.print(hu.getTH("center", "Approval Status"));
      
      for(ReimbursementRequest rr : rrs) {
        av.addElement(rr.getPurposeOfExpense());
        av.addElement(rr.getDateExpenseMade());
        av.addElement(rr.getTreatmentStatus());
        av.addElement(rr.getApprovalStatus());
      }
      
      out.print(hu.getTableContents("center", av, 4));
      out.print(hu.getHtmlFooter());
    }
  }

}
