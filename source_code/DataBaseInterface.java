import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DataBaseInterface {
    
	public static String Username;
	public static String Password;
	public static Statement statement;
	public static PreparedStatement prestatement;
	public static Connection connection;
	
	public DataBaseInterface() {
	}
	
	public static void main(String[] args) throws IOException, SQLException {
		
		while(true) {
			
			String job = LoginUI();	
			switch(job) {
			   case"Admin": AdminUI(); break;
			   case"HR":   HRUI(); break;
			   case"Engineer":  EngineerUI(); break;
			   case"Sale": SaleUI();  break;
			   default: break;
			}
				
			
		}
	}
	
	
	public static String LoginUI() throws IOException, SQLException{
		System.out.println("Please input your userID and password!");
		System.out.print("Username:");
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Username = reader.readLine();
        System.out.print("Password:");
        Password = reader.readLine();
        String job = "";
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", Username, Password); 
        System.out.println("Connected to PostgreSQL database!");
        statement = connection.createStatement();
            prestatement = connection.prepareStatement("SELECT privilege FROM jobtype WHERE user_ID = ?");
            prestatement.setString(1, Username);
            ResultSet resultSet =prestatement.executeQuery();
           while (resultSet.next()) {
            	 job = resultSet.getString("privilege");
            	// System.out.println(job);
               // System.out.printf("%-30.30s  %-30.30s%n", resultSet.getString("job_type"), resultSet.getString("price"));
            }
        
        return job;
 
	}
	public static void AdminUI() throws IOException, SQLException{
		 while(true) {
		  Boolean goto_login = false;
		  System.out.println("Admin Operations:");
		  System.out.println("1.Create a new employee");
		  System.out.println("2.Set Table");
		  System.out.println("3.Grant Access");
		  System.out.println("4.Business Analytic");
		  System.out.println("5.SQl Operations");
		  System.out.println("6.Logout");
		  System.out.println("Please select a Admin operations(for exmaple: 1):");
		  
		  BufferedReader reader = new BufferedReader (new InputStreamReader(System.in));
		  String selection = reader.readLine();
		  switch(selection){
		  case"1": newEmployee(); break;
		  case"2": SetTable(); break;
		  case"3": GrantAccess(); break;
		  case"4": BusinessAnalytic(); break;
		  case"5": inputSQL(); break;
		  case"6": goto_login = true; break;
		  default: break;
		  }
		  if(goto_login) {
		    	statement.close();
		    	prestatement.close();
		    	connection.close();
		    	 break;
		    }
		    
		 }
		 
		}
	
		public static void newEmployee() throws IOException, SQLException{
			 System.out.println("Please enter Employee ID (Example e000000001 length is 10).");
			 BufferedReader reader3 = new BufferedReader (new InputStreamReader(System.in));
			 String EmployeeID = reader3.readLine();
			 System.out.println("Please enter privilege of Admin, HR, Engineer, Sale:");
			 BufferedReader reader4 = new BufferedReader (new InputStreamReader(System.in));
			 String Privilege = reader4.readLine();
			 System.out.println("Please enter employee first name");
			 BufferedReader reader6 = new BufferedReader (new InputStreamReader(System.in));
			 String firstName = reader6.readLine();
			 System.out.println("Please enter employee last name");
			 BufferedReader reader7 = new BufferedReader (new InputStreamReader(System.in));
			 String lastName = reader7.readLine();
			 System.out.println("Please enter employee ssn");
			 BufferedReader reader5 = new BufferedReader (new InputStreamReader(System.in));
			 String SSN = reader5.readLine();
			 System.out.println("Please enter the amount of Salary");
			 BufferedReader reader11 = new BufferedReader (new InputStreamReader(System.in));
			 double Salary = Double.parseDouble(reader11.readLine());
			 System.out.println("Please enter employee salary type (hourly or salaried)");
			 BufferedReader reader8 = new BufferedReader (new InputStreamReader(System.in));
			 String salaryType = reader8.readLine();
			 System.out.println("Please enter employee job type(HR, Sale, Engineer)");
			 BufferedReader reader9 = new BufferedReader (new InputStreamReader(System.in));
			 String jobType = reader9.readLine();
			 System.out.println("Please enter UserID:");
			 BufferedReader reader = new BufferedReader (new InputStreamReader(System.in));
			 String NEWuserID = reader.readLine();
			 System.out.println("Please enter password: ");
			 BufferedReader reader2 = new BufferedReader (new InputStreamReader(System.in));
			 String NEWpassword = reader2.readLine();
			 String Newpassword = "\'"+NEWpassword+"\'";
			 System.out.println("Please enter Role of new user: ");
			 BufferedReader reader10 = new BufferedReader (new InputStreamReader(System.in));
			 String NEWrole = reader10.readLine();
			 prestatement = connection.prepareStatement("insert into employeelogin values(?,?,?,?,?,?,?,?,?)");
			 prestatement.setString(1, EmployeeID);
			 prestatement.setString(2, NEWuserID);
			 prestatement.setString(3, Privilege);
			 prestatement.setString(4, firstName);
			 prestatement.setString(5, lastName);
			 prestatement.setString(6, SSN);
			 prestatement.setDouble(7, Salary);
			 prestatement.setString(8, salaryType);
			 prestatement.setString(9, jobType);
			 prestatement.executeUpdate();
			 statement.execute("create user " +NEWuserID+" with password "+Newpassword+ " inherit login");
			
			 if(Privilege == "Admin") {
				 statement.execute("ALTER ROLE "+ "\""+NEWuserID +"\"" + " SUPERUSER CREATEDB CREATEROLE INHERIT LOGIN");
			 }
			 statement.execute("grant "+NEWrole+" to "+NEWuserID);
			System.out.printf("NEWuserID: %s Newpassword: %s. this is your new userID and password please remember it!\n", NEWuserID, Newpassword);
			}


		
		public static void SetTable() throws IOException, SQLException{
			 while(true) {
				  Boolean goto_login = false;
				  System.out.println("Table Operations:");
				  System.out.println("1.View Tables");
				  System.out.println("2.Insert Tuples into Tables");
				  System.out.println("3.Delete Tuples into Tables");
				  System.out.println("4.Other Operations");
				  System.out.println("5.Go Back");
				  System.out.println("Please select one operations(for exmaple: 1):");
				  
				  BufferedReader reader = new BufferedReader (new InputStreamReader(System.in));
				  String selection = reader.readLine();
				  switch(selection){
				  case"1": ViewTables(); break;
				  case"2": InsertTables(); break;
				  case"3": DeleteTables(); break;
				  case"4": Others(); break;
				  case"5": goto_login = true; break;
				  default: break;
				  }
				  if(goto_login) {
				    	 break;
				    }
			 }
		}
		
		public static void ViewTables()throws IOException, SQLException{
			 System.out.println("Please type in your SQL code here to view tables:");
			 BufferedReader reader = new BufferedReader (new InputStreamReader(System.in));
			 String ViewCode = reader.readLine();
			 ResultSet rs = statement.executeQuery(ViewCode);
			 ResultSetMetaData metaData = rs.getMetaData();
			 int columns = metaData.getColumnCount();	 
			 for(int i=1; i<=columns;i++) {
					System.out.printf( "%-25.25s  ", metaData.getColumnName(i));
				 }
			 System.out.printf("\n");
			 while(rs.next()) {
				 for(int i=1; i<=columns;i++) {
					System.out.printf( "%-25.25s  ", rs.getObject(i).toString());
				 }
				 System.out.printf("\n");
			 }
			 
		}
		
		public static void InsertTables()throws IOException, SQLException{
			 System.out.println("Please type in your SQL code here to insert tuples into tables:");
			 BufferedReader reader = new BufferedReader (new InputStreamReader(System.in));
			 String InsertCode = reader.readLine();
			 statement.executeUpdate(InsertCode);
		}
		
		public static void DeleteTables()throws IOException, SQLException{
			 System.out.println("Please type in your SQL code here to delete tuples from tables:");
			 BufferedReader reader = new BufferedReader (new InputStreamReader(System.in));
			 String DeleteCode = reader.readLine();
			 statement.executeUpdate(DeleteCode);
		}
		
		public static void Others() throws IOException, SQLException{
			 System.out.println("Please type in your SQL code here:");
			 BufferedReader reader = new BufferedReader (new InputStreamReader(System.in));
			 String OtherCode = reader.readLine();
			 statement.execute(OtherCode);
		}
		
		public static void GrantAccess() throws IOException, SQLException{
		 System.out.println("Please type in your SQL code here to grant access to Employee:");
		 BufferedReader reader = new BufferedReader (new InputStreamReader(System.in));
		 String GrantCode = reader.readLine();
		 statement.execute(GrantCode);
		}
		
		
		public static void BusinessAnalytic() throws IOException, SQLException{
		 while(true) {
		  Boolean goto_login = false;
		  System.out.println("Buisness Analytics:");
		  System.out.println("1.Total revenue from sale");
		  System.out.println("2.Customer model bought and quantity to make prediction and understand trending");
		  System.out.println("3.For each order, the associated parts and available inventory");
		  System.out.println("4.Business report including Expense report, employee showing salary, bonus expense and part cost");
		  System.out.println("5.Go Back");
		  System.out.println("Please select a Admin operations(for exmaple: 1):");
		  
		  BufferedReader reader = new BufferedReader (new InputStreamReader(System.in));
		  String selection = reader.readLine();
		  switch(selection){
		  case"1": Revenue(); break;
		  case"2": ModelReport(); break;
		  case"3": OrderInventory(); break;
		  case"4": ExpenseReport(); break;
		  case"5": goto_login = true; break;
		  default: break;
		  }
		  if(goto_login) {
		    	 break;
		    }
		    
		 }
		}
		
		public static void Revenue() throws IOException, SQLException{
		System.out.println("This is the Revenue Table");
        ResultSet rs = statement.executeQuery("SELECT * FROM TotalRevenue");
        System.out.printf("%-15.15s  %-15.15s\n", "Revenue","year");
        while (rs.next()) {
           double Revenue = rs.getDouble("Revenue");
           String Revenue_string = Double.toString(Revenue);
           int year = rs.getInt("year");
           String year_string = Integer.toString(year);          
           System.out.printf("%-15.15s  %-15.15s\n", Revenue_string,year_string);

         }
        System.out.printf("\n");

		}
		
		public static void ModelReport() throws IOException, SQLException{
			 System.out.println("This is the Model Report!");
			 ResultSet rs = statement.executeQuery("select * from modelReport");
			 System.out.printf("%-15.15s  %-15.15s  %-15.15s\n", "model_ID","Sale_Number","year");
			 while (rs.next()) {
		           String model_ID = rs.getString("model_ID");
		           int sum = rs.getInt("sum");
		           String sum_string = Integer.toString(sum);
		           int year = rs.getInt("year");
		           String year_string = Integer.toString(year);
		           System.out.printf("%-15.15s  %-15.15s  %-15.15s\n", model_ID,sum_string,year_string);
		         }
	         System.out.printf("\n");

		}
		
		public static void OrderInventory() throws IOException, SQLException{
		 System.out.println("This is the Order Report associated with Inventory!");
		 System.out.printf("%-25.25s  %-25.25s  %-25.25s  %-25.25s\n", "model_ID","order_ID","purchasing_amount","available_inventory");
		 ResultSet rs = statement.executeQuery("select * from orderInventory");
		 while (rs.next()) {
	           String model_ID = rs.getString("model_ID");
	           String order_ID = rs.getString("order_ID");
	           int purchasing_amount = rs.getInt("purchasing_amount");
	           String purchasing_amount_string = Integer.toString(purchasing_amount);
	           int  available_inventory = rs.getInt("number");
	           String available_inventory_string = Integer.toString(available_inventory);
	           System.out.printf("%-25.25s  %-25.25s  %-25.25s  %-25.25s\n", model_ID,order_ID,purchasing_amount_string,available_inventory_string);
	         }
         System.out.printf("\n");

		}
		
		public static void ExpenseReport() throws IOException, SQLException{
		 System.out.println("This is the Expense Report!");
		 ResultSet rs = statement.executeQuery("select * from totalExpense");
		 System.out.printf("%-25.25s  %-25.25s\n", "Total_Expense", "year");
		 while (rs.next()) {
	           double Total_Expense = rs.getDouble("Total_Expense");
	           String Total_Expense_string = Double.toString(Total_Expense);
	           int year = rs.getInt("year");
	           String year_string = Integer.toString(year);
	           System.out.printf("%-25.25s  %-25.25s\n", Total_Expense_string,year_string);
	         }
         System.out.printf("\n");

		}
		
		public static void HRUI() throws IOException, SQLException{
			 while(true) {
			  Boolean goto_login = false;
			  System.out.println("HR Operations:");
			  System.out.println("1.View Employee");
			  System.out.println("2.Search in Employee"); //firstname, job type, loginID
			  System.out.println("3.Update Employee");  //salary, privilege, job type (search by login id)
			  System.out.println("4.View employee and associated sales number");
	 		  System.out.println("5.SQl Operations");
			  System.out.println("6.Logout");
			  System.out.println("Please select a HR operations(for exmaple: 1):");
			  
			  BufferedReader reader = new BufferedReader (new InputStreamReader(System.in));
			  String selection = reader.readLine();
			  switch(selection){
			  case"1": viewEmployee(); break;
			  case"2": searchEmployee(); break;
			  case"3": UpdateEmployee(); break;
			  case"4": EmployeeSale(); break;
			  case"5": inputSQL(); break;
			  case"6": goto_login = true; break;
			  default: break;
			  }
			  if(goto_login) {
			    	statement.close();
			    	prestatement.close();
			    	connection.close();
			    	 break;
			    }
			    
			 }
			 
			}
			public static void searchEmployee() throws IOException, SQLException{
				while(true) {
					  Boolean goto_login = false;
					  System.out.println("Search Employee:");
					  System.out.println("1.Search by First Name of Employee");
					  System.out.println("2.Search by job type of Employee"); 
					  System.out.println("3.Search by User ID");
					  System.out.println("4.Go back");
					  System.out.println("Please select a HR operations(for exmaple: 1)");
					  
					  BufferedReader reader = new BufferedReader (new InputStreamReader(System.in));
					  String selection = reader.readLine();
					  switch(selection){
					  case"1": firstSearch(); break;
					  case"2": typeSearch(); break;
					  case"3": IDSearch(); break;
					  case"4": goto_login = true; break;
					  default: break;
					  }
					  if(goto_login) {
					    	 break;
					    }
					    
				}
			}
			
			public static void firstSearch() throws IOException, SQLException{
				 System.out.println("Search for employee by first name.");
				 System.out.println("Please enter the employee's first name:");
				 BufferedReader reader = new BufferedReader (new InputStreamReader(System.in));
				 String FirstSearch = reader.readLine();
				 String FirstName = "\'"+FirstSearch+"\'";
				 System.out.println("Here is the result for "+FirstName);
			        ResultSet rs = statement.executeQuery("select * from EmployeeLogin where first_name = "+FirstName);
			        System.out.printf("%-15.15s  %-15.15s  %-15.15s  %-15.15s  %-15.15s  %-15.15s  %-15.15s  %-15.15s  %-15.15s\n", "Employee ID", "User ID","Privilege","First Name","Last Name","SSN","Salary","Salary Type","Job Type" );
		            System.out.printf("\n");
		            
			        while (rs.next()) {
			            String EmployeeID = rs.getString("employee_ID");
			            String UserID = rs.getString("user_ID");
			            String privilege = rs.getString("privilege");
			            String firstName = rs.getString("first_name");
			            String lastname = rs.getString("last_name");
			            String ssn = rs.getString("ssn");
			            double Salary = rs.getDouble("salary");
			            String salary_string = Double.toString(Salary);
			            String salarytype = rs.getString("salary_type");
			            String jobtype = rs.getString("job_type");
			            System.out.printf("%-15.15s  %-15.15s  %-15.15s  %-15.15s  %-15.15s  %-15.15s  %-15.15s  %-15.15s  %-15.15s\n", EmployeeID, UserID,privilege,firstName,lastname,ssn,salary_string,salarytype,jobtype );
			            System.out.printf("\n");
			         }
			}
			
			public static void typeSearch() throws IOException, SQLException{
				System.out.println("Search for employee by job type.");
				 System.out.println("Please enter the employee's job type:");
				 BufferedReader reader = new BufferedReader (new InputStreamReader(System.in));
				 String jobSearch = reader.readLine();
				 String typeSearch = "\'"+jobSearch+"\'";
				 System.out.println("Here is the result for "+typeSearch);
			        ResultSet rs = statement.executeQuery("select * from EmployeeLogin where job_type = "+typeSearch);
			        System.out.printf("%-15.15s  %-15.15s  %-15.15s  %-15.15s  %-15.15s  %-15.15s  %-15.15s  %-15.15s  %-15.15s\n", "Employee ID", "User ID","Privilege","First Name","Last Name","SSN","Salary","Salary Type","Job Type" );
		            System.out.printf("\n");
		            
			        while (rs.next()) {
			            String EmployeeID = rs.getString("employee_ID");
			            String UserID = rs.getString("user_ID");
			            String privilege = rs.getString("privilege");
			            String firstName = rs.getString("first_name");
			            String lastname = rs.getString("last_name");
			            String ssn = rs.getString("ssn");
			            double Salary = rs.getDouble("salary");
			            String salary_string = Double.toString(Salary);
			            String salarytype = rs.getString("salary_type");
			            String jobtype = rs.getString("job_type");
			            System.out.printf("%-15.15s  %-15.15s  %-15.15s  %-15.15s  %-15.15s  %-15.15s  %-15.15s  %-15.15s  %-15.15s\n", EmployeeID, UserID,privilege,firstName,lastname,ssn,salary_string,salarytype,jobtype );
			            System.out.printf("\n");
			         }
			}
			
			public static void IDSearch() throws IOException, SQLException{
				System.out.println("Search for employee by User ID.");
				 System.out.println("Please enter the employee's User ID:");
				 BufferedReader reader = new BufferedReader (new InputStreamReader(System.in));
				 String IDSearch = reader.readLine();
				 String UserSearch = "\'"+IDSearch+"\'";
				 System.out.println("Here is the result for "+UserSearch);
			        ResultSet rs = statement.executeQuery("select * from EmployeeLogin where user_ID = "+UserSearch);
			        System.out.printf("%-15.15s  %-15.15s  %-15.15s  %-15.15s  %-15.15s  %-15.15s  %-15.15s  %-15.15s  %-15.15s\n", "Employee ID", "User ID","Privilege","First Name","Last Name","SSN","Salary","Salary Type","Job Type" );
		            System.out.printf("\n");
		            
			        while (rs.next()) {
			            String EmployeeID = rs.getString("employee_ID");
			            String UserID = rs.getString("user_ID");
			            String privilege = rs.getString("privilege");
			            String firstName = rs.getString("first_name");
			            String lastname = rs.getString("last_name");
			            String ssn = rs.getString("ssn");
			            double Salary = rs.getDouble("salary");
			            String salary_string = Double.toString(Salary);
			            String salarytype = rs.getString("salary_type");
			            String jobtype = rs.getString("job_type");
			            System.out.printf("%-15.15s  %-15.15s  %-15.15s  %-15.15s  %-15.15s  %-15.15s  %-15.15s  %-15.15s  %-15.15s\n", EmployeeID, UserID,privilege,firstName,lastname,ssn,salary_string,salarytype,jobtype );
			            System.out.printf("\n");
			         }
			}
			
			public static void viewEmployee() throws IOException, SQLException{
				System.out.println("Here are the Employee information:");
		         ResultSet rs = statement.executeQuery("SELECT * FROM EmployeeLogin");
		         System.out.printf("%-15.15s  %-15.15s  %-15.15s  %-15.15s  %-15.15s  %-15.15s  %-15.15s  %-15.15s  %-15.15s\n", "Employee ID", "User ID","Privilege","First Name","Last Name","SSN","Salary","Salary Type","Job Type" );
		            System.out.printf("\n");
		            
		         while (rs.next()) {
		            String EmployeeID = rs.getString("employee_ID");
		            String UserID = rs.getString("user_ID");
		            String privilege = rs.getString("privilege");
		            String firstName = rs.getString("first_name");
		            String lastname = rs.getString("last_name");
		            String ssn = rs.getString("ssn");
		            double Salary = rs.getDouble("salary");
		            String salary_string = Double.toString(Salary);
		            String salarytype = rs.getString("salary_type");
		            String jobtype = rs.getString("job_type");
		            System.out.printf("%-15.15s  %-15.15s  %-15.15s  %-15.15s  %-15.15s  %-15.15s  %-15.15s  %-15.15s  %-15.15s\n", EmployeeID, UserID,privilege,firstName,lastname,ssn,salary_string,salarytype,jobtype );
		            System.out.printf("\n");
		            }
			}
			
			public static void UpdateEmployee() throws IOException, SQLException{ //salary, privilege, job type (search by login id)
				while(true) {
					  Boolean goto_login = false;
					  System.out.println("Update Employee:");
					  System.out.println("1.Update Employee Salary");
					  System.out.println("2.Update Employee Job Type");
					  System.out.println("3.Go back");
					  System.out.println("Please select a HR operations(for exmaple: 1)");
					  
					  BufferedReader reader = new BufferedReader (new InputStreamReader(System.in));
					  String selection = reader.readLine();
					  switch(selection){
					  case"1": salaryUpdate(); break;
					  case"2": jobUpdate(); break;
					  case"3": goto_login = true; break;
					  default: break;
					  }
					  if(goto_login) {
					    	 break;
					    }
					    
				}
			}
			//update EmployeeLogin set salary = 5000000 where employee_id = 'e100000001';
			public static void salaryUpdate() throws IOException, SQLException{
				System.out.println("update employee salary.");
				 System.out.println("Please enter the employee's ID:");
				 BufferedReader reader = new BufferedReader (new InputStreamReader(System.in));
				 String EmployeeID = reader.readLine();
				 System.out.println("Please enter the new salary:");
				 BufferedReader reader2 = new BufferedReader (new InputStreamReader(System.in));
				 int Salary = Integer.parseInt(reader2.readLine());
				 
				 PreparedStatement pStmt = connection.prepareStatement("update EmployeeLogin set salary = ? where employee_id = ?");
				 pStmt.setDouble(1, Salary);
				 pStmt.setString(2, EmployeeID);
				 pStmt.executeUpdate();
				 System.out.println(EmployeeID+" new Salary has been changed to "+Salary);
				 
			}
			
			public static void privilegeUpdate() throws IOException, SQLException{
				System.out.println("update employee privilege.");
				 System.out.println("Please enter the employee's ID:");
				 BufferedReader reader = new BufferedReader (new InputStreamReader(System.in));
				 String EmployeeID = reader.readLine();
				 System.out.println("Please enter the new privilege:");
				 BufferedReader reader2 = new BufferedReader (new InputStreamReader(System.in));
				 String privilege = reader2.readLine();
				 
				 PreparedStatement pStmt = connection.prepareStatement("update EmployeeLogin set privilege = ? where employee_id = ?");
				 pStmt.setString(1, privilege);
				 pStmt.setString(2, EmployeeID);
				 pStmt.executeUpdate();
				 System.out.println(EmployeeID+" new privilege has been changed to "+privilege);
			}
			
			public static void jobUpdate() throws IOException, SQLException{
				System.out.println("update employee privilege.");
				 System.out.println("Please enter the employee's ID:");
				 BufferedReader reader = new BufferedReader (new InputStreamReader(System.in));
				 String EmployeeID = reader.readLine();
				 System.out.println("Please enter the new Job Type:");
				 BufferedReader reader2 = new BufferedReader (new InputStreamReader(System.in));
				 String job = reader2.readLine();
				 
				 PreparedStatement pStmt = connection.prepareStatement("update EmployeeLogin set job_type = ? where employee_id = ?");
				 pStmt.setString(1, job);
				 pStmt.setString(2, EmployeeID);
				 pStmt.executeUpdate();
				 System.out.println(EmployeeID+" new job type has been changed to "+job);
			}
			
			public static void EmployeeSale() throws IOException, SQLException{
			 System.out.println("This is the table for Sales Value of each employee by ID");
	        ResultSet rs = statement.executeQuery("SELECT employee_ID ,sum FROM emploSales");
	        System.out.println("EmployeeID  SalesValue");
	        System.out.printf("%-15.15s  %-15.15s \n", "Employee ID", "Sale Sum");
	        System.out.printf("\n");
	        
	        while (rs.next()) {
	           String EmployeeID = rs.getString("employee_ID");
	           Double Sale_Sum = rs.getDouble("sum");
	           String Sale_string = Double.toString(Sale_Sum);
	           System.out.printf("%-15.15s  %-15.15s \n", EmployeeID, Sale_string);
	           System.out.printf("\n");
	        }
	       
		}
			 public static void inputSQL() throws IOException, SQLException{
		        	System.out.println("Please type in your SQL code here:");
					 BufferedReader reader = new BufferedReader (new InputStreamReader(System.in));
					 String ChangeEmployee = reader.readLine();
					 statement.execute(ChangeEmployee);
					 ResultSet rs = statement.getResultSet();
					if(rs != null)
					 {
					     ResultSetMetaData metaData = rs.getMetaData();
						 int columns = metaData.getColumnCount();	 
						 for(int i=1; i<=columns;i++) {
								System.out.printf( "%-25.25s  ", metaData.getColumnName(i));
							 }
						 System.out.printf("\n");
						 while(rs.next()) {
							 for(int i=1; i<=columns;i++) {
								System.out.printf( "%-25.25s  ", rs.getObject(i).toString());
							 }
							 System.out.printf("\n");
						 }
					}
			 }


	
	public static void SaleUI() throws IOException, SQLException{
		   while(true) {
		    Boolean goto_login = false;
		    System.out.println("Salesman Operations:");
		    System.out.println("1.View Customer");
		    System.out.println("2.Search in Customer");
		    System.out.println("3.Update Customer");
		    System.out.println("4.Create a Customer");
		    System.out.println("5.Create an Order");
		    System.out.println("6.View Sales Report");
		    System.out.println("7.View Revenue Report");
		    System.out.println("8.View Model Report");
		    System.out.println("9.SQl Operations");
		    System.out.println("10.Logout");
		    System.out.println("Please Select Your Operation(for exmaple: 1):");
		   
		    
		    BufferedReader reader = new BufferedReader (new InputStreamReader(System.in));
		    String selection = reader.readLine();
		    switch(selection){
		    case"1": viewCustomer(); break;
		    case"2": SearchinCustomer(); break;
		    case"3": updateCustomer(); break;
		    case"4": createCustomer(); break;
		    case"5": createOrder(); break;
		    case"6": AcessSaleReport(); break;
		    case"7": Revenue(); break;
			case"8": ModelReport(); break;
		    case"9": inputSQL(); break;
		    case"10": goto_login = true; break;
		    default: break;
		    }
		    if(goto_login) {
		    	statement.close();
		    	prestatement.close();
		    	connection.close();
		    	 break;
		    }
		    
		   }
	}
	
	public static void createCustomer()throws IOException, SQLException{
		System.out.println("Please input the CustomerID:");
		BufferedReader reader = new BufferedReader (new InputStreamReader(System.in));
	    String CustomerID = reader.readLine();
	    System.out.println("Please input the CustomerFirstName:");
	    BufferedReader reader1 = new BufferedReader (new InputStreamReader(System.in));
	    String CustomerFirstName = reader.readLine();
	    System.out.println("Please input the CustomerLastName:");
	    BufferedReader reader2 = new BufferedReader (new InputStreamReader(System.in));
	    String CustomerLastName = reader.readLine();
	    prestatement = connection.prepareStatement("insert into customer values(?,?,?)");
	    prestatement.setString(1, CustomerID);
	    prestatement.setString(2, CustomerFirstName);
	    prestatement.setString(3, CustomerLastName);
	    prestatement.executeUpdate();
	}
	
	public static void SearchinCustomer() throws IOException, SQLException{
		 while(true) {
		  Boolean goto_login = false;
		  System.out.println("SearchCustomer Options:");
		  System.out.println("1.Search by CustomerID");
		  System.out.println("2.Search by firstName");
		  System.out.println("3.Return to SaleUI");
		  System.out.println("Please select a option(for exmaple: 1):");
		  
		  BufferedReader reader = new BufferedReader (new InputStreamReader(System.in));
		  String selection = reader.readLine();
		  switch(selection){
		  case"1": SearchbyCustomerID(); break;
		  case"2": SearchbyCustomer_firstName();break;
		  case"3": goto_login = true; break;
		  default: break;
		  }
		  if(goto_login) {
		    	 break;
		    }	    
		 }
		}
	
	public static void SearchbyCustomerID() throws IOException, SQLException{
		System.out.println("Please input the CustomerID:");
		BufferedReader reader = new BufferedReader (new InputStreamReader(System.in));
	    String selection = reader.readLine();
	    prestatement = connection.prepareStatement("select * from customer where customer_ID = ?");
	    prestatement.setString(1, selection);
	    ResultSet rs = prestatement.executeQuery();
	    //ResultSet rs = statement.executeQuery("select * from EmployeeLogin where EmployeeID = "+selection1);
	    System.out.printf("%-15.15s  %-15.15s   %-15.15s\n", "customer_ID", "first_name", "last_name");
        System.out.printf("\n");
        while (rs.next()) {
           String CustomerID = rs.getString("customer_ID");
           String firstname = rs.getString("first_name");
           String lastname = rs.getString("last_name");
           System.out.printf("%-15.15s  %-15.15s   %-15.15s\n", CustomerID, firstname, lastname);
        }
         System.out.printf("\n");
		
	}
	
	public static void SearchbyCustomer_firstName() throws IOException, SQLException{
		System.out.println("Please input the firstName:");
		BufferedReader reader = new BufferedReader (new InputStreamReader(System.in));
	    String selection = reader.readLine();
	    prestatement = connection.prepareStatement("select * from customer where first_name = ?");
	    prestatement.setString(1, selection);
	    ResultSet rs = prestatement.executeQuery();
	   // ResultSet rs = statement.executeQuery("select * from EmployeeLogin where firstName = "+selection1);
	    System.out.printf("%-15.15s  %-15.15s   %-15.15s\n", "customer_ID", "first_name", "last_name");
        System.out.printf("\n");
        while (rs.next()) {
           String CustomerID = rs.getString("customer_ID");
           String firstname = rs.getString("first_name");
           String lastname = rs.getString("last_name");
           System.out.printf("%-15.15s  %-15.15s   %-15.15s\n", CustomerID, firstname, lastname);
        }
         System.out.printf("\n");
		
	}
	
    public static void viewCustomer() throws IOException, SQLException{
		   ResultSet rs = statement.executeQuery("select * from customer");
		   System.out.printf("%-15.15s  %-15.15s   %-15.15s\n", "customer_ID", "first_name", "last_name");
	        System.out.printf("\n");
	        while (rs.next()) {
	           String CustomerID = rs.getString("customer_ID");
	           String firstname = rs.getString("first_name");
	           String lastname = rs.getString("last_name");
	           System.out.printf("%-15.15s  %-15.15s   %-15.15s\n", CustomerID, firstname, lastname);
	        }
	         System.out.printf("\n");

		  }
    public static void updateCustomer() throws IOException, SQLException{
		   while(true) {
	    	   Boolean goto_login = false;
			   System.out.println("Please select update options:");
			   System.out.println("1.Update firstName");
			   System.out.println("2.Update lastName");
			   System.out.println("3.Return to SaleUI");
			   BufferedReader reader = new BufferedReader (new InputStreamReader(System.in));
				 String selection = reader.readLine();
				  switch(selection){
				  case"1": updateCustomerfirstName(); break;
				  case"2": updateCustomerlastName(); break;
				  case"3": goto_login = true; break;
				  default: break;
				  }
				  if(goto_login) {
				    	 break;
				    }
		   }
		  }
    public static void  updateCustomerfirstName()throws IOException, SQLException{
    	prestatement = connection.prepareStatement("update customer set first_name = ? where customer_ID = ?");
    	System.out.println("Please input the CustomerID:");
		BufferedReader reader = new BufferedReader (new InputStreamReader(System.in));
	    String selection = reader.readLine();

	    System.out.println("Please input the firstname you want to update:");
		BufferedReader reader1 = new BufferedReader (new InputStreamReader(System.in));
	    String selection2 = reader1.readLine();
	    prestatement.setString(1, selection2);
	    prestatement.setString(2, selection);
	    prestatement.executeUpdate();
	    prestatement = connection.prepareStatement("select * from customer where customer_ID = ?");
	    prestatement.setString(1, selection);
	    ResultSet rs = prestatement.executeQuery();
	    System.out.printf("%-15.15s  %-15.15s   %-15.15s\n", "customer_ID", "first_name", "last_name");
        System.out.printf("\n");
        while (rs.next()) {
           String CustomerID = rs.getString("customer_ID");
           String firstname = rs.getString("first_name");
           String lastname = rs.getString("last_name");
           System.out.printf("%-15.15s  %-15.15s   %-15.15s\n", CustomerID, firstname, lastname);
        }
         System.out.printf("\n");	    
    }
    public static void  updateCustomerlastName()throws IOException, SQLException{
    	prestatement = connection.prepareStatement("update customer set last_name = ? where customer_ID = ?");
    	System.out.println("Please input the CustomerID:");
		BufferedReader reader = new BufferedReader (new InputStreamReader(System.in));
	    String selection = reader.readLine();
	    System.out.println("Please input the lastname you want to update:");
		BufferedReader reader1 = new BufferedReader (new InputStreamReader(System.in));
	    String selection2 = reader1.readLine();
	    prestatement.setString(1, selection2);
	    prestatement.setString(2, selection);
	    prestatement.executeUpdate();
	    prestatement = connection.prepareStatement("select * from customer where customer_ID = ?");
	    prestatement.setString(1, selection);
	    ResultSet rs = prestatement.executeQuery();
	    System.out.printf("%-15.15s  %-15.15s   %-15.15s\n", "customer_ID", "first_name", "last_name");
        System.out.printf("\n");
        while (rs.next()) {
           String CustomerID = rs.getString("customer_ID");
           String firstname = rs.getString("first_name");
           String lastname = rs.getString("last_name");
           System.out.printf("%-15.15s  %-15.15s   %-15.15s\n", CustomerID, firstname, lastname);
        }
         System.out.printf("\n");
	    

	    
    }
		  public static void createOrder() throws IOException, SQLException{
			  
			     System.out.println("Please enter Order ID (Example o000000001 length is 10):");
				 BufferedReader reader3 = new BufferedReader (new InputStreamReader(System.in));
				 String OderID = reader3.readLine();
				 System.out.println("Please enter SalesMan Employee ID (Example e000000001 length is 10):");
				 BufferedReader reader4 = new BufferedReader (new InputStreamReader(System.in));
				 String EmployeeID = reader4.readLine();
				 System.out.println("Please enter Customer ID (Example c000000001 length is 10):");
				 BufferedReader reader6 = new BufferedReader (new InputStreamReader(System.in));
				 String CustomerID = reader6.readLine();
				 System.out.println("Please enter year (Example 2020):");
				 BufferedReader reader7 = new BufferedReader (new InputStreamReader(System.in));
				 String year = reader7.readLine();
				 int year_int = Integer.parseInt(year);
				 System.out.println("Please enter Model Number of this Order:");
				 BufferedReader reader9 = new BufferedReader (new InputStreamReader(System.in));
				 String ModelNumber = reader9.readLine();
				 int ModelNumber_int = Integer.parseInt(ModelNumber);
				 List<String> model_list = new ArrayList<String> ();
				 List<Integer> purchasing_amount_list = new ArrayList<Integer> ();
				for(int i =0; i<ModelNumber_int; i++)
				{	
					 System.out.println("Please enter Model ID (Example m000000001 length is 10):");
					 BufferedReader reader5 = new BufferedReader (new InputStreamReader(System.in));
					 String ModelID = reader5.readLine();
					 model_list.add(ModelID);
					 System.out.println("Please enter purchasing_amount:");
					 BufferedReader reader8 = new BufferedReader (new InputStreamReader(System.in));
					 String purchasing_amount = reader8.readLine();
					 int purchasing_amount_int = Integer.parseInt(purchasing_amount);
					 purchasing_amount_list.add(purchasing_amount_int);
				 }
				 List<String> ID_list = new ArrayList<String> ();
				 List<Integer> number_list = new ArrayList<Integer> ();
				 List<Double> cost_list = new ArrayList<Double> ();
				 double salevalue=0;
				 for(int i =0; i<ModelNumber_int; i++) {
			         prestatement = connection.prepareStatement("select * from Inventory where model_ID = ?");
			         prestatement.setString(1,model_list.get(i));
			         ResultSet rs = prestatement.executeQuery();
			         prestatement = connection.prepareStatement("select * from Model where model_ID = ?");
			         prestatement.setString(1,model_list.get(i));
			         ResultSet rs1 = prestatement.executeQuery();
			 	     boolean status = false;
			         while (rs.next()&&rs1.next()) {
			            String ID1   = rs.getString("ID");
			            int number1 = rs.getInt("number");
			            double cost1 = rs1.getDouble("sales_price");
			            if(number1 > purchasing_amount_list.get(i)||number1 == purchasing_amount_list.get(i))
			            {
			            	number_list.add(number1 -purchasing_amount_list.get(i));
			            	ID_list.add(ID1);
			            	cost_list.add(cost1);
			            	salevalue +=cost1*purchasing_amount_list.get(i);
			            	status = true;
			            	break;
			            }
			           
			            if(!status)
			            {
			            	System.out.println("There is no available inventary!");
			            	return;
			            }
			         }
				 }
		         prestatement = connection.prepareStatement("insert into Orders values(?,?,?,?,?)"); 
		         prestatement.setString(1, OderID);
		         prestatement.setDouble(2, salevalue);
		         prestatement.setString(3, EmployeeID);
		         prestatement.setString(4, CustomerID);
		         prestatement.setInt(5, year_int);
		         prestatement.executeUpdate();
		         for(int i=0;i<ModelNumber_int;i++) {
		        	 prestatement = connection.prepareStatement("insert into Choose values(?,?,?)"); 
		        	 prestatement.setString(2, OderID);
		        	 prestatement.setString(1, model_list.get(i));
		        	 prestatement.setInt(3, purchasing_amount_list.get(i));
		        	 prestatement.executeUpdate();
		         }
		         for(int i =0; i<ModelNumber_int; i++) {
		        	 prestatement = connection.prepareStatement("update Inventory set number = ? where ID = ?"); 
		        	 prestatement.setInt(1, number_list.get(i));
		        	 prestatement.setString(2, ID_list.get(i));
		        	 prestatement.executeUpdate();
		         }
		  }
		  public static void AcessSaleReport() throws IOException, SQLException{
		
		   ResultSet rs = statement.executeQuery("select order_ID, sale_value from Orders");
		   System.out.printf("%-15.15s  %-15.15s\n", "order_ID", "sale_value");
	        System.out.printf("\n");
		   while (rs.next()) {
			   String order_ID = rs.getString("order_ID");
			   Double sale_value = rs.getDouble("sale_value");
			   String sale_vale_string = Double.toString(sale_value);
			   System.out.printf("%-15.15s  %-15.15s\n", order_ID,sale_vale_string);
		   }
	         System.out.printf("\n");

		  }
		  
		  
	//todo	    
  public static void EngineerUI() throws IOException, SQLException{
		   while(true) {
		    Boolean goto_login = false;
		    System.out.println("Engineer Operations:");
		    System.out.println("1.View Model");
		    System.out.println("2.Search in Model");
		    System.out.println("3.Update Model");
		    System.out.println("4.View Inventory");
		    System.out.println("5.Search in Inventory");
		    System.out.println("6.Update Inventory");
		    System.out.println("7.View Employee Information");
		    System.out.println("8.View Order and Inventory Information");
		    System.out.println("9.SQl Operations");
		    System.out.println("10.Logout");
		    System.out.println("Please Select Your Operation(for exmaple: 1):");

		    
		    BufferedReader reader = new BufferedReader (new InputStreamReader(System.in));
		    String selection = reader.readLine();
		    switch(selection){
		    case"1": viewModel(); break;
		    case"2": searchModel(); break;
		    case"3": updateModel(); break;
		    case"4": viewInventory(); break;
		    case"5": searchInventory();break;
		    case"6": updateInventory();break;
		    case"7": viewEmployeelimited();break;
			case"8": OrderInventory(); break;
		    case"9": inputSQL();break;
		    case"10": goto_login = true; break;
		    default: break;

		    }
		    if(goto_login) {
		    	statement.close();
		    	prestatement.close();
		    	connection.close();
		    	 break;
		    }
		    
		   }
		   
		  }
		  public static void viewModel() throws IOException, SQLException{
			   ResultSet rs = statement.executeQuery("select * from model");
			   System.out.printf("%-15.15s  %-15.15s  %-15.15s\n", "model_ID", "model_name","sales_price" );
		        System.out.printf("\n");
		        while (rs.next()) {
		           String model_ID = rs.getString("model_ID");
		           String model_name = rs.getString("model_name");
		           double sales_price = rs.getDouble("sales_price");
		           String sales_price_string = Double.toString(sales_price);
		           System.out.printf("%-15.15s  %-15.15s  %-15.15s\n", model_ID, model_name,sales_price_string);
		        }
		         System.out.printf("\n");

		  }
		  
		  public static void searchModel() throws IOException, SQLException{
			  while(true) {
			  Boolean goto_login = false;
			  System.out.println("SearchModel Options:");
			  System.out.println("1.Search by ModelID");
			  System.out.println("2.Search by ModelName");
			  System.out.println("3.Return to EngineerUI");
			  System.out.println("Please select a option(for exmaple: 1):");
			  
			  BufferedReader reader = new BufferedReader (new InputStreamReader(System.in));
			   String selection = reader.readLine();
			   switch(selection) {
			      case"1": SearchbyModelID(); break;
				  case"2": SearchbyModel_Name();break;
				  case"3": goto_login = true; break;
				  default: break;
			   }
			   if(goto_login) {
				   break;
			   }
			  }
		        
		  }
		  
		  public static void SearchbyModelID() throws IOException, SQLException{
				System.out.println("Please input the ModelID:");
				BufferedReader reader = new BufferedReader (new InputStreamReader(System.in));
			    String selection = reader.readLine();
			    prestatement = connection.prepareStatement("select * from Model where model_ID = ?");
			    prestatement.setString(1, selection);
			    ResultSet rs = prestatement.executeQuery();
			    System.out.printf("%-15.15s  %-15.15s  %-15.15s\n", "model_ID", "model_name","sales_price" );
		        System.out.printf("\n");
		        while (rs.next()) {
		           String model_ID = rs.getString("model_ID");
		           String model_name = rs.getString("model_name");
		           double sales_price = rs.getDouble("sales_price");
		           String sales_price_string = Double.toString(sales_price);
		           System.out.printf("%-15.15s  %-15.15s  %-15.15s\n", model_ID, model_name,sales_price_string);
		        }
		         System.out.printf("\n");
				
			}

		  public static void SearchbyModel_Name() throws IOException, SQLException{
				System.out.println("Please input the Model Name:");
				BufferedReader reader = new BufferedReader (new InputStreamReader(System.in));
			    String selection = reader.readLine();
			    prestatement = connection.prepareStatement("select * from model where model_name = ?");
			    prestatement.setString(1, selection);
			    ResultSet rs = prestatement.executeQuery();
			    System.out.printf("%-15.15s  %-15.15s  %-15.15s\n", "model_ID", "model_name","sales_price" );
		        System.out.printf("\n");
		        while (rs.next()) {
		           String model_ID = rs.getString("model_ID");
		           String model_name = rs.getString("model_name");
		           double sales_price = rs.getDouble("sales_price");
		           String sales_price_string = Double.toString(sales_price);
		           System.out.printf("%-15.15s  %-15.15s  %-15.15s\n", model_ID, model_name,sales_price_string);
		        }
		         System.out.printf("\n");
			}

		  public static void searchInventory() throws IOException, SQLException{
			  while(true) {
				  Boolean goto_login = false;
				  System.out.println("SearchInventory Options:");
				  System.out.println("1.Search by InventoryID");
				  System.out.println("2.Search by ModelName");
				  System.out.println("3.Return to EngineerUI");
				  System.out.println("Please select a option(for exmaple: 1):");
				  
				  BufferedReader reader = new BufferedReader (new InputStreamReader(System.in));
				   String selection = reader.readLine();
				   switch(selection) {
				      case"1": SearchbyInventoryID(); break;
					  case"2": SearchbyModel_ID();break;
					  case"3": goto_login = true; break;
					  default: break;
				   }
				   if(goto_login) {
					   break;
				   }
				  }
			        
			  }
		  public static void SearchbyInventoryID() throws IOException, SQLException{
				System.out.println("Please input the InventoryID:");
				BufferedReader reader = new BufferedReader (new InputStreamReader(System.in));
			    String selection = reader.readLine();
			    prestatement = connection.prepareStatement("select * from Inventory where ID = ?");
			    prestatement.setString(1, selection);
			    ResultSet rs = prestatement.executeQuery();
			    System.out.printf("%-15.15s  %-15.15s  %-15.15s  %-15.15s  %-15.15s\n", "Batch_ID", "model_ID","lead_time","cost","number" );
		        System.out.printf("\n");
		        while (rs.next()) {
		           String ID = rs.getString("ID");
		           double cost = rs.getDouble("cost");
		           String cost_string = Double.toString(cost);
		           int lead_time = rs.getInt("lead_time");
		           String lead_time_string = Integer.toString(lead_time);
		           String model_ID = rs.getString("model_ID");
		           int number = rs.getInt("number");
		           String number_string = Integer.toString(number);
		           System.out.printf("%-15.15s  %-15.15s  %-15.15s  %-15.15s  %-15.15s\n", ID,model_ID, lead_time_string,cost_string, number_string); 
		        }
		         System.out.printf("\n");
			}

		  public static void SearchbyModel_ID() throws IOException, SQLException{
				System.out.println("Please input the Model ID:");
				BufferedReader reader = new BufferedReader (new InputStreamReader(System.in));
			    String selection = reader.readLine();
			    prestatement = connection.prepareStatement("select * from inventory where model_ID = ?");
			    prestatement.setString(1, selection);
			    ResultSet rs = prestatement.executeQuery();
			    System.out.printf("%-15.15s  %-15.15s  %-15.15s  %-15.15s  %-15.15s\n", "Batch_ID", "model_ID","lead_time","cost","number" );
		        System.out.printf("\n");
		        while (rs.next()) {
		           String ID = rs.getString("ID");
		           double cost = rs.getDouble("cost");
		           String cost_string = Double.toString(cost);
		           int lead_time = rs.getInt("lead_time");
		           String lead_time_string = Integer.toString(lead_time);
		           String model_ID = rs.getString("model_ID");
		           int number = rs.getInt("number");
		           String number_string = Integer.toString(number);
		           System.out.printf("%-15.15s  %-15.15s  %-15.15s  %-15.15s  %-15.15s\n", ID,model_ID, lead_time_string,cost_string, number_string); 
		        }
		         System.out.printf("\n");
				
			}
		  public static void updateModel() throws IOException, SQLException{
			  while(true) {
				  Boolean goto_login = false;
				  System.out.println("Please select update options:");
				  System.out.println("1.Update salePrice by ModelID");
				  System.out.println("2.Return to EngineerUI");
				  System.out.println("Please select a option(for exmaple: 1):");
				  
				  BufferedReader reader = new BufferedReader (new InputStreamReader(System.in));
				   String selection = reader.readLine();
				   switch(selection) {
				      case"1": UpdateSalePriceByModelID(); break;
					  case"2": goto_login = true; break;
					  default: break;
				   }
				   if(goto_login) {
					   break;
				   }
				  }
			        
			  }
		  public static void  UpdateSalePriceByModelID()throws IOException, SQLException{
		    	prestatement = connection.prepareStatement("update Model set sales_price = ? where model_ID = ?");
		    	System.out.println("Please input the ModelID:");
				BufferedReader reader = new BufferedReader (new InputStreamReader(System.in));
			    String selection = reader.readLine();
			    System.out.println("Please input the sales_price you want to update:");
				BufferedReader reader1 = new BufferedReader (new InputStreamReader(System.in));
			    String selection2 = reader1.readLine();
			    double selection1 = Double.parseDouble(selection2); 
			    prestatement.setDouble(1, selection1);
			    prestatement.setString(2, selection);
			    prestatement.executeUpdate();
			    prestatement = connection.prepareStatement("select * from Model where model_ID = ?");
			    prestatement.setString(1, selection);
			    ResultSet rs = prestatement.executeQuery();
			    System.out.printf("%-15.15s  %-15.15s  %-15.15s\n", "model_ID", "model_name","sales_price" );
		        System.out.printf("\n");
		        while (rs.next()) {
		           String model_ID = rs.getString("model_ID");
		           String model_name = rs.getString("model_name");
		           double sales_price = rs.getDouble("sales_price");
		           String sales_price_string = Double.toString(sales_price);
		           System.out.printf("%-15.15s  %-15.15s  %-15.15s\n", model_ID, model_name,sales_price_string);
		        }
		         System.out.printf("\n");
			    
		    }

		  
		  
		  
		  public static void updateInventory() throws IOException, SQLException{
			  while(true) {
				  Boolean goto_login = false;
				  System.out.println("SearchInventory Options:");
				  System.out.println("1.Update Number by ID");
				  System.out.println("2.Update Cost by ID");
				  System.out.println("3.Return to EngineerUI");
				  System.out.println("Please select a option(for exmaple: 1):");
				  
				  BufferedReader reader = new BufferedReader (new InputStreamReader(System.in));
				   String a = reader.readLine();
				   switch(a) {
				      case"1": UpdateNumber(); break;
					  case"2": UpdateCost();break;
					  case"3": goto_login = true; break;
					  default: break;
				   }
				   if(goto_login) {
					   break;
				   }
				  }
			        
			  }
		  
		  public static void UpdateNumber() throws IOException, SQLException{
			    prestatement = connection.prepareStatement("update Inventory set number = ? where ID = ?");
		    	System.out.println("Please input the  Inventory ID:");
				BufferedReader reader = new BufferedReader (new InputStreamReader(System.in));
			    String selection = reader.readLine();
			    System.out.println("Please input the number you want to update:");
				BufferedReader reader1 = new BufferedReader (new InputStreamReader(System.in));
			    String selection2 = reader1.readLine();
			    int selection1 = Integer.parseInt(selection2); 
			    prestatement.setInt(1, selection1);
			    prestatement.setString(2, selection);
			    prestatement.executeUpdate();
			    prestatement = connection.prepareStatement("select * from Inventory where ID = ?");
			    prestatement.setString(1, selection);
			    ResultSet rs = prestatement.executeQuery();
			    System.out.printf("%-15.15s  %-15.15s  %-15.15s  %-15.15s  %-15.15s\n", "Batch_ID", "model_ID","lead_time","cost","number" );
		        System.out.printf("\n");
		        while (rs.next()) {
		           String ID = rs.getString("ID");
		           double cost = rs.getDouble("cost");
		           String cost_string = Double.toString(cost);
		           int lead_time = rs.getInt("lead_time");
		           String lead_time_string = Integer.toString(lead_time);
		           String model_ID = rs.getString("model_ID");
		           int number = rs.getInt("number");
		           String number_string = Integer.toString(number);
		           System.out.printf("%-15.15s  %-15.15s  %-15.15s  %-15.15s  %-15.15s\n", ID,model_ID, lead_time_string,cost_string, number_string); 
		        }
		         System.out.printf("\n");
			    
		  }
		  
		  public static void UpdateCost() throws IOException, SQLException{
			  prestatement = connection.prepareStatement("update Inventory set cost = ? where ID = ?");
		    	System.out.println("Please input the  Inventory ID:");
				BufferedReader reader = new BufferedReader (new InputStreamReader(System.in));
			    String selection = reader.readLine();
			    System.out.println("Please input the cost you want to update:");
				BufferedReader reader1 = new BufferedReader (new InputStreamReader(System.in));
			    String selection2 = reader1.readLine();
			    double selection1 = Double.parseDouble(selection2); 
			    prestatement.setDouble(1, selection1);
			    prestatement.setString(2, selection);
			    prestatement.executeUpdate();
			    prestatement = connection.prepareStatement("select * from Inventory where ID = ?");
			    prestatement.setString(1, selection);
			    ResultSet rs = prestatement.executeQuery();
			    System.out.printf("%-15.15s  %-15.15s  %-15.15s  %-15.15s  %-15.15s\n", "Batch_ID", "model_ID","lead_time","cost","number" );
		        System.out.printf("\n");
		        while (rs.next()) {
		           String ID = rs.getString("ID");
		           double cost = rs.getDouble("cost");
		           String cost_string = Double.toString(cost);
		           int lead_time = rs.getInt("lead_time");
		           String lead_time_string = Integer.toString(lead_time);
		           String model_ID = rs.getString("model_ID");
		           int number = rs.getInt("number");
		           String number_string = Integer.toString(number);
		           System.out.printf("%-15.15s  %-15.15s  %-15.15s  %-15.15s  %-15.15s\n", ID,model_ID, lead_time_string,cost_string, number_string); 
		        }
		         System.out.printf("\n");
		  }
		  
		  public static void viewInventory() throws IOException, SQLException{
			   ResultSet rs = statement.executeQuery("select * from inventory");
			   System.out.printf("%-15.15s  %-15.15s  %-15.15s  %-15.15s  %-15.15s\n", "Batch_ID", "model_ID","lead_time","cost","number" );
		        System.out.printf("\n");
		        while (rs.next()) {
		           String ID = rs.getString("ID");
		           double cost = rs.getDouble("cost");
		           String cost_string = Double.toString(cost);
		           int lead_time = rs.getInt("lead_time");
		           String lead_time_string = Integer.toString(lead_time);
		           String model_ID = rs.getString("model_ID");
		           int number = rs.getInt("number");
		           String number_string = Integer.toString(number);
		           System.out.printf("%-15.15s  %-15.15s  %-15.15s  %-15.15s  %-15.15s\n", ID,model_ID, lead_time_string,cost_string, number_string); 
		        }
		         System.out.printf("\n");

		  }
			
		  
		
		  public static void viewEmployeelimited() throws IOException, SQLException{
			   
			   ResultSet rs = statement.executeQuery("select * from enginView");
			   System.out.printf("%-15.15s  %-15.15s  %-15.15s\n","first_name", "last_name", "job_type");
			   System.out.printf("\n");
		        while (rs.next()) {
		           String first_name = rs.getString("first_name");
		           String last_name = rs.getString("last_name");
		           String job_type = rs.getString("job_type");
		           System.out.printf("%-15.15s  %-15.15s  %-15.15s\n",first_name, last_name, job_type);
		        }
		         System.out.printf("\n");

			  }
		
}
