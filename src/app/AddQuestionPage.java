package app;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * AddQuestionPage is a JFrame that allows an admin to add a new question to the database.
 * It includes fields for the question, answer, options, and difficulty level, as well as buttons for adding the question and logging out.
 */
public class AddQuestionPage extends JFrame {

    private static final long serialVersionUID = 1L;

    // Declare form fields
    private JTextField textFieldQuestion, textFieldAnswer, textFieldOption1, textFieldOption2, textFieldOption3, textFieldOption4;
    private JComboBox<String> comboBoxLevel;

    /**
     * Constructor for the AddQuestionPage.
     * Initializes the UI components and layout for the page where admins can add new questions.
     * 
     * @param admin The current administrator object, used to display the admin's name.
     */
    public AddQuestionPage(Compitetor admin) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Create content pane with layout and background color
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPane.setBackground(new Color(44, 62, 80));
        setContentPane(contentPane);

        // Back Button Setup
        JButton backButton = createBackButton(admin);
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(new Color(44, 62, 80));
        topPanel.add(backButton);
        contentPane.add(topPanel, BorderLayout.NORTH);

        // Center Panel for form fields
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(44, 62, 80));
        contentPane.add(centerPanel, BorderLayout.CENTER);

        // GridBagConstraints for positioning form fields
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        // Welcome Label
        JLabel lblWelcome = new JLabel("Welcome, " + admin.getName() + "!");
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 20));
        lblWelcome.setForeground(Color.WHITE);
        centerPanel.add(lblWelcome, gbc);

        gbc.gridwidth = 1; // Reset for form fields

        // Add form fields to the panel
        addFormField(centerPanel, gbc, "Question:", textFieldQuestion = new JTextField(30));
        addFormField(centerPanel, gbc, "Level:", comboBoxLevel = new JComboBox<>(new String[]{"Beginner", "Intermediate", "Advanced"}));
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
        buttonPanel.setBackground(new Color(44, 62, 80));

        // Add Question Button
        JButton btnAddQuestion = new JButton("Add Question");
        styleButton(btnAddQuestion, new Color(46, 204, 113), Color.BLACK);
        btnAddQuestion.addActionListener(new AddQuestionAction());
        buttonPanel.add(btnAddQuestion);

        centerPanel.add(buttonPanel, gbc);

        // Logout Button
        JButton btnLogout = new JButton("Logout");
        styleButton(btnLogout, new Color(231, 76, 60), Color.BLACK);
        btnLogout.addActionListener(e -> {
            dispose();
            new Home();
        });
        contentPane.add(btnLogout, BorderLayout.SOUTH);

        setVisible(true);
    }

    /**
     * Creates a Back button to return to the Admin Panel.
     * 
     * @param admin The current administrator object.
     * @return A JButton configured with an action listener for going back to the admin panel.
     */
    private JButton createBackButton(Compitetor admin) {
        ImageIcon backIcon = new ImageIcon(new ImageIcon("/Users/pratikdhimal/Desktop/Remove Background Preview.png").getImage()
                .getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        JButton backButton = new JButton(backIcon);
        backButton.setPreferredSize(new Dimension(40, 40));
        backButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        backButton.setBackground(new Color(44, 62, 80));
        backButton.addActionListener(e -> {
            new AdminPanelUI(admin);
            dispose();
        });
        return backButton;
    }

    /**
     * Helper method to add form fields (labels and text fields) to the given panel.
     * 
     * @param panel The JPanel where the form fields will be added.
     * @param gbc GridBagConstraints to manage component layout.
     * @param labelText The text to display for the label.
     * @param field The JComponent (JTextField or JComboBox) to add to the form.
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
        field.setPreferredSize(new Dimension(250, 40)); // Increased height for better visual appearance
        panel.add(field, gbc);
    }

    /**
     * Helper method to style buttons with background and foreground colors.
     * 
     * @param button The JButton to style.
     * @param bgColor The background color of the button.
     * @param fgColor The foreground color (text color) of the button.
     */
    private void styleButton(JButton button, Color bgColor, Color fgColor) {
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
    }

    /**
     * ActionListener for the "Add Question" button to add a new question to the database.
     */
    private class AddQuestionAction implements ActionListener {
        /**
         * Handles the event of clicking the "Add Question" button.
         * Retrieves form data, validates fields, and calls JDBC to add the question to the database.
         * 
         * @param e The ActionEvent triggered by clicking the button.
         */
        public void actionPerformed(ActionEvent e) {
            String question = textFieldQuestion.getText().trim();
            String answer = textFieldAnswer.getText().trim();
            String option1 = textFieldOption1.getText().trim();
            String option2 = textFieldOption2.getText().trim();
            String option3 = textFieldOption3.getText().trim();
            String option4 = textFieldOption4.getText().trim();
            String level = ((String) comboBoxLevel.getSelectedItem()).toUpperCase();

            // Validate that all fields are filled
            if (question.isEmpty() || answer.isEmpty() || option1.isEmpty() || option2.isEmpty() || option3.isEmpty() || option4.isEmpty()) {
                JOptionPane.showMessageDialog(AddQuestionPage.this, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Call JDBC method to add the question
            ReturnMessage response = JDBC.addQuestion(question, level, answer, option1, option2, option3, option4);
            if (response.success) {
                // Show success message and reset fields
                JOptionPane.showMessageDialog(AddQuestionPage.this, response.msg, "Success", JOptionPane.INFORMATION_MESSAGE);
                textFieldQuestion.setText("");
                textFieldAnswer.setText("");
                textFieldOption1.setText("");
                textFieldOption2.setText("");
                textFieldOption3.setText("");
                textFieldOption4.setText("");
                comboBoxLevel.setSelectedIndex(0);
            } else {
                // Show error message
                JOptionPane.showMessageDialog(AddQuestionPage.this, response.msg, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
