package app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * This class provides the user interface for viewing the details of a player. 
 * It allows searching for a player by their ID (email) and displays detailed information about the player.
 */
class PlayerDetailsUI {
    private JLabel nameLabel, emailLabel, levelLabel, scoreLabel, fullDetailsLabel;
    private JPanel detailsPanel;

    /**
     * Constructor that initializes the PlayerDetailsUI frame and components.
     *
     * @param user The currently logged-in user (Compitetor object).
     */
    public PlayerDetailsUI(Compitetor user) {
        // Create the main frame for the player details UI
        JFrame frame = new JFrame("Player Details");
        frame.setSize(800, 400); // Set size of the frame
        frame.setLayout(new BorderLayout()); // Set layout of the frame

        // Panel for taking input (player ID search)
        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(new Color(52, 73, 94)); // Set background color
        inputPanel.setLayout(new FlowLayout()); // Set flow layout for input panel

        // Create search field and button
        JTextField searchField = new JTextField(10);
        JButton seeResultButton = new JButton("See Result");
        seeResultButton.setFont(new Font("Arial", Font.BOLD, 14)); // Set button font

        // Add components to the input panel
        inputPanel.add(new JLabel("Enter Player ID: "));
        inputPanel.add(searchField);
        inputPanel.add(seeResultButton);

        // Panel to display player details
        detailsPanel = new JPanel();
        detailsPanel.setBackground(new Color(44, 62, 80)); // Set background color
        detailsPanel.setLayout(new GridLayout(5, 1, 10, 10)); // Set grid layout for details
        detailsPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.WHITE),
            "Player Details", 0, 0,
            new Font("Arial", Font.BOLD, 16),
            Color.WHITE
        ));

        // Initialize labels to display player details
        nameLabel = new JLabel("Name: " + user.getName());
        emailLabel = new JLabel("Email: " + user.getEmail());
        levelLabel = new JLabel("Level: " + user.getLevel());
        scoreLabel = new JLabel("Overall Score: " + user.getOverallScore() + "/25");
        fullDetailsLabel = new JLabel(user.getFullDetails());

        // Set fonts for the labels
        Font labelFont = new Font("Arial", Font.BOLD, 18);
        Font descriptionFont = new Font("Arial", Font.ITALIC, 20);

        nameLabel.setFont(labelFont);
        emailLabel.setFont(labelFont);
        levelLabel.setFont(labelFont);
        scoreLabel.setFont(labelFont);
        fullDetailsLabel.setFont(descriptionFont);

        // Set text color for labels
        nameLabel.setForeground(Color.WHITE);
        emailLabel.setForeground(Color.WHITE);
        levelLabel.setForeground(Color.WHITE);
        scoreLabel.setForeground(Color.WHITE);
        fullDetailsLabel.setForeground(Color.WHITE);

        // Add labels to the details panel
        detailsPanel.add(nameLabel);
        detailsPanel.add(emailLabel);
        detailsPanel.add(levelLabel);
        detailsPanel.add(scoreLabel);
        detailsPanel.add(fullDetailsLabel);

        // Add action listener to the "See Result" button
        seeResultButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputId = searchField.getText();
                ReturnMessage response = JDBC.getUserByEmail(inputId);
                
                if (!response.success) {
                    // Show an error message if the player is not found
                    JOptionPane.showMessageDialog(frame, response.msg, "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Update the UI with the new player details
                    Compitetor newUser = response.user;
                    nameLabel.setText("Name: " + newUser.getName());
                    emailLabel.setText("Email: " + newUser.getEmail());
                    levelLabel.setText("Level: " + newUser.getLevel());
                    scoreLabel.setText("Overall Score: " + newUser.getOverallScore() + "/25");
                    fullDetailsLabel.setText(newUser.getFullDetails());
                    detailsPanel.revalidate(); // Revalidate the panel
                    detailsPanel.repaint();    // Repaint the panel to update the UI
                }
            }
        });

        // Add panels to the frame
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(detailsPanel, BorderLayout.CENTER);

        // Add a window listener to open the user panel when the window is closed
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose(); // Dispose the current frame
                new UserPannelUI(user); // Re-open the UserPannelUI
            }
        });

        // Set the frame to be visible
        frame.setVisible(true);
    }
}
