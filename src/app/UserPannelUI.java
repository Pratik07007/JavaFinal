package app;

import javax.swing.*;
import java.awt.*;

public class UserPannelUI {
    public UserPannelUI(Users user) {
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
        
        JButton playButton = createButton("Play Quiz", frame, () -> new QuizUI(user));
        JButton highScoreButton = createButton("View High Scores", frame, () -> new LeaderboardUI(user));
        JButton playerDetailsButton = createButton("View Player Details", frame, () -> new PlayerDetailsUI(user));
        JButton logoutButton = createButton("Logout", frame, () -> System.exit(0));
        
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
            action.run();
        });
        return button;
    }
}

class QuizUI {
    public QuizUI(Users user) {
        JFrame frame = new JFrame("Quiz");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());
        
        JPanel panel = new JPanel();
        panel.setBackground(new Color(52, 73, 94));
        
        JLabel label = new JLabel("Quiz Page", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 30));
        label.setForeground(Color.WHITE);
        
        JButton backButton = createBackButton(frame, user);
        
        panel.add(label);
        frame.add(backButton, BorderLayout.WEST);
        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
    
    JButton createBackButton(JFrame frame, Users user) {
        JButton backButton = new JButton("←");
        backButton.setFont(new Font("Arial", Font.BOLD, 20));
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            frame.dispose();
            new UserPannelUI(user);
        });
        return backButton;
    }
}

class LeaderboardUI {
    public LeaderboardUI(Users user) {
        JFrame frame = new JFrame("High Scores");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());
        
        JPanel panel = new JPanel();
        panel.setBackground(new Color(52, 73, 94));
        
        JLabel label = new JLabel("High Scores Page", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 30));
        label.setForeground(Color.WHITE);
        
        JButton backButton = new QuizUI(user).createBackButton(frame, user);
        
        panel.add(label);
        frame.add(backButton, BorderLayout.WEST);
        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}

class PlayerDetailsUI {
    public PlayerDetailsUI(Users user) {
        JFrame frame = new JFrame("Player Details");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());
        
        JPanel panel = new JPanel();
        panel.setBackground(new Color(52, 73, 94));
        
        JLabel label = new JLabel("Player Details Page", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 30));
        label.setForeground(Color.WHITE);
        
        JButton backButton = new QuizUI(user).createBackButton(frame, user);
        
        panel.add(label);
        frame.add(backButton, BorderLayout.WEST);
        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}