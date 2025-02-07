package app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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
    public LeaderboardUI(Compitetor user) {
        JFrame frame = new JFrame("High Scores");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());
        
        JPanel panel = new JPanel();
        panel.setBackground(new Color(52, 73, 94));
        
        JLabel label = new JLabel("High Scores Page", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 30));
        label.setForeground(Color.WHITE);
        
        ImageIcon backIcon = new ImageIcon(new ImageIcon("/Users/pratikdhimal/Desktop/Remove Background Preview.png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        JButton backButton = new JButton(backIcon);
        backButton.addActionListener(e -> {
            frame.dispose();
            new UserPannelUI(user); 
            
        });
       
        panel.add(label);
        frame.add(backButton, BorderLayout.WEST);
        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
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