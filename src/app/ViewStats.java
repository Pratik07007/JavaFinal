package app;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ViewStats extends JFrame {
    private JTextField searchField;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel totalPlayersLabel, highestMarkLabel, lowestMarkLabel, meanMarkLabel, totalScoresLabel;

    public ViewStats(Compitetor admin) {
        setTitle("Competitor Stats");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(44, 62, 80));
        add(topPanel, BorderLayout.NORTH);

        JButton backButton = createBackButton(admin);
        topPanel.add(backButton, BorderLayout.WEST);

        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.setBackground(new Color(44, 62, 80));

        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search by Email");
        styleButton(searchButton, new Color(52, 152, 219), Color.BLACK);
        searchButton.addActionListener(e -> searchUser());

        searchPanel.add(new JLabel("Email: "));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        topPanel.add(searchPanel, BorderLayout.EAST);

        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        loadTableData(null);

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

    private JButton createBackButton(Compitetor user) {
        JButton backButton = new JButton("← Back");
        styleButton(backButton, new Color(231, 76, 60), Color.BLACK);
        backButton.addActionListener(e -> {
        	if(user.getRole()=="ADMIN") {
        		
        		dispose();
        		new AdminPanelUI(user);
        	}else {
        		dispose();
        		new UserPannelUI(user);
        	}
        });
        return backButton;
    }

    private void styleButton(JButton button, Color bgColor, Color fgColor) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
    }

    private void loadTableData(String email) {
        String url = "jdbc:mysql://localhost:3306/quizApp";
        String user = "root";
        String password = "admin@12345";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT id, name, email, level, score1, score2, score3, score4, score5, overallScore FROM users WHERE role = 'USER'";
            if (email != null) sql += " AND email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            if (email != null) stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();
            tableModel.setRowCount(0);
            tableModel.setColumnIdentifiers(new String[]{"Competitor ID", "Name", "Email", "Level", "Score1", "Score2", "Score3", "Score4", "Score5", "Overall Score"});

            while (rs.next()) {
                int score1 = rs.getInt("score1");
                int score2 = rs.getInt("score2");
                int score3 = rs.getInt("score3");
                int score4 = rs.getInt("score4");
                int score5 = rs.getInt("score5");
                int calculatedOverallScore = score1 + score2 + score3 + score4 + score5;

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
                        calculatedOverallScore
                });
            }

            // Set custom column widths
            table.getColumnModel().getColumn(1).setPreferredWidth(200); // Name column width
            table.getColumnModel().getColumn(2).setPreferredWidth(300); // Email column width
            table.getColumnModel().getColumn(3).setPreferredWidth(100); // Level column width
            table.getColumnModel().getColumn(4).setPreferredWidth(80); // Score1 column width
            table.getColumnModel().getColumn(5).setPreferredWidth(80); // Score2 column width
            table.getColumnModel().getColumn(6).setPreferredWidth(80); // Score3 column width
            table.getColumnModel().getColumn(7).setPreferredWidth(80); // Score4 column width
            table.getColumnModel().getColumn(8).setPreferredWidth(80); // Score5 column width
            table.getColumnModel().getColumn(9).setPreferredWidth(120); // Overall Score column width
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void searchUser() {
        String email = searchField.getText().trim();
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter an email to search!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        loadTableData(email);
    }

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

                // Calculate and display total scores
                int totalScores = rs.getInt("totalScores");
                totalScoresLabel.setText("Total Scores: " + totalScores);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error retrieving stats!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
