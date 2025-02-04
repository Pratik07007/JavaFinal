package app;

import javax.swing.*;
import java.awt.*;

public class UserPannelUI {
    private JFrame frame;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    public UserPannelUI(String name) {
        frame = new JFrame("My Quiz App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Home Page
        JPanel homePanel = createHomePage(name);
        mainPanel.add(homePanel, "Home");
        
        // Quiz Page
        JPanel quizPanel = createPage("Play Quiz");
        mainPanel.add(quizPanel, "Quiz");
        
        // High Scores Page
        JPanel highScorePanel = createPage("High Scores");
        mainPanel.add(highScorePanel, "HighScores");
        
        // Player Details Page
        JPanel playerDetailsPanel = createPage("Player Details");
        mainPanel.add(playerDetailsPanel, "PlayerDetails");

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private JPanel createHomePage(String name) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(50, 150, 250)); // Background color

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(20, 0, 20, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Welcome "+name+" !", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);

        JButton playButton = createStyledButton("Play Quiz");
        JButton highScoreButton = createStyledButton("View High Scores");
        JButton playerDetailsButton = createStyledButton("View Player Details");

        playButton.addActionListener(e -> cardLayout.show(mainPanel, "Quiz"));
        highScoreButton.addActionListener(e -> cardLayout.show(mainPanel, "HighScores"));
        playerDetailsButton.addActionListener(e -> cardLayout.show(mainPanel, "PlayerDetails"));

        panel.add(titleLabel, gbc);
        panel.add(playButton, gbc);
        panel.add(highScoreButton, gbc);
        panel.add(playerDetailsButton, gbc);

        return panel;
    }

    private JPanel createPage(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(70, 180, 250));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(Color.WHITE);

        JButton backButton = new JButton(new ImageIcon("/Users/pratikdhimal/Desktop/Remove Background Preview.png"));
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setFocusPainted(false);
        backButton.setPreferredSize(new Dimension(60, 60));
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Home"));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);
        topPanel.add(backButton);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 0, 20, 0);

        centerPanel.add(titleLabel, gbc);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);

        return panel;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(300, 80));
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setBackground(Color.WHITE);
        button.setForeground(new Color(50, 50, 50));
        return button;
    }

   
}
