package app;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UpdateQuestionPage extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTextField textFieldQuestion, textFieldAnswer, textFieldOption1, textFieldOption2, textFieldOption3, textFieldOption4;
    private JComboBox<String> comboBoxLevel;
    private Questions question;  // Add this to store the question object

    public UpdateQuestionPage(Questions question,Users admin) {
        this.question = question; // Store the question object

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        // Set the content pane to use BorderLayout
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20)); 
        setContentPane(contentPane);
        
        // Create a center panel that uses GridBagLayout to center its content
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(245, 245, 245));
        contentPane.add(centerPanel, BorderLayout.CENTER);
        
        // Create GridBagConstraints for consistent spacing and centering
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // space around components
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; 
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        
        // Welcome Label
        JLabel lblWelcome = new JLabel("Update this Question:");
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 20));
        lblWelcome.setForeground(Color.BLACK);
        centerPanel.add(lblWelcome, gbc);
        
        // Reset gridwidth for subsequent rows
        gbc.gridwidth = 1;
        
        // Add fields with default values
        addFormField(centerPanel, gbc, "Question:", textFieldQuestion = new JTextField(question.getText(), 30));
        addFormField(centerPanel, gbc, "Level:", comboBoxLevel = new JComboBox<>(new String[]{"Beginner", "Intermediate", "Advance"}));
        comboBoxLevel.setSelectedItem(question.getLevel());  // Default level from the question
        addFormField(centerPanel, gbc, "Answer:", textFieldAnswer = new JTextField(question.getAnswer(), 30));
        addFormField(centerPanel, gbc, "Option 1:", textFieldOption1 = new JTextField(question.getOpt1(), 20));
        addFormField(centerPanel, gbc, "Option 2:", textFieldOption2 = new JTextField(question.getOpt2(), 20));
        addFormField(centerPanel, gbc, "Option 3:", textFieldOption3 = new JTextField(question.getOpt3(), 20));
        addFormField(centerPanel, gbc, "Option 4:", textFieldOption4 = new JTextField(question.getOpt4(), 20));

        // Buttons Panel
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel(new FlowLayout());

        // Update Question Button
        JButton btnUpdateQuestion = new JButton("Update Question");
        styleButton(btnUpdateQuestion, new Color(46, 204, 113), Color.BLACK);
        btnUpdateQuestion.addActionListener(new UpdateQuestionAction());
        buttonPanel.add(btnUpdateQuestion);

        centerPanel.add(buttonPanel, gbc);

        // Logout Button
        JButton btnLogout = new JButton("Logout");
        styleButton(btnLogout, new Color(46, 204, 113), Color.BLACK);
        btnLogout.addActionListener(e -> {
            dispose();
            new Home();
        });
        contentPane.add(btnLogout, BorderLayout.SOUTH);

        // Back Button with Icon
        ImageIcon backIcon = new ImageIcon(new ImageIcon("/Users/pratikdhimal/Desktop/Remove Background Preview.png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        JButton btnBack = new JButton(backIcon);
        btnBack.setPreferredSize(new Dimension(50, 50));
        btnBack.setContentAreaFilled(false);
        btnBack.setBorderPainted(false);
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();  // Close current window
                new ManageQuestionPages(admin); 
            }
        });

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(btnBack);  // Add the back button to the top panel
        contentPane.add(topPanel, BorderLayout.NORTH);

        setVisible(true);
    }

    // Helper method to add form fields
    private void addFormField(JPanel panel, GridBagConstraints gbc, String labelText, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(Color.BLACK);
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        field.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(field, gbc);
    }

    // Helper method to style buttons
    private void styleButton(JButton button, Color bgColor, Color fgColor) {
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
    }

    // Action for the "Update Question" button
    private class UpdateQuestionAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Get the inputs
            String questionText = textFieldQuestion.getText();
            String answer = textFieldAnswer.getText();
            String option1 = textFieldOption1.getText();
            String option2 = textFieldOption2.getText();
            String option3 = textFieldOption3.getText();
            String option4 = textFieldOption4.getText();
            String level = ((String) comboBoxLevel.getSelectedItem()).toUpperCase();

            // Validate if any field is empty
            if (questionText.isEmpty() || answer.isEmpty() || option1.isEmpty() || option2.isEmpty() || option3.isEmpty() || option4.isEmpty()) {
                JOptionPane.showMessageDialog(UpdateQuestionPage.this, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Get the questionId using getId() method
            int questionId = question.getId();

            // Call the updateQuestion method with the questionId
            Boolean success = JDBC.updateQuestion(questionId, questionText, level, option1, option2, option3, option4, answer);
            
            if(success) {
                JOptionPane.showMessageDialog(UpdateQuestionPage.this, "Question updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(UpdateQuestionPage.this, "Question updation failed!", "Error", JOptionPane.ERROR_MESSAGE);
            }

            // Clear input fields after update
            textFieldQuestion.setText("");
            textFieldAnswer.setText("");
            textFieldOption1.setText("");
            textFieldOption2.setText("");
            textFieldOption3.setText("");
            textFieldOption4.setText("");
            comboBoxLevel.setSelectedIndex(0);
        }
    }
}
