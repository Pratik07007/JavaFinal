package app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignUpPage extends JFrame {
    public SignUpPage() {
        setTitle("Sign Up");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(new Color(44, 62, 80));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Back Button with Icon
        ImageIcon backIcon = new ImageIcon(new ImageIcon("/Users/pratikdhimal/Desktop/Remove Background Preview.png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        JButton backButton = new JButton(backIcon);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Home();
            }
        });

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(new Color(44, 62, 80));
        topPanel.add(backButton);

        JLabel nameLabel = new JLabel("Name:");
        styleLabel(nameLabel);

        JTextField nameField = new JTextField(20);
        styleTextField(nameField);

        JLabel emailLabel = new JLabel("Email:");
        styleLabel(emailLabel);

        JTextField emailField = new JTextField(20);
        styleTextField(emailField);

        JLabel passwordLabel = new JLabel("Password:");
        styleLabel(passwordLabel);

        JPasswordField passwordField = new JPasswordField(20);
        styleTextField(passwordField);

        JButton signUpButton = new JButton("Sign Up");
        styleButton(signUpButton, new Color(41, 128, 185));

        signUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                
                if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
                    JOptionPane.showMessageDialog(SignUpPage.this, "Fields cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (password.length() < 6) {
                    JOptionPane.showMessageDialog(SignUpPage.this, "Password must be at least 6 characters!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String hashedPassword = PasswordHash.hashPassword(password);
                
                ReturnMessage response = JDBC.registerUser(name, email, hashedPassword);
                if(response.success) {
                	JOptionPane.showMessageDialog(SignUpPage.this, response.msg, "Success", JOptionPane.ERROR_MESSAGE);
                	dispose();
                	new SignInPage();
                }else {
                	JOptionPane.showMessageDialog(SignUpPage.this, response.msg, "Error", JOptionPane.ERROR_MESSAGE);
                }
                
                
                
                
            }
        });

        gbc.gridy = 0;
        mainPanel.add(nameLabel, gbc);

        gbc.gridy = 1;
        mainPanel.add(nameField, gbc);

        gbc.gridy = 2;
        mainPanel.add(emailLabel, gbc);

        gbc.gridy = 3;
        mainPanel.add(emailField, gbc);

        gbc.gridy = 4;
        mainPanel.add(passwordLabel, gbc);

        gbc.gridy = 5;
        mainPanel.add(passwordField, gbc);

        gbc.gridy = 6;
        mainPanel.add(signUpButton, gbc);

        add(topPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 50));
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    private void styleLabel(JLabel label) {
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setForeground(Color.WHITE);
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("Arial", Font.PLAIN, 18));
        field.setPreferredSize(new Dimension(250, 40));
    }

    
}
