package app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The SuperAdmin class represents a UI for the super admin to add new admins.
 * It allows input of name, email, and password with validation.
 */
public class SuperAdmin extends JFrame {
    /**
     * Constructs the SuperAdmin UI.
     * Sets up the layout, components, and event listeners.
     */
    public SuperAdmin() {
        setTitle("Super Admin - Add Admin");
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
        
        JLabel nameLabel = new JLabel("Name:");
        styleLabel(nameLabel);

        JTextField nameFeild = new JTextField(20);
        styleTextField(nameFeild);

        JLabel emailLabel = new JLabel("Email:");
        styleLabel(emailLabel);

        JTextField emailField = new JTextField(20);
        styleTextField(emailField);

        JLabel passwordLabel = new JLabel("Password:");
        styleLabel(passwordLabel);

        JPasswordField passwordField = new JPasswordField(20);
        styleTextField(passwordField);

        JButton submitButton = new JButton("Add Admin");
        styleButton(submitButton, new Color(231, 76, 60)); // Red color for admin action

        /**
         * Action listener for the submit button.
         * Validates input and attempts to add an admin.
         */
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String name = nameFeild.getText();
                String password = new String(passwordField.getPassword());
                if (email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(SuperAdmin.this, "Fields cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (password.length() < 6) {
                    JOptionPane.showMessageDialog(SuperAdmin.this, "Password must be at least 6 characters!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if(!EmailValidiator.isValidEmail(email)) {
                    JOptionPane.showMessageDialog(SuperAdmin.this, "Please Enter a Valid email", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                ReturnMessage response =  JDBC.addAdmin(email,password,name);
                if(response.success) {
                    JOptionPane.showMessageDialog(SuperAdmin.this, response.msg, "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    new SignInPage();
                } else {
                    JOptionPane.showMessageDialog(SuperAdmin.this, response.msg, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        gbc.gridy = 0;
        mainPanel.add(nameLabel, gbc);

        gbc.gridy = 1;
        mainPanel.add(nameFeild, gbc);
        
        gbc.gridy = 2;
        mainPanel.add(emailLabel, gbc);

        gbc.gridy = 3;
        mainPanel.add(emailField, gbc);

        gbc.gridy = 4;
        mainPanel.add(passwordLabel, gbc);

        gbc.gridy = 5;
        mainPanel.add(passwordField, gbc);

        gbc.gridy = 6;
        mainPanel.add(submitButton, gbc);

        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    /**
     * Styles a button with a specific background color.
     * @param button the button to style
     * @param bgColor the background color
     */
    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 50));
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    /**
     * Styles a label with font and color.
     * @param label the label to style
     */
    private void styleLabel(JLabel label) {
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setForeground(Color.WHITE);
    }

    /**
     * Styles a text field with font and size.
     * @param field the text field to style
     */
    private void styleTextField(JTextField field) {
        field.setFont(new Font("Arial", Font.PLAIN, 18));
        field.setPreferredSize(new Dimension(250, 40));
    }

    /**
     * Main method to launch the SuperAdmin UI.
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        new SuperAdmin();
    }
}
