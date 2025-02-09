package app;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.List;

/**
 * This class represents the Leaderboard UI for displaying high scores in various difficulty levels.
 * It fetches the leaderboard data from the database and displays it in a table format for each difficulty level.
 */
public class LeaderBoardUI {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/quizApp"; // Database connection URL
    private static final String DB_USER = "root"; // Database username
    private static final String DB_PASSWORD = "admin@12345"; // Database password

    /**
     * Constructor to create and display the Leaderboard UI.
     *
     * @param user The user who is accessing the leaderboard.
     */
    public LeaderBoardUI(Compitetor user) {
        JFrame frame = new JFrame("High Scores"); // Create the JFrame for the high scores page
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Set close operation
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize the window
        frame.setLayout(new BorderLayout()); // Set layout of the frame

        // Header Panel with title and back button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 73, 94)); // Set header background color

        JLabel label = new JLabel("High Scores Page", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 30)); // Set label font
        label.setForeground(Color.WHITE); // Set label color

        // Load and scale the back button image
        ImageIcon backIcon = new ImageIcon(new ImageIcon("/Users/pratikdhimal/Desktop/Remove Background Preview.png")
                .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        JButton backButton = new JButton(backIcon);
        backButton.setBorderPainted(false); // Remove border around button
        backButton.setContentAreaFilled(false); // Set content area transparent
        backButton.setFocusPainted(false); // Remove focus painting on button
        backButton.addActionListener(e -> { // Action listener for back button
            frame.dispose(); // Close the leaderboard window
            new UserPannelUI(user); // Open user panel when back button is clicked
        });

        headerPanel.add(backButton, BorderLayout.WEST); // Add back button to left
        headerPanel.add(label, BorderLayout.CENTER); // Add title in the center
        frame.add(headerPanel, BorderLayout.NORTH); // Add header panel to the top

        // Main Content Panel to hold difficulty level panels
        JPanel mainPanel = new JPanel(new GridLayout(1, 3, 10, 0)); // Set grid layout with 3 columns
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding around main panel

        // Create and add difficulty level panels (Beginner, Intermediate, Advance)
        mainPanel.add(createDifficultyPanel("Beginner", fetchLeaderboardData("BEGINNER")));
        mainPanel.add(createDifficultyPanel("Intermediate", fetchLeaderboardData("INTERMEDIATE")));
        mainPanel.add(createDifficultyPanel("Advance", fetchLeaderboardData("ADVANCE")));
        
        frame.add(mainPanel, BorderLayout.CENTER); // Add main content panel to the center
        frame.setVisible(true); // Make the frame visible
    }

    /**
     * Creates a panel displaying the leaderboard for a specific difficulty level.
     *
     * @param difficulty The difficulty level (Beginner, Intermediate, Advance).
     * @param entries A list of leaderboard entries (player and score).
     * @return A JPanel containing the leaderboard table for the specified difficulty level.
     */
    private JPanel createDifficultyPanel(String difficulty, List<LeaderboardEntry> entries) {
        JPanel panel = new JPanel(new BorderLayout()); // Panel to hold rankings for each difficulty
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),
                difficulty + " Level Rankings", TitledBorder.CENTER, TitledBorder.TOP)); // Add border with title

        // Create table model to hold leaderboard data
        String[] columnNames = {"Rank", "Player", "Score"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            private static final long serialVersionUID = 1L;

            // Prevent cells from being editable
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Add data to the table model (rank, player name, and score)
        for (int i = 0; i < entries.size(); i++) {
            LeaderboardEntry entry = entries.get(i);
            model.addRow(new Object[]{i + 1, entry.username, entry.score});
        }

        // Create and configure table to display leaderboard data
        JTable table = new JTable(model);
        table.setShowGrid(true); // Show grid lines
        table.setGridColor(Color.LIGHT_GRAY); // Set grid color
        table.getTableHeader().setBackground(new Color(52, 73, 94)); // Set header background color
        table.getTableHeader().setForeground(Color.WHITE); // Set header text color

        JScrollPane scrollPane = new JScrollPane(table); // Add table to scroll pane
        panel.add(scrollPane, BorderLayout.CENTER); // Add scroll pane to panel

        return panel; // Return the difficulty level panel
    }

    /**
     * Fetches the leaderboard data from the database for the specified difficulty level.
     *
     * @param difficulty The difficulty level for which to fetch leaderboard data.
     * @return A list of leaderboard entries containing player usernames and scores.
     */
    private List<LeaderboardEntry> fetchLeaderboardData(String difficulty) {
        List<LeaderboardEntry> entries = new ArrayList<>();
        String level = difficulty.toUpperCase(); // Convert difficulty level to uppercase
        String query = "SELECT email, overallScore FROM users WHERE level = ? ORDER BY overallScore DESC"; // Query to fetch leaderboard data

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD); // Establish DB connection
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, level); // Set level parameter in query
            ResultSet rs = stmt.executeQuery(); // Execute query and get results

            // Add leaderboard entries from the result set
            while (rs.next()) {
                entries.add(new LeaderboardEntry(
                    rs.getString("email"), // Get player email (username)
                    rs.getInt("overallScore") // Get player score
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Show error message if there is an issue fetching data from the database
            JOptionPane.showMessageDialog(null,
                "Error fetching leaderboard data: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }

        return entries; // Return the list of leaderboard entries
    }

    /**
     * Inner class to represent a leaderboard entry containing a player's username and score.
     */
    private static class LeaderboardEntry {
        String username; // Player's username (email)
        int score; // Player's score

        /**
         * Constructor to create a new leaderboard entry.
         *
         * @param username The username (email) of the player.
         * @param score The score of the player.
         */
        LeaderboardEntry(String username, int score) {
            this.username = username;
            this.score = score;
        }
    }
}
