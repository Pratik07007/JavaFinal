package tests;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import app.JDBC;
import app.PasswordHash;
import app.ReturnMessage;


public class DatabaseTest {
	

	
	@BeforeEach
	public void establishConnection() {
		assertNotNull(JDBC.getConnection(),"There must be a valid database connection: please check the database credentials");
	}
	
	
	@Test
	public void checkUniqEmailConstraint() {
		
        String name = "Pratik Dhimal";
        String email = "ram@gmail.com"; //this user already exists so this must fail
        String password = "Abcedeasl;f"; 
        String level = "BEGINNER";
        
        String hash =  PasswordHash.hashPassword(password);

        ReturnMessage response = JDBC.registerUser(name, email, hash, level);
        
        assertTrue(response.success);
 
    }
	
	
    @Test
    public void testAddQuestion() {
        String question = "Knok Knok!";
        String level = "BEGINNER";
        String answer = "Okay";
        String opt1 = "No";
        String opt2 = "Okay";
        String opt3 = "OH NO";
        String opt4 = "Please come in";
        
        ReturnMessage result = JDBC.addQuestion(question, level, answer, opt1, opt2, opt3, opt4);
        
        assertTrue(result.success, "Question addition failed");
    }
    
    @Test
    public void testAddAdmin() {

    	String email = "pratikdhimal@gmail.com";

    	String password = "admiasfaaasfn123";

    	String name = "AdminUser";

    	ReturnMessage result = JDBC.addAdmin(email, password, name);

    	assertTrue(result.success, "Admin creation failed");

    	}
	
    
    @AfterAll
    public static void clenUp() {
        try (Connection conn = JDBC.getConnection()) {
            if (conn == null) {
                System.err.println("Failed to connect to database during cleanup");
                return;
            }

            // Clean up test question
            try (PreparedStatement questionStmt = conn.prepareStatement("DELETE FROM questions WHERE question = ? AND answer = ?")) {
                questionStmt.setString(1, "Knok Knok!");
                questionStmt.setString(2, "Okay");
                questionStmt.executeUpdate();
            }

            // Clean up test admin
            try (PreparedStatement adminStmt = conn.prepareStatement("DELETE FROM users WHERE email = ? AND role = 'ADMIN'")) {
                adminStmt.setString(1, "pratikdhimal@gmail.com");
                adminStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
          
       
    }


