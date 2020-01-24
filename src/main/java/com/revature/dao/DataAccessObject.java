package com.revature.dao;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.revature.model.Employee;
import com.revature.model.ExpenseReceipt;
import com.revature.model.Manager;
import com.revature.model.ReimbursementRequest;

public class DataAccessObject {
  
public static Connection conn;
  
  static {
    try {
      Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e1) {
      e1.printStackTrace();
    }
    try {
      conn = DriverManager.getConnection(
          System.getenv("connstring"), System.getenv("username"),System.getenv("password"));
      System.out.println("Connectioned to Database. We are good to go.");
    } catch (SQLException e) {
      System.out.println("We have failed to connection to the database");
    }
  }
  
 // loginEmployee. reused project 0
  public Employee loginEmployee(String uname,String pass)
  {
      Employee employee=null;
      PreparedStatement stmt = null;
      ResultSet rs = null;

      try {
        stmt = conn.prepareStatement("SELECT * FROM employee WHERE username = ? AND userpassword=?");
    
        stmt.setString(1, uname);
        stmt.setString(2, pass);
        if (stmt.execute()) {
          rs = stmt.getResultSet();
          if(rs.next())
          {
            int id = rs.getInt("id");
            String firstName = rs.getString("firstName");
            String lastName = rs.getString("lastName");
            String username = rs.getString("username");
            String phoneNumber = rs.getString("phoneNumber");
            String employeemail = rs.getString("employeemail");
            String social = rs.getString("social");
            String idBoss = rs.getString("idBoss");
            
            employee = new Employee();
            employee.setUsername(username);
            employee.setEmployeemail(employeemail);
            employee.setFirstName(firstName);
            employee.setId(id);
            employee.setIdBoss(idBoss);
            employee.setLastName(lastName);
            employee.setPhoneNumber(phoneNumber);
            employee.setSocial(social);
          }
        }
  }
      catch (SQLException e) {
        }
      
      return employee;
  }
  
  // loginManager.
  public Manager loginManager(String uname,String pass)
  {
      Manager manager =null;
      PreparedStatement stmt = null;
      ResultSet rs = null;

      try {
        stmt = conn.prepareStatement("SELECT * FROM manager WHERE username = ? AND userpassword=?");
    
        stmt.setString(1, uname);
        stmt.setString(2, pass);
        if (stmt.execute()) {
          rs = stmt.getResultSet();
          if(rs.next())
          {
            
            String firstName = rs.getString("firstName");
            String lastName = rs.getString("lastName");
            String username = rs.getString("username");
            String phoneNumber = rs.getString("phoneNumber");
            String employeemail = rs.getString("employeemail");
            String social = rs.getString("social");
            String idBoss = rs.getString("idBoss");
            
            manager = new Manager();
            manager.setUsername(username);
            manager.setEmployeemail(employeemail);
            manager.setFirstName(firstName);
            manager.setIdBoss(idBoss);
            manager.setLastName(lastName);
            manager.setPhoneNumber(phoneNumber);
            manager.setSocial(social);
          }
        }
  }
      catch (SQLException e) {
        }
      
      return manager;
  }

 // login function 
  public boolean auth(String uname,String pass)
  {
      boolean flag=false;
      PreparedStatement stmt = null;
      ResultSet rs = null;

      try {
        stmt = conn.prepareStatement("SELECT * FROM employee, manager  WHERE username = ? AND upass=?");
    
        stmt.setString(1, uname);
        stmt.setString(2, pass);
        if (stmt.execute()) {
          rs = stmt.getResultSet();
          while(rs.next())
          {
              flag=true;
          }
        }
  }
      catch (SQLException e) {
        }
      
      return flag;
  }
  
  // creating a new RR in database
  public int createNewReimbursementRequest(ReimbursementRequest rr) {
    int last_inserted_id = 0;
    String SQL_INSERT = "INSERT INTO reimbursementrequest(treatmentStatus, approvalStatus, purposeOfExpense, dateExpenseMade, treatedbymanagerid, employeeID) "
        + "VALUES (?,?,?,?,?,?) RETURNING id";
    try {
      PreparedStatement preparedStatement = conn.prepareStatement(SQL_INSERT);

      preparedStatement.setString(1,rr.getTreatmentStatus());
      preparedStatement.setString(2,rr.getApprovalStatus());
      preparedStatement.setString(3,rr.getPurposeOfExpense());
      preparedStatement.setString(4,rr.getDateExpenseMade());
      preparedStatement.setString(5,rr.getTreatedbymanagerid());
      preparedStatement.setInt(6,rr.getEmployeeID());

      preparedStatement.execute();
      
      ResultSet last_updated_person = preparedStatement.getResultSet();
      last_updated_person.next();
      last_inserted_id = last_updated_person.getInt(1);
      System.out.println(last_inserted_id);
      
    } catch (SQLException e) {
      System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return last_inserted_id;
  }
  
  // create a new ER in DB
  public void createNewExpenseReceipt(ExpenseReceipt er) {        
    String SQL_INSERT = "INSERT INTO expensereceipt(imagename, image, reimbursementrequestID) "
        + "VALUES (?,?,?)";
    try {
      PreparedStatement preparedStatement = conn.prepareStatement(SQL_INSERT);

      preparedStatement.setString(1,er.getImagename());
      byte[] imagesBytes = er.getImage();
      ByteArrayInputStream bais = new ByteArrayInputStream(imagesBytes);
      preparedStatement.setBinaryStream(2, bais, imagesBytes.length);
      
      preparedStatement.setInt(3,er.getReimbursementrequestID());

      int row = preparedStatement.executeUpdate();

      // rows affected
      System.out.println(row); //1
      
    } catch (SQLException e) {
      System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  // search for RR that have pending as their treatmentStatus for Employee
  public List<ReimbursementRequest> findPendingReimbursementRequestsForEmployee(int employeeID) {
    PreparedStatement stmt = null;
    ResultSet rs = null;
    List<ReimbursementRequest> rrs = new ArrayList<ReimbursementRequest>();

    try {
      stmt = conn.prepareStatement("SELECT * FROM reimbursementrequest WHERE employeeID = ? AND treatmentStatus=?");
  
      stmt.setInt(1, employeeID);
      stmt.setString(2, "pending");
      if (stmt.execute()) {
        rs = stmt.getResultSet();
        while(rs.next())
        {          
          int id = rs.getInt("id");
          String treatmentStatus = rs.getString("treatmentStatus");
          String approvalStatus = rs.getString("approvalStatus");
          String purposeOfExpense = rs.getString("purposeOfExpense");
          String dateExpenseMade = rs.getString("dateExpenseMade");
          String treatedbymanagerid = rs.getString("treatedbymanagerid");
          int localEmployeeID = rs.getInt("employeeID");
          
          ReimbursementRequest rr = new ReimbursementRequest();
          rr.setApprovalStatus(approvalStatus);
          rr.setDateExpenseMade(dateExpenseMade);
          rr.setEmployeeID(localEmployeeID);
          rr.setId(id);
          rr.setPurposeOfExpense(purposeOfExpense);
          rr.setTreatedbymanagerid(treatedbymanagerid);
          rr.setTreatmentStatus(treatmentStatus);
          
          rrs.add(rr);
        }
      }
      
    }
    catch (SQLException e) {
      }
    
    return rrs;
  }
  
  public ReimbursementRequest findReimbursementRequestByID(int reimbursementRequestID) {
    PreparedStatement stmt = null;
    ResultSet rs = null;
    ReimbursementRequest rr = null;

    try {
      stmt = conn.prepareStatement("SELECT * FROM reimbursementrequest WHERE id = ?");
  
      stmt.setInt(1, reimbursementRequestID);
      if (stmt.execute()) {
        rs = stmt.getResultSet();
        if(rs.next())
        {          
          int id = rs.getInt("id");
          String treatmentStatus = rs.getString("treatmentStatus");
          String approvalStatus = rs.getString("approvalStatus");
          String purposeOfExpense = rs.getString("purposeOfExpense");
          String dateExpenseMade = rs.getString("dateExpenseMade");
          String treatedbymanagerid = rs.getString("treatedbymanagerid");
          int localEmployeeID = rs.getInt("employeeID");
          
          rr = new ReimbursementRequest();
          rr.setApprovalStatus(approvalStatus);
          rr.setDateExpenseMade(dateExpenseMade);
          rr.setEmployeeID(localEmployeeID);
          rr.setId(id);
          rr.setPurposeOfExpense(purposeOfExpense);
          rr.setTreatedbymanagerid(treatedbymanagerid);
          rr.setTreatmentStatus(treatmentStatus);
         
        }
      }
      
    }
    catch (SQLException e) {
      }
    
    return rr;
  }


  // search for RR that have treated as their treatmentStatus for Employee
  public List<ReimbursementRequest> findTreatedReimbursementRequestsForAllEmployees() {
    PreparedStatement stmt = null;
    ResultSet rs = null;
    List<ReimbursementRequest> rrs = new ArrayList<ReimbursementRequest>();

    try {
      stmt = conn.prepareStatement("SELECT * FROM reimbursementrequest WHERE treatmentStatus=?");  
     
      stmt.setString(1, "treated");
      if (stmt.execute()) {
        rs = stmt.getResultSet();
        while(rs.next())
        {          
          int id = rs.getInt("id");
          String treatmentStatus = rs.getString("treatmentStatus");
          String approvalStatus = rs.getString("approvalStatus");
          String purposeOfExpense = rs.getString("purposeOfExpense");
          String dateExpenseMade = rs.getString("dateExpenseMade");
          String treatedbymanagerid = rs.getString("treatedbymanagerid");
          int localEmployeeID = rs.getInt("employeeID");
          
          ReimbursementRequest rr = new ReimbursementRequest();
          rr.setApprovalStatus(approvalStatus);
          rr.setDateExpenseMade(dateExpenseMade);
          rr.setEmployeeID(localEmployeeID);
          rr.setId(id);
          rr.setPurposeOfExpense(purposeOfExpense);
          rr.setTreatedbymanagerid(treatedbymanagerid);
          rr.setTreatmentStatus(treatmentStatus);
          
          rrs.add(rr);
        }
      }
      
    }
    catch (SQLException e) {
      }
    
    return rrs;

  }
  //Adam's ComicDaoPostgres code. 
  public List<ReimbursementRequest> findTreatedReimbursementRequestsForEmployee(int employeeID) {
    PreparedStatement stmt = null;
    ResultSet rs = null;
    List<ReimbursementRequest> rrs = new ArrayList<ReimbursementRequest>();

    try {
      stmt = conn.prepareStatement("SELECT * FROM reimbursementrequest WHERE employeeID=? AND treatmentStatus=?");  
     
      stmt.setInt(1, employeeID);
      stmt.setString(2, "treated");
      if (stmt.execute()) {
        rs = stmt.getResultSet();
        while(rs.next())
        {          
          int id = rs.getInt("id");
          String treatmentStatus = rs.getString("treatmentStatus");
          String approvalStatus = rs.getString("approvalStatus");
          String purposeOfExpense = rs.getString("purposeOfExpense");
          String dateExpenseMade = rs.getString("dateExpenseMade");
          String treatedbymanagerid = rs.getString("treatedbymanagerid");
          int localEmployeeID = rs.getInt("employeeID");
          
          ReimbursementRequest rr = new ReimbursementRequest();
          rr.setApprovalStatus(approvalStatus);
          rr.setDateExpenseMade(dateExpenseMade);
          rr.setEmployeeID(localEmployeeID);
          rr.setId(id);
          rr.setPurposeOfExpense(purposeOfExpense);
          rr.setTreatedbymanagerid(treatedbymanagerid);
          rr.setTreatmentStatus(treatmentStatus);
          
          rrs.add(rr);
        }
      }
      
    }
    catch (SQLException e) {
      }
    
    return rrs;

  }

  
  // search for Employee
  public Employee findEmployee(int employeeID) {
    
    PreparedStatement stmt = null;
    ResultSet rs = null;
    Employee employee = null;

    try {
      stmt = conn.prepareStatement("SELECT * FROM employee WHERE id = ?");
  
      stmt.setInt(1, employeeID);
      if (stmt.execute()) {
        rs = stmt.getResultSet();
        while(rs.next())
        {
          
          int id = rs.getInt("id");
          String firstName = rs.getString("firstName");
          String lastName = rs.getString("lastName");
          String username = rs.getString("username");
          String phoneNumber = rs.getString("phoneNumber");
          String employeemail = rs.getString("employeemail");
          String social = rs.getString("social");
          String idBoss = rs.getString("idBoss");
          
          employee = new Employee();
          employee.setUsername(username);
          employee.setEmployeemail(employeemail);
          employee.setFirstName(firstName);
          employee.setId(id);
          employee.setIdBoss(idBoss);
          employee.setLastName(lastName);
          employee.setPhoneNumber(phoneNumber);
          employee.setSocial(social);
          
        }
      }
}
    catch (SQLException e) {
      }
    return employee;

  }
  
  
  // update an Employee
  public void updateEmployee(Employee employee) {        
    String SQL_UPDATE = "UPDATE employee SET firstName=?, lastName=?, username=?, phoneNumber=?,  employeemail=?, social=?, idBoss=?  WHERE id=?";
        
    try {
      PreparedStatement preparedStatement = conn.prepareStatement(SQL_UPDATE);
      preparedStatement.setString(1, employee.getFirstName());
      preparedStatement.setString(2, employee.getLastName());
      preparedStatement.setString(3, employee.getUsername());
      preparedStatement.setString(4, employee.getPhoneNumber());
      
      preparedStatement.setString(5, employee.getEmployeemail());
      preparedStatement.setString(6, employee.getSocial());
      preparedStatement.setString(7, employee.getIdBoss());
      preparedStatement.setInt(8, employee.getId());

      int row = preparedStatement.executeUpdate();

      // rows affected
      System.out.println(row);
    }
    catch (SQLException e) {
      }
  }
  
  // search for all RR for all employees that have pending as their treatmentStatus
  public List<ReimbursementRequest> findPendingReimbursementRequestsForAllEmployees(){
    PreparedStatement stmt = null;
    ResultSet rs = null;
    List<ReimbursementRequest> rrs = new ArrayList<ReimbursementRequest>();

    try {
      stmt = conn.prepareStatement("SELECT * FROM reimbursementrequest WHERE treatmentStatus=?");
 
      stmt.setString(1, "pending");
      if (stmt.execute()) {
        rs = stmt.getResultSet();
        while(rs.next())
        {          
          int id = rs.getInt("id");
          String treatmentStatus = rs.getString("treatmentStatus");
          String approvalStatus = rs.getString("approvalStatus");
          String purposeOfExpense = rs.getString("purposeOfExpense");
          String dateExpenseMade = rs.getString("dateExpenseMade");
          String treatedbymanagerid = rs.getString("treatedbymanagerid");
          int localEmployeeID = rs.getInt("employeeID");
          
          ReimbursementRequest rr = new ReimbursementRequest();
          rr.setApprovalStatus(approvalStatus);
          rr.setDateExpenseMade(dateExpenseMade);
          rr.setEmployeeID(localEmployeeID);
          rr.setId(id);
          rr.setPurposeOfExpense(purposeOfExpense);
          rr.setTreatedbymanagerid(treatedbymanagerid);
          rr.setTreatmentStatus(treatmentStatus);
          
          rrs.add(rr);
        }
      }
      
    }
    catch (SQLException e) {
      }
    
    return rrs;

  }
  
  
  // update RR treatmentStatus and approvalStatus, and manager that treated it
  public void updateReimbursementRequest(ReimbursementRequest rr) {
    
    String SQL_UPDATE = "UPDATE reimbursementrequest SET treatmentStatus=?, approvalStatus=?, purposeOfExpense=?, dateExpenseMade=?, "
         + "treatedbymanagerid=?, employeeID=? WHERE id=?";
    
    try {
      PreparedStatement preparedStatement = conn.prepareStatement(SQL_UPDATE);
      preparedStatement.setString(1, rr.getTreatmentStatus());
      preparedStatement.setString(2, rr.getApprovalStatus());
      preparedStatement.setString(3, rr.getPurposeOfExpense());
      preparedStatement.setString(4, rr.getDateExpenseMade());
      
      preparedStatement.setString(5, rr.getTreatedbymanagerid());
      preparedStatement.setInt(6, rr.getEmployeeID());
      preparedStatement.setInt(7, rr.getId());

      int row = preparedStatement.executeUpdate();
      System.out.print("In update RR"  + rr.getPurposeOfExpense() + "  "+ row);
      // rows affected
      System.out.println(row);
    }
    catch (SQLException e) {
      }

  }
  
  // search for all ER that belongs to the given RR id
  public List<ExpenseReceipt> findExpenseReceipts(int reimbursementRequestID){
    
    PreparedStatement stmt = null;
    ResultSet rs = null;
    List<ExpenseReceipt> ers = new ArrayList<ExpenseReceipt>();
    
    try {
      stmt = conn.prepareStatement("SELECT * FROM expensereceipt WHERE reimbursementrequestID=?");
 
      stmt.setInt(1, reimbursementRequestID);
      if (stmt.execute()) {
        rs = stmt.getResultSet();
        while(rs.next())
        {        
          String imagename = rs.getString("imagename");
          byte[] imgBytes = rs.getBytes("image");        
          int reimbursementrequestID = rs.getInt("reimbursementrequestID");
          
          ExpenseReceipt er = new ExpenseReceipt();
          er.setImage(imgBytes);
          er.setImagename(imagename);
          er.setReimbursementrequestID(reimbursementrequestID);
          
          ers.add(er);
        }
      }
      
    }
    catch (SQLException e) {
      }
    
    return ers;

    
  }

  
  // find the Manager with that id
  public Manager findManager(String managerID) {
    PreparedStatement stmt = null;
    ResultSet rs = null;
    Manager manager = null;

    try {
      stmt = conn.prepareStatement("SELECT * FROM manager WHERE idBoss = ?");
  
      stmt.setString(1, managerID);
      if (stmt.execute()) {
        rs = stmt.getResultSet();
        while(rs.next())
        {
          
          String idBoss = rs.getString("idBoss");
          String firstName = rs.getString("firstName");
          String lastName = rs.getString("lastName");
          String username = rs.getString("username");
          String phoneNumber = rs.getString("phoneNumber");
          String employeemail = rs.getString("employeemail");
          String social = rs.getString("social");
          
          manager = new Manager();
          manager.setUsername(username);
          manager.setEmployeemail(employeemail);
          manager.setFirstName(firstName);
          
          manager.setIdBoss(idBoss);
          manager.setLastName(lastName);
          manager.setPhoneNumber(phoneNumber);
          manager.setSocial(social);
          
        }
      }
}
    catch (SQLException e) {
      }
    return manager;


  }
  
  
  // fetch all employees in database
  public List<Employee> fetchAllEmployees(){
    Statement stmt = null;
    ResultSet rs = null;
    List<Employee> employees = new ArrayList<Employee>();
    

    try {
      stmt = conn.createStatement();

      if (stmt.execute("SELECT * FROM employee")) {
        rs = stmt.getResultSet();
        while(rs.next())
        {
          
          int id = rs.getInt("id");
          String firstName = rs.getString("firstName");
          String lastName = rs.getString("lastName");
          String username = rs.getString("username");
          String phoneNumber = rs.getString("phoneNumber");
          String employeemail = rs.getString("employeemail");
          String social = rs.getString("social");
          String idBoss = rs.getString("idBoss");
          
          Employee employee = new Employee();
          
          employee = new Employee();
          employee.setUsername(username);
          employee.setEmployeemail(employeemail);
          employee.setFirstName(firstName);
          employee.setId(id);
          employee.setIdBoss(idBoss);
          employee.setLastName(lastName);
          employee.setPhoneNumber(phoneNumber);
          employee.setSocial(social);
          
          employees.add(employee);
        }
      }
}
    catch (SQLException e) {
      }
    return employees;

  }
  
  // find RRs that have given employeeID
  public List<ReimbursementRequest> findReimbursementRequestForEmployee(int employeeID){
    
    PreparedStatement stmt = null;
    ResultSet rs = null;
    List<ReimbursementRequest> rrs = new ArrayList<ReimbursementRequest>();

    try {
      stmt = conn.prepareStatement("SELECT * FROM reimbursementrequest WHERE employeeID=?");
 
      stmt.setInt(1, employeeID);
      if (stmt.execute()) {
        rs = stmt.getResultSet();
        while(rs.next())
        {          
          int id = rs.getInt("id");
          String treatmentStatus = rs.getString("treatmentStatus");
          String approvalStatus = rs.getString("approvalStatus");
          String purposeOfExpense = rs.getString("purposeOfExpense");
          String dateExpenseMade = rs.getString("dateExpenseMade");
          String treatedbymanagerid = rs.getString("treatedbymanagerid");
          int localEmployeeID = rs.getInt("employeeID");
          
          ReimbursementRequest rr = new ReimbursementRequest();
          rr.setApprovalStatus(approvalStatus);
          rr.setDateExpenseMade(dateExpenseMade);
          rr.setEmployeeID(localEmployeeID);
          rr.setId(id);
          rr.setPurposeOfExpense(purposeOfExpense);
          rr.setTreatedbymanagerid(treatedbymanagerid);
          rr.setTreatmentStatus(treatmentStatus);
          
          rrs.add(rr);
        }
      }
      
    }
    catch (SQLException e) {
      }
    
    return rrs;

  }
}
