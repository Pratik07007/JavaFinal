package app;
import java.sql.*;

public class JDBC {
	
	
	
	public static ReturnMessage chekLogin(String email,String password) {
        String url = "jdbc:mysql://localhost:3306/quizApp";
        String user = "root";
        String pass = "admin@12345";
        
        String query = "SELECT * FROM users WHERE email = ?";
            
         try (Connection connection = DriverManager.getConnection(url, user, pass);
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
           
                preparedStatement.setString(1, email);
                
        
                ResultSet response = preparedStatement.executeQuery();
               
                if (response.next()) {
                	int id = response.getInt(1);
                    String name = response.getString(2);
                    String dbPassword = response.getString(4);
                    String role = response.getString(10);
                    
                    Boolean verified =  PasswordHash.verifyPassword(password,dbPassword);
                    
//                    System.out.println(dbPassword);
//                    System.out.println(password);
                    
                    
                    if(verified) {
                    	return new ReturnMessage(true,"Loggin Succesfull", new Users(id,name,email,role));
                    	
                    }else 
                    	
                    {                
                    	return new ReturnMessage(false,"Incorrect password for email: "+email, null);
                    }
                }else {	
                	return new ReturnMessage(false,"No User found with the provided credentials",null);
                }
                
                
            } catch (SQLException exception) {
                System.out.println(exception);
                return new ReturnMessage(false,"Server side exception occured",null);
                
            }
        	
        }
	
	
	public static ReturnMessage registerUser(String name, String email, String password) {
        String url = "jdbc:mysql://localhost:3306/quizApp";
        String user = "root";
        String pass = "admin@12345";
      
        ReturnMessage response = validEmail(email);
        if(response.success) {
        	String query = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
            
            try (Connection connection = DriverManager.getConnection(url, user, pass);
                 PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                
                // Set the parameters for the insert query
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, email);
                preparedStatement.setString(3, password);
                
                // Execute the query and get the generated keys (id)
                int affectedRows = preparedStatement.executeUpdate();
                
                if (affectedRows > 0) {
                	return  new ReturnMessage(true,"User: "+name.toUpperCase()+" created succesfully ",null);
                }
                return new ReturnMessage(false,"User creation failed",null);
                
            } catch (SQLException exception) {
                System.out.println(exception);
                return new ReturnMessage(false,"Server Side exception occured",null);
            }
        }else {
        	return new ReturnMessage(false,"User with this email already exist",null);
        	
        }
    }

	
	public static ReturnMessage validEmail(String email) {
		
		String url = "jdbc:mysql://localhost:3306/quizApp";
	    String user = "root";
	    String pass = "admin@12345";
	        
        String query = "SELECT name, password FROM users WHERE email = ?";

        try (Connection connection = DriverManager.getConnection(url, user, pass);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                return new ReturnMessage(false, "User with the email"+email+ "already exists", null);
            } else {
            	
                return new ReturnMessage(true, "Success", null);
            }
            
        } catch (SQLException exception) {
            System.out.println(exception);
            return new ReturnMessage(false, "Server-side exception occurred", null);
        }
    }


	public static ReturnMessage addAdmin(String email,String password,String name) {
	
	String url = "jdbc:mysql://localhost:3306/quizApp";
    String user = "root";
    String pass = "admin@12345";
        
    String query = "INSERT INTO users (name, email, password,role) VALUES (?, ?, ?,?)";

    try (Connection connection = DriverManager.getConnection(url, user, pass);
         PreparedStatement preparedStatement = connection.prepareStatement(query)) {
    	String hashedPassword = PasswordHash.hashPassword(password);	
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, email);
        preparedStatement.setString(3, hashedPassword);
        preparedStatement.setString(4, "ADMIN");
                
        int affectedRows = preparedStatement.executeUpdate();
        
        if (affectedRows > 0) {
        	return  new ReturnMessage(true,"Admin: "+email+" created succesfully ",null);
        }
        return new ReturnMessage(false,"Admin creation failed",null);
        
    } catch (SQLException exception) {
        System.out.println(exception);
        return new ReturnMessage(false, "Server-side exception occurred", null);
    }
}


	public static ReturnMessage addQuestion(String question,String level,String answer, String opt1,String opt2,String opt3,String opt4) {
        String url = "jdbc:mysql://localhost:3306/quizApp";
        String user = "root";
        String pass = "admin@12345";
        
        String query = "INSERT INTO questions (question, level, answer, option1, option2, option3, option4) VALUES (?, ?, ?, ?, ?, ?, ?)";

         try (Connection connection = DriverManager.getConnection(url, user, pass);
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        	 
           
                preparedStatement.setString(1, question);
                preparedStatement.setString(2, level);
                preparedStatement.setString(3, answer);
                preparedStatement.setString(4, opt1);
                preparedStatement.setString(5, opt2);
                preparedStatement.setString(6, opt3);
                preparedStatement.setString(7, opt4);
                
        
                int affectedRows = preparedStatement.executeUpdate();
                if(affectedRows>0) {
                	return new ReturnMessage(true, "Question Addition Successed", null);
                	
                }else {
                	return new ReturnMessage(false, "Question Addition Failed", null);
                }
                
            } catch (SQLException exception) {
                System.out.println(exception);
                return new ReturnMessage(false,"Server side exception occured",null);
                
            }
        	
        }

}



	




