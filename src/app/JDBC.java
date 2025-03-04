package app;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JDBC {
	private static final String URL = "jdbc:mysql://localhost:3306/quizApp"; 
    private static final String USER = "root";
    private static final String PASSWORD = "admin@12345";


    /**
     * Establishes a connection to the database.
     * @return Connection object if successful, otherwise null.
     */
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
            return null;
        }
    }
	
	
	
    public static ReturnMessage checkLogin(String email, String password) {
        String query = "SELECT * FROM users WHERE email = ?";
                
        try (Connection connection = getConnection()) {
            if (connection == null) {
                return new ReturnMessage(false, "Unable to connect to database", null);
            }
            
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, email);
                ResultSet response = preparedStatement.executeQuery();
                   
                if (response.next()) {
                    int id = response.getInt(1);
                    String name = response.getString(2);
                    String dbPassword = response.getString(4);
                    String role = response.getString(10);
                    String level = response.getString(11);
                    
                    int[] scores = {response.getInt(5), response.getInt(6), response.getInt(7), 
                                  response.getInt(8), response.getInt(9)};
                    
                    Boolean verified = PasswordHash.verifyPassword(password, dbPassword);
                    
                    if (verified) {
                        return new ReturnMessage(true, "Login Successful", 
                               new Compitetor(id, name, email, role, level, scores));
                    } else {
                        return new ReturnMessage(false, "Incorrect password for email: " + email, null);
                    }
                } else {    
                    return new ReturnMessage(false, "No User found with the provided credentials", null);
                }
            }
        } catch (SQLException exception) {
            System.out.println(exception);
            return new ReturnMessage(false, "Server side exception occurred", null);
        }
    }
	
	
    public static ReturnMessage registerUser(String name, String email, String password, String level) {
        ReturnMessage response = validEmail(email);
        if(response.success) {
            String query = "INSERT INTO users (name, email, password,level) VALUES (?, ?, ?,?)";
            
            try (Connection connection =getConnection()) {
                if (connection == null) {
                    return new ReturnMessage(false, "Unable to connect to database", null);
                }
                
                try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                    preparedStatement.setString(1, name);
                    preparedStatement.setString(2, email);
                    preparedStatement.setString(3, password);
                    preparedStatement.setString(4, level);
                    
                    int affectedRows = preparedStatement.executeUpdate();
                    
                    if (affectedRows > 0) {
                        return new ReturnMessage(true, "User: " + name.toUpperCase() + " created successfully", null);
                    }
                    return new ReturnMessage(false, "User creation failed", null);
                }
            } catch (SQLException exception) {
                System.out.println(exception);
                return new ReturnMessage(false, "Server Side exception occurred", null);
            }
        }
        return new ReturnMessage(false, "User with this email already exists", null);
    }
	
    public static ReturnMessage validEmail(String email) {
        String query = "SELECT id FROM users WHERE email = ?";

        try (Connection connection = getConnection()) {
            if (connection == null) {
                return new ReturnMessage(false, "Unable to connect to database", null);
            }
            
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, email);
                ResultSet resultSet = preparedStatement.executeQuery();
                
                if (resultSet.next()) {
                    return new ReturnMessage(false, "User with the email " + email + " already exists", null);
                } else {
                    return new ReturnMessage(true, "Success", null);
                }
            }
        } catch (SQLException exception) {
            System.out.println(exception);
            return new ReturnMessage(false, "Server-side exception occurred", null);
        }
    }

    public static ReturnMessage addAdmin(String email, String password, String name) {
        String query = "INSERT INTO users (name, email, password,role) VALUES (?, ?, ?,?)";
        
        ReturnMessage valid = validEmail(email);
        if(!valid.success) {
            return new ReturnMessage(false, "User or admin with this detail exists", null);
        }

        try (Connection connection = getConnection()) {
            if (connection == null) {
                return new ReturnMessage(false, "Unable to connect to database", null);
            }
            
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                String hashedPassword = PasswordHash.hashPassword(password);    
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, email);
                preparedStatement.setString(3, hashedPassword);
                preparedStatement.setString(4, "ADMIN");
                        
                int affectedRows = preparedStatement.executeUpdate();
                
                if (affectedRows > 0) {
                    return new ReturnMessage(true, "Admin: " + email + " created successfully", null);
                }
                return new ReturnMessage(false, "Admin creation failed", null);
            }
        } catch (SQLException exception) {
            System.out.println(exception);
            return new ReturnMessage(false, "Server-side exception occurred", null);
        }
    }


    public static ReturnMessage addQuestion(String question, String level, String answer, 
            String opt1, String opt2, String opt3, String opt4) {
        
        String query = "INSERT INTO questions (question, level, answer, option1, option2, option3, option4) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection()) {
            if (connection == null) {
                return new ReturnMessage(false, "Unable to connect to database", null);
            }
            
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, question);
                preparedStatement.setString(2, level);
                preparedStatement.setString(3, answer);
                preparedStatement.setString(4, opt1);
                preparedStatement.setString(5, opt2);
                preparedStatement.setString(6, opt3);
                preparedStatement.setString(7, opt4);
                
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    return new ReturnMessage(true, "Question Addition Successful", null);
                } else {
                    return new ReturnMessage(false, "Question Addition Failed", null);
                }
            }
        } catch (SQLException exception) {
            System.out.println(exception);
            return new ReturnMessage(false, "Server side exception occurred", null);
        }
    }

    public static ArrayList<Questions> fetchAllQuestions() {
        ArrayList<Questions> questions = new ArrayList<>();
        String query = "SELECT * FROM questions ORDER BY id DESC";

        try (Connection conn = getConnection()) {
            if (conn == null) {
                return null;
            }
            
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String text = rs.getString("question");
                    String opt1 = rs.getString("option1");
                    String opt2 = rs.getString("option2");
                    String opt3 = rs.getString("option3");
                    String opt4 = rs.getString("option4");
                    String level = rs.getString("level");
                    String answer = rs.getString("answer");

                    questions.add(new Questions(id, text, opt1, opt2, opt3, opt4, level, answer));
                }
                return questions;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

	
    public static boolean deleteQuestion(int questionId) {
        String query = "DELETE FROM questions WHERE id = ?";  

        try (Connection conn = getConnection()) {
            if (conn == null) {
                return false;
            }
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, questionId);  
                return stmt.executeUpdate() > 0;  
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateQuestion(int questionId, String questionText, String level, 
            String option1, String option2, String option3, String option4, String answer) {
        String query = "UPDATE questions SET question = ?, level = ?, option1 = ?, option2 = ?, " +
                      "option3 = ?, option4 = ?, answer = ? WHERE id = ?";  

        try (Connection conn = getConnection()) {
            if (conn == null) {
                return false;
            }
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, questionText);
                stmt.setString(2, level.toUpperCase());  // Ensure level is capitalized
                stmt.setString(3, option1.isEmpty() ? null : option1);  // If empty, set to null
                stmt.setString(4, option2.isEmpty() ? null : option2);  // If empty, set to null
                stmt.setString(5, option3.isEmpty() ? null : option3);  // If empty, set to null
                stmt.setString(6, option4.isEmpty() ? null : option4);  // If empty, set to null
                stmt.setString(7, answer.isEmpty() ? null : answer);    // If empty, set to null
                stmt.setInt(8, questionId);

                return stmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Questions> fetchRandomQuestionsByDifficulty(String level) {
        List<Questions> questions = new ArrayList<>();  
        String query = "SELECT * FROM questions WHERE level = ? ORDER BY RAND() LIMIT 5";  

        try (Connection conn = getConnection()) {
            if (conn == null) {
                return Collections.emptyList();
            }
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, level);  
                
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        questions.add(new Questions(
                            rs.getInt("id"),
                            rs.getString("question"),
                            rs.getString("option1"),
                            rs.getString("option2"),
                            rs.getString("option3"),
                            rs.getString("option4"),
                            rs.getString("level"),
                            rs.getString("answer")
                        ));
                    }
                }
                return questions;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
	
	 
    public static boolean updateScore(int userId, String columnName, int score) {
        String query = "UPDATE users SET " + columnName + " = ? WHERE id = ?";  

        try (Connection conn = getConnection()) {
            if (conn == null) {
                return false;
            }
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, score);
                stmt.setInt(2, userId);

                int rowsAffected = stmt.executeUpdate();  

                if (rowsAffected > 0) {
                    System.out.println("Score updated successfully for user ID: " + userId);
                    return true;
                } else {
                    System.out.println("No rows affected. User ID might not exist.");
                    return false;
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            return false;
        }
    }

    public static ReturnMessage getUserByEmail(String email) {
        String query = "SELECT * FROM users WHERE email = ? AND role = 'USER'";

        try (Connection connection = getConnection()) {
            if (connection == null) {
                return new ReturnMessage(false, "Unable to connect to database", null);
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, email);
                ResultSet rs = preparedStatement.executeQuery();
                
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String email_db = rs.getString("email");
                    int[] scores = {
                        rs.getInt("score1"),
                        rs.getInt("score2"),
                        rs.getInt("score3"),
                        rs.getInt("score4"),
                        rs.getInt("score5")
                    };
                    String role = rs.getString("role").toUpperCase();
                    String level = rs.getString("level");
                    
                    Compitetor user = new Compitetor(id, name, email_db, role, level, scores);
                    return new ReturnMessage(true, "User Found!", user);
                } else {
                    return new ReturnMessage(false, "No user found with this email", null);
                }
            }
        } catch (SQLException exception) {
            System.out.println(exception);
            return new ReturnMessage(false, "Server-side exception occurred", null);
        }
    }

    public static ReturnMessage getAllUsers() {
        String query = "SELECT * FROM users WHERE role = 'USER'";

        try (Connection connection = getConnection()) {
            if (connection == null) {
                return new ReturnMessage(false, "Unable to connect to database", null);
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                ResultSet rs = preparedStatement.executeQuery();
                
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String email_db = rs.getString("email");
                    int[] scores = {
                        rs.getInt("score1"),
                        rs.getInt("score2"),
                        rs.getInt("score3"),
                        rs.getInt("score4"),
                        rs.getInt("score5")
                    };
                    String role = rs.getString("role").toUpperCase();
                    String level = rs.getString("level");
                    
                    Compitetor user = new Compitetor(id, name, email_db, role, level, scores);
                    return new ReturnMessage(true, "User Found!", user);
                } else {
                    return new ReturnMessage(false, "No users found", null);
                }
            }
        } catch (SQLException exception) {
            System.out.println(exception);
            return new ReturnMessage(false, "Server-side exception occurred", null);
        }
    }	public static void updateOverallScore(int userId, double overallScore) {
	    String query = "UPDATE users SET overallScore = ? WHERE id = ?";
	    
	    try (Connection conn = getConnection()) {
	        if (conn == null) {
	            return;
	        }
	        try (PreparedStatement stmt = conn.prepareStatement(query)) {
	            stmt.setDouble(1, overallScore);
	            stmt.setInt(2, userId);
	            stmt.executeUpdate();
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	public static int[] fetchUserScores(int userId) {
	    String query = "SELECT score1, score2, score3, score4 FROM users WHERE id = ?";
	    int[] scores = new int[4];

	    try (Connection conn = getConnection()) {
	        if (conn == null) {
	            return scores;
	        }
	        try (PreparedStatement stmt = conn.prepareStatement(query)) {
	            stmt.setInt(1, userId);
	            ResultSet rs = stmt.executeQuery();

	            if (rs.next()) {
	                for (int i = 0; i < 4; i++) {
	                    scores[i] = rs.getInt(i + 1);
	                }
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return scores;
	}}




    




	




