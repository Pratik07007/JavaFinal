package app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The Home class represents the main screen of the application, where users
 * can either sign up or sign in to the quiz app.
 */
public class Home extends JFrame {

    /**
     * Constructor to set up the home screen with a welcome label and buttons
     * for Sign Up and Sign In.
     */
    public Home() {
        setTitle("MYQUIZ APP");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create main panel to hold the components
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(new Color(44, 62, 80));

        // Label for welcoming the user
        JLabel welcomeLabel = new JLabel("Welcome!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 60));
        welcomeLabel.setForeground(Color.WHITE);

        // Buttons for signing up and signing in
        JButton signUpButton = new JButton("Sign Up");
        JButton signInButton = new JButton("Sign In");
        
        // Style buttons using the styleButton helper method
        styleButton(signUpButton);
        styleButton(signInButton);

        // Action listener for the Sign Up button
        signUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Navigate to the Sign Up page
                new SignUpPage(); 
            }
        });

        // Action listener for the Sign In button
        signInButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Close current window and open Sign In page
                dispose();
                new SignInPage();
            }
        });

        // Panel for organizing the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.add(signUpButton);
        buttonPanel.add(signInButton);

        // Set layout and add components to main panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 0, 20, 0);
        mainPanel.add(welcomeLabel, gbc);

        gbc.gridy = 1;
        mainPanel.add(buttonPanel, gbc);

        // Add main panel to the frame
        add(mainPanel);

        // Set the frame visibility
        setVisible(true);
    }

    /**
     * Helper method to style the buttons with custom fonts, colors, and dimensions.
     *
     * @param button The JButton to style.
     */
    public static void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 60));
        button.setOpaque(true);
        button.setBorder(BorderFactory.createLineBorder(new Color(41, 128, 185), 3, true));
    }

    /**
     * Main method to launch the Home screen.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        new Home();
    }
}
