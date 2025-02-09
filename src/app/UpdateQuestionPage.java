package app;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The UpdateQuestionPage class represents the UI for updating an existing question.
 * It allows modification of the question text, answer, options, and difficulty level.
 */
public class UpdateQuestionPage extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField textFieldQuestion, textFieldAnswer, textFieldOption1, textFieldOption2, textFieldOption3, textFieldOption4;
    private JComboBox<String> comboBoxLevel;
    private Questions question;

    /**
     * Constructs the UpdateQuestionPage UI.
     * @param question The question object to be updated.
     * @param admin The admin user performing the update.
     */
    public UpdateQuestionPage(Questions question, Compitetor admin) {
        this.question = question;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPane.setBackground(new Color(44, 62, 80));
        setContentPane(contentPane);
        
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(44, 62, 80));
        contentPane.add(centerPanel, BorderLayout.CENTER);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; 
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        
        JLabel lblWelcome = new JLabel("Update this Question:");
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 20));
        lblWelcome.setForeground(Color.WHITE);
        centerPanel.add(lblWelcome, gbc);
        
        gbc.gridwidth = 1;
        addFormField(centerPanel, gbc, "Question:", textFieldQuestion = new JTextField(question.getQuestionText(), 30));
        addFormField(centerPanel, gbc, "Level:", comboBoxLevel = new JComboBox<>(new String[]{"Beginner", "Intermediate", "Advance"}));
        comboBoxLevel.setSelectedItem(question.getLevel());
        addFormField(centerPanel, gbc, "Answer:", textFieldAnswer = new JTextField(question.getAnswer(), 30));
        addFormField(centerPanel, gbc, "Option 1:", textFieldOption1 = new JTextField(question.getOpt1(), 20));
        addFormField(centerPanel, gbc, "Option 2:", textFieldOption2 = new JTextField(question.getOpt2(), 20));
        addFormField(centerPanel, gbc, "Option 3:", textFieldOption3 = new JTextField(question.getOpt3(), 20));
        addFormField(centerPanel, gbc, "Option 4:", textFieldOption4 = new JTextField(question.getOpt4(), 20));

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnUpdateQuestion = new JButton("Update Question");
        styleButton(btnUpdateQuestion, new Color(46, 204, 113), Color.BLACK);
        btnUpdateQuestion.addActionListener(new UpdateQuestionAction());
        buttonPanel.add(btnUpdateQuestion);
        centerPanel.add(buttonPanel, gbc);

        JButton btnLogout = new JButton("Logout");
        styleButton(btnLogout, new Color(46, 204, 113), Color.BLACK);
        btnLogout.addActionListener(e -> {
            dispose();
            new Home();
        });
        contentPane.add(btnLogout, BorderLayout.SOUTH);

        ImageIcon backIcon = new ImageIcon(new ImageIcon("/Users/pratikdhimal/Desktop/Remove Background Preview.png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        JButton btnBack = new JButton(backIcon);
        btnBack.setPreferredSize(new Dimension(50, 50));
        btnBack.setContentAreaFilled(false);
        btnBack.setBorderPainted(false);
        btnBack.addActionListener(e -> {
            dispose();
            new ManageQuestionPages(admin);
        });

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(btnBack);
        contentPane.add(topPanel, BorderLayout.NORTH);
        contentPane.setBackground(new Color(44, 62, 80));
        setVisible(true);
    }

    /**
     * Adds a form field with a label and input field.
     * @param panel The panel to add the field to.
     * @param gbc The layout constraints.
     * @param labelText The label text.
     * @param field The input field component.
     */
    private void addFormField(JPanel panel, GridBagConstraints gbc, String labelText, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(Color.WHITE);
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        field.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(field, gbc);
    }

    /**
     * Styles a button with specific background and foreground colors.
     * @param button The button to style.
     * @param bgColor The background color.
     * @param fgColor The foreground color.
     */
    private void styleButton(JButton button, Color bgColor, Color fgColor) {
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
    }

    /**
     * Handles the action of updating a question.
     */
    private class UpdateQuestionAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String questionText = textFieldQuestion.getText();
            String answer = textFieldAnswer.getText();
            String option1 = textFieldOption1.getText();
            String option2 = textFieldOption2.getText();
            String option3 = textFieldOption3.getText();
            String option4 = textFieldOption4.getText();
            String level = ((String) comboBoxLevel.getSelectedItem()).toUpperCase();

            if (questionText.isEmpty() || answer.isEmpty() || option1.isEmpty() || option2.isEmpty() || option3.isEmpty() || option4.isEmpty()) {
                JOptionPane.showMessageDialog(UpdateQuestionPage.this, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int questionId = question.getId();
            Boolean success = JDBC.updateQuestion(questionId, questionText, level, option1, option2, option3, option4, answer);
            
            if (success) {
                JOptionPane.showMessageDialog(UpdateQuestionPage.this, "Question updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(UpdateQuestionPage.this, "Question updation failed!", "Error", JOptionPane.ERROR_MESSAGE);
            }

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
