package app;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.List;

public class UserPannelUI {
    public UserPannelUI(Compitetor user) {
        JFrame frame = new JFrame("My Quiz App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(44, 62, 80));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel titleLabel = new JLabel("Welcome " + user.getName() + "!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        
        JButton playButton = createButton("Play Quiz", frame, () -> new PlayQuiz(user));
        JButton highScoreButton = createButton("View High Scores", frame, () -> new LeaderboardUI(user));
        JButton playerDetailsButton = createButton("View Player Details", frame, null);
        
        playerDetailsButton.addActionListener(e -> {
        	
        	new PlayerDetailsUI(user);
            
        });
        JButton logoutButton = createButton("Logout", frame, () -> System.exit(0));
        logoutButton.setForeground(Color.RED);
        gbc.gridy = 0;
        mainPanel.add(titleLabel, gbc);
        gbc.gridy = 1;
        mainPanel.add(playButton, gbc);
        gbc.gridy = 2;
        mainPanel.add(highScoreButton, gbc);
        gbc.gridy = 3;
        mainPanel.add(playerDetailsButton, gbc);
        gbc.gridy = 4;
        mainPanel.add(logoutButton, gbc);
        
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
    
    private JButton createButton(String text, JFrame frame, Runnable action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setBackground(new Color(41, 128, 185));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(300, 80));
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.addActionListener(e -> {
            frame.dispose();
            try {
				
            	action.run();
			} catch (Exception e2) {
				System.out.println(e2);
			}
        });
        return button;
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
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(52, 73, 94));

        JLabel label = new JLabel("High Scores Page", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 30));
        label.setForeground(Color.WHITE);

        ImageIcon backIcon = new ImageIcon(new ImageIcon("/Users/pratikdhimal/Desktop/Remove Background Preview.png")
                .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        JButton backButton = new JButton(backIcon);
        backButton.addActionListener(e -> {
            frame.dispose();
            new UserPannelUI(user);
        });

        headerPanel.add(label);
        frame.add(backButton, BorderLayout.WEST);
        frame.add(headerPanel, BorderLayout.NORTH);

        // Main Content Panel
        JPanel mainPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create and add difficulty level panels
        mainPanel.add(createDifficultyPanel("Easy", fetchLeaderboardData("Easy")));
        mainPanel.add(createDifficultyPanel("Medium", fetchLeaderboardData("Medium")));
        mainPanel.add(createDifficultyPanel("Hard", fetchLeaderboardData("Hard")));

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private JPanel createDifficultyPanel(String difficulty, List<LeaderboardEntry> entries) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(difficulty + " Level Rankings"));

        // Create table model
        String[] columnNames = {"Rank", "Player", "Score"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Add data to table
        for (int i = 0; i < entries.size(); i++) {
            LeaderboardEntry entry = entries.get(i);
            model.addRow(new Object[]{
                i + 1,
                entry.username,
                entry.score
            });
        }

        // Create and configure table
        JTable table = new JTable(model);
        table.setShowGrid(true);
        table.setGridColor(Color.LIGHT_GRAY);
        table.getTableHeader().setBackground(new Color(52, 73, 94));
        table.getTableHeader().setForeground(Color.WHITE);

        // Add table to scrollpane
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private List<LeaderboardEntry> fetchLeaderboardData(String difficulty) {
        List<LeaderboardEntry> entries = new ArrayList<>();
        String level  = difficulty.toUpperCase();
        String query = "SELECT email, overallScore FROM users WHERE level = ? ORDER BY overallScore DESC";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, level);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                entries.add(new LeaderboardEntry(
                    rs.getString("username"),
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
    private JLabel nameLabel, emailLabel, levelLabel, scoreLabel,fullDetailsLabel;
    private JPanel detailsPanel;

    public PlayerDetailsUI(Compitetor user) {
        JFrame frame = new JFrame("Player Details");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        scoreLabel = new JLabel("Overall Score: " + user.getOverallScore()+"/25");
        fullDetailsLabel = new JLabel(user.getFullDetails());
               

        Font labelFont = new Font("Arial", Font.BOLD, 18);
        Font descriptionFont = new Font("Arial", Font.ITALIC, 20);
        
        nameLabel.setFont(labelFont);
        emailLabel.setFont(labelFont);
        
        
        levelLabel.setFont(labelFont);
        scoreLabel.setFont(labelFont);
        fullDetailsLabel.setFont(descriptionFont);
        
//        scoreLabel.setFont(labelFont);
        
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
                    scoreLabel.setText("Overall Score: " + newUser.getOverallScore()+"/25");
                    fullDetailsLabel.setText(newUser.getFullDetails());
                    detailsPanel.revalidate();
                    detailsPanel.repaint();
                }
            }
        });

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(detailsPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }


}