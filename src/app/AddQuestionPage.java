package app;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AddQuestionPage extends JFrame {

    private static final long serialVersionUID = 1L;
    
    private JTextField textFieldQuestion, textFieldAnswer, textFieldOption1, textFieldOption2, textFieldOption3, textFieldOption4;
    private JComboBox<String> comboBoxLevel; 

    public AddQuestionPage(Compitetor admin) {
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
        JLabel lblWelcome = new JLabel("Welcome, " + admin.name + "!");
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 20));
        lblWelcome.setForeground(Color.BLACK);
        centerPanel.add(lblWelcome, gbc);
        
        // Reset gridwidth for subsequent rows
        gbc.gridwidth = 1;
        
        addFormField(centerPanel, gbc, "Question:", textFieldQuestion = new JTextField(30));
        addFormField(centerPanel, gbc, "Level:", comboBoxLevel = new JComboBox<>(new String[]{"Beginner", "Intermediate", "Advance"}));
        addFormField(centerPanel, gbc, "Answer:", textFieldAnswer = new JTextField(30));
        addFormField(centerPanel, gbc, "Option 1:", textFieldOption1 = new JTextField(20));
        addFormField(centerPanel, gbc, "Option 2:", textFieldOption2 = new JTextField(20));
        addFormField(centerPanel, gbc, "Option 3:", textFieldOption3 = new JTextField(20));
        addFormField(centerPanel, gbc, "Option 4:", textFieldOption4 = new JTextField(20));

        // Buttons Panel
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel(new FlowLayout());

        // Add Question Button
        JButton btnAddQuestion = new JButton("Add Question");
        styleButton(btnAddQuestion, new Color(46, 204, 113), Color.BLACK);
        btnAddQuestion.addActionListener(new AddQuestionAction());
        buttonPanel.add(btnAddQuestion);

        // Update Question Button
        JButton btnUpdateQuestion = new JButton("Update an existing question");
        styleButton(btnUpdateQuestion, new Color(231, 76, 60), Color.BLACK);
        btnUpdateQuestion.addActionListener(e -> new ManageQuestionPages(admin)); //navigates to EditQuestionsPage
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

    // Action for the "Add Question" button
    private class AddQuestionAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Convert inputs to uppercase and trim spaces
            String question = textFieldQuestion.getText();
            String answer = textFieldAnswer.getText();
            String option1 = textFieldOption1.getText();
            String option2 = textFieldOption2.getText();
            String option3 = textFieldOption3.getText();
            String option4 = textFieldOption4.getText();
            String level = ((String) comboBoxLevel.getSelectedItem()).toUpperCase();

            // Validate if any field is empty
            if (question.isEmpty() || answer.isEmpty() || option1.isEmpty() || option2.isEmpty() || option3.isEmpty() || option4.isEmpty()) {
                JOptionPane.showMessageDialog(AddQuestionPage.this, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            
            ReturnMessage response = JDBC.addQuestion(question, level, answer, option1, option2, option3, option4);
            if (response.success) {
                JOptionPane.showMessageDialog(AddQuestionPage.this, response.msg, "Success", JOptionPane.INFORMATION_MESSAGE);

                // Clear input fields after successful addition
                textFieldQuestion.setText("");
                textFieldAnswer.setText("");
                textFieldOption1.setText("");
                textFieldOption2.setText("");
                textFieldOption3.setText("");
                textFieldOption4.setText("");
                comboBoxLevel.setSelectedIndex(0);

            } else {
                JOptionPane.showMessageDialog(AddQuestionPage.this, response.msg, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
