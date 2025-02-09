package app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Home extends JFrame {
    public Home() {
        setTitle("MYQUIZ APP");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(new Color(44, 62, 80));

        JLabel welcomeLabel = new JLabel("Welcome!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 60));
        welcomeLabel.setForeground(Color.WHITE);

        JButton signUpButton = new JButton("Sign Up");
        JButton signInButton = new JButton("Sign In");
        styleButton(signUpButton);
        styleButton(signInButton);
        
        signUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                System.out.println("Sign Up Clicked");
                new SignUpPage(); 
            }
        });

        signInButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                System.out.println("Sign In Clicked");
                dispose();
                new SignInPage();
            }
        });
        

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.add(signUpButton);
        buttonPanel.add(signInButton);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 0, 20, 0);
        mainPanel.add(welcomeLabel, gbc);

        gbc.gridy = 1;
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel);
        
        
        setVisible(true);
        
        
    }

    private  void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 60));
        button.setOpaque(true);
        button.setBorder(BorderFactory.createLineBorder(new Color(41, 128, 185), 3, true));
    }

    public static void main(String[] args) {
        new Home();
    }
}
