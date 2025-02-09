package app;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

/**
 * The ViewStats class represents a statistics panel for competitors.
 * It provides functionalities to search for users, filter by difficulty level, and display statistics.
 */
public class ViewStats extends JFrame {
    private JTextField searchField;
    private JComboBox<String> levelFilter;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel totalPlayersLabel, highestMarkLabel, lowestMarkLabel, meanMarkLabel, totalScoresLabel;

    /**
     * Constructs the ViewStats UI.
     * @param user The competitor user for whom the statistics panel is displayed.
     */
    public ViewStats(Compitetor user) {
        setTitle("Competitor Stats");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(44, 62, 80));
        add(topPanel, BorderLayout.NORTH);

        JButton backButton = createBackButton(user);
        topPanel.add(backButton, BorderLayout.WEST);

        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.setBackground(new Color(44, 62, 80));

        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        styleButton(searchButton, new Color(52, 152, 219), Color.BLACK);
        searchButton.addActionListener(e -> searchUser());

        levelFilter = new JComboBox<>(new String[]{"ALL", "BEGINNER", "INTERMEDIATE", "ADVANCED"});
        levelFilter.addActionListener(e -> searchUser());

        searchPanel.add(new JLabel("Email: "));
        searchPanel.add(searchField);
        searchPanel.add(new JLabel("Level: "));
        searchPanel.add(levelFilter);
        searchPanel.add(searchButton);
        topPanel.add(searchPanel, BorderLayout.EAST);

        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        loadTableData(null, "ALL");

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel statsPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        statsPanel.setBackground(new Color(236, 240, 241));

        totalPlayersLabel = new JLabel("Total Players: ");
        highestMarkLabel = new JLabel("Highest Mark: ");
        lowestMarkLabel = new JLabel("Lowest Mark: ");
        meanMarkLabel = new JLabel("Mean Mark: ");
        totalScoresLabel = new JLabel("Total Scores: ");

        statsPanel.add(totalPlayersLabel);
        statsPanel.add(highestMarkLabel);
        statsPanel.add(lowestMarkLabel);
        statsPanel.add(meanMarkLabel);
        statsPanel.add(totalScoresLabel);

        add(statsPanel, BorderLayout.EAST);
        updateStats();

        setVisible(true);
    }

    /**
     * Creates a back button to return to the Admin Panel.
     * @param user The competitor user.
     * @return The configured back button.
     */
    private JButton createBackButton(Compitetor user) {
        JButton backButton = new JButton("\u2190 Back");
        styleButton(backButton, new Color(231, 76, 60), Color.BLACK);
        backButton.addActionListener(e -> {
            dispose();
            new AdminPanelUI(user);
        });
        return backButton;
    }

    /**
     * Styles a button with specified colors.
     * @param button The button to style.
     * @param bgColor The background color.
     * @param fgColor The foreground color.
     */
    private void styleButton(JButton button, Color bgColor, Color fgColor) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
    }

    /**
     * Loads the table data based on email and level filters.
     * @param email The email filter.
     * @param level The level filter.
     */
    private void loadTableData(String email, String level) {
        String url = "jdbc:mysql://localhost:3306/quizApp";
        String user = "root";
        String password = "admin@12345";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT id, name, email, level, score1, score2, score3, score4, score5 FROM users WHERE role = 'USER'";
            if (email != null && !email.isEmpty()) sql += " AND email = ?";
            if (!"ALL".equals(level)) sql += " AND level = ?";

            PreparedStatement stmt = conn.prepareStatement(sql);
            int paramIndex = 1;
            if (email != null && !email.isEmpty()) stmt.setString(paramIndex++, email);
            if (!"ALL".equals(level)) stmt.setString(paramIndex, level);

            ResultSet rs = stmt.executeQuery();
            tableModel.setRowCount(0);
            tableModel.setColumnIdentifiers(new String[]{"Competitor ID", "Name", "Email", "Level", "Score1", "Score2", "Score3", "Score4", "Score5", "Overall Score"});

            while (rs.next()) {
                int score1 = rs.getInt("score1");
                int score2 = rs.getInt("score2");
                int score3 = rs.getInt("score3");
                int score4 = rs.getInt("score4");
                int score5 = rs.getInt("score5");
                int overallScore = score1 + score2 + score3 + score4 + score5;

                tableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("level"),
                        score1,
                        score2,
                        score3,
                        score4,
                        score5,
                        overallScore
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Searches for a user based on input email and level filter.
     */
    private void searchUser() {
        String email = searchField.getText().trim();
        String level = levelFilter.getSelectedItem().toString();
        loadTableData(email, level);
    }

    /**
     * Updates the statistics displayed on the UI.
     */
    private void updateStats() {
        String url = "jdbc:mysql://localhost:3306/quizApp";
        String user = "root";
        String password = "admin@12345";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT COUNT(*) AS totalPlayers, MAX(overallScore) AS highest, MIN(overallScore) AS lowest, AVG(overallScore) AS mean, " +
                         "SUM(score1 + score2 + score3 + score4 + score5) AS totalScores " +
                         "FROM users WHERE role = 'USER'";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                totalPlayersLabel.setText("Total Players: " + rs.getInt("totalPlayers"));
                highestMarkLabel.setText("Highest Mark: " + rs.getInt("highest"));
                lowestMarkLabel.setText("Lowest Mark: " + rs.getInt("lowest"));
                meanMarkLabel.setText("Mean Mark: " + rs.getDouble("mean"));
                totalScoresLabel.setText("Total Scores: " + rs.getInt("totalScores"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error retrieving stats!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
