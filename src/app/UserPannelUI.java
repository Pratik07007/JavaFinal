package app;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.List;
import java.util.Map;

public class UserPannelUI {
    public UserPannelUI(Compitetor user) {
    	Map<String, Runnable> userActions = new LinkedHashMap<>();
        userActions.put("Play Quiz", () -> new PlayQuiz(user));
        userActions.put("View High Scores", () -> new LeaderboardUI(user));
        userActions.put("View Player Details", () -> new PlayerDetailsUI(user));
        userActions.put("Logout", () -> System.exit(0));

        new DashBoardUI("Welcome " + user.getName(), userActions);
    }
}

class LeaderboardUI {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/quizApp";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "admin@12345";

    public LeaderboardUI(Compitetor user) {
        JFrame frame = new JFrame("High Scores");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 73, 94));

        JLabel label = new JLabel("High Scores Page", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 30));
        label.setForeground(Color.WHITE);

        // Load and scale the back button image
        ImageIcon backIcon = new ImageIcon(new ImageIcon("/Users/pratikdhimal/Desktop/Remove Background Preview.png")
                .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        JButton backButton = new JButton(backIcon);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> {
            frame.dispose();
            new UserPannelUI(user);
        });

        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(label, BorderLayout.CENTER);
        frame.add(headerPanel, BorderLayout.NORTH);

        // Main Content Panel
        JPanel mainPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create and add difficulty level panels
        mainPanel.add(createDifficultyPanel("Beginner", fetchLeaderboardData("BEGINNER")));
        mainPanel.add(createDifficultyPanel("Intermediate", fetchLeaderboardData("INTERMEDIATE")));
        mainPanel.add(createDifficultyPanel("Advance", fetchLeaderboardData("ADVANCE")));
        
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private JPanel createDifficultyPanel(String difficulty, List<LeaderboardEntry> entries) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),
                difficulty + " Level Rankings", TitledBorder.CENTER, TitledBorder.TOP));

        // Create table model
        String[] columnNames = {"Rank", "Player", "Score"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            private static final long serialVersionUID = 1L;

			@Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Add data to table
        for (int i = 0; i < entries.size(); i++) {
            LeaderboardEntry entry = entries.get(i);
            model.addRow(new Object[]{i + 1, entry.username, entry.score});
        }

        // Create and configure table
        JTable table = new JTable(model);
        table.setShowGrid(true);
        table.setGridColor(Color.LIGHT_GRAY);
        table.getTableHeader().setBackground(new Color(52, 73, 94));
        table.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private List<LeaderboardEntry> fetchLeaderboardData(String difficulty) {
        List<LeaderboardEntry> entries = new ArrayList<>();
        String level = difficulty.toUpperCase();
        String query = "SELECT email, overallScore FROM users WHERE level = ? ORDER BY overallScore DESC";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, level);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                entries.add(new LeaderboardEntry(
                    rs.getString("email"),
                    rs.getInt("overallScore")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                "Error fetching leaderboard data: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }

        return entries;
    }

    private static class LeaderboardEntry {
        String username;
        int score;

        LeaderboardEntry(String username, int score) {
            this.username = username;
            this.score = score;
        }
    }
}


class PlayerDetailsUI {
    private JLabel nameLabel, emailLabel, levelLabel, scoreLabel, fullDetailsLabel;
    private JPanel detailsPanel;

    public PlayerDetailsUI(Compitetor user) {
        JFrame frame = new JFrame("Player Details");
        frame.setSize(800, 400);
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(new Color(52, 73, 94));
        inputPanel.setLayout(new FlowLayout());

        JTextField searchField = new JTextField(10);
        JButton seeResultButton = new JButton("See Result");
        seeResultButton.setFont(new Font("Arial", Font.BOLD, 14));

        inputPanel.add(new JLabel("Enter Player ID: "));
        inputPanel.add(searchField);
        inputPanel.add(seeResultButton);

        detailsPanel = new JPanel();
        detailsPanel.setBackground(new Color(44, 62, 80));
        detailsPanel.setLayout(new GridLayout(5, 1, 10, 10));
        detailsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE), "Player Details", 0, 0, new Font("Arial", Font.BOLD, 16), Color.WHITE));

        nameLabel = new JLabel("Name: " + user.getName());
        emailLabel = new JLabel("Email: " + user.getEmail());

        levelLabel = new JLabel("Level: " + user.getLevel());
        scoreLabel = new JLabel("Overall Score: " + user.getOverallScore() + "/25");
        fullDetailsLabel = new JLabel(user.getFullDetails());

        Font labelFont = new Font("Arial", Font.BOLD, 18);
        Font descriptionFont = new Font("Arial", Font.ITALIC, 20);

        nameLabel.setFont(labelFont);
        emailLabel.setFont(labelFont);

        levelLabel.setFont(labelFont);
        scoreLabel.setFont(labelFont);
        fullDetailsLabel.setFont(descriptionFont);

        nameLabel.setForeground(Color.WHITE);
        emailLabel.setForeground(Color.WHITE);
        fullDetailsLabel.setForeground(Color.WHITE);

        levelLabel.setForeground(Color.WHITE);
        scoreLabel.setForeground(Color.WHITE);
        fullDetailsLabel.setForeground(Color.WHITE);

        detailsPanel.add(nameLabel);
        detailsPanel.add(emailLabel);

        detailsPanel.add(levelLabel);
        detailsPanel.add(scoreLabel);
        detailsPanel.add(fullDetailsLabel);

        seeResultButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputId = searchField.getText();
                ReturnMessage response = JDBC.getUserByEmail(inputId);
                if (!response.success) {
                    JOptionPane.showMessageDialog(frame, response.msg, "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    Compitetor newUser = response.user;
                    nameLabel.setText("Name: " + newUser.getName());
                    emailLabel.setText("Email: " + newUser.getEmail());
                    levelLabel.setText("Level: " + newUser.getLevel());
                    scoreLabel.setText("Overall Score: " + newUser.getOverallScore() + "/25");
                    fullDetailsLabel.setText(newUser.getFullDetails());
                    detailsPanel.revalidate();
                    detailsPanel.repaint();
                }
            }
        });

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(detailsPanel, BorderLayout.CENTER);
        
        // Add Window Listener to load UserPannelUI when the window is closed
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose(); // Dispose the current frame
                new UserPannelUI(user); // Re-open the UserPannelUI
            }
        });

        frame.setVisible(true);
    }

}