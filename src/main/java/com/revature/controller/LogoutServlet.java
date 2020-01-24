package com.revature.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogoutServlet extends HttpServlet {
  
  protected void doGet(HttpServletRequest request, HttpServletResponse response)  
      throws ServletException, IOException {              

      HttpSession session=request.getSession();  
      session.invalidate();  

      //https://www.javatpoint.com/servlet-http-session-login-and-logout-example
      // Redirect the user back to login page
      response.sendRedirect("login.html");    
}  

}
