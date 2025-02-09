package app;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

/**
 * This class represents the Manage Questions page for the admin to manage and update quiz questions.
 * It allows the admin to view, update, and delete questions from the database.
 */
public class ManageQuestionPages {
    
    // Colors used for UI elements
    private static final Color DARK_BACKGROUND = new Color(44, 62, 80);
    private static final Color PANEL_BACKGROUND = new Color(44, 62, 80);
    private static final Color TEXT_COLOR = new Color(220, 220, 230);
    
    private static final Color UPDATE_COLOR = new Color(80, 250, 123); // Update button color
    private static final Color DELETE_COLOR = new Color(255, 85, 85); // Delete button color

    private JFrame frame;
    private JPanel contentPanel;
    private ArrayList<JPanel> questionPanels;

    /**
     * Constructor to create and display the Manage Questions page.
     *
     * @param admin The admin user who is managing the questions.
     */
    public ManageQuestionPages(Compitetor admin) {
        // Set up the JFrame for the manage questions screen
        frame = new JFrame("Manage Questions");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(DARK_BACKGROUND);

        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(DARK_BACKGROUND);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        questionPanels = new ArrayList<>();

        // Back button with custom styling
        ImageIcon backIcon = new ImageIcon(new ImageIcon("/Users/pratikdhimal/Desktop/Remove Background Preview.png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        JButton backButton = new JButton(backIcon);
        backButton.setPreferredSize(new Dimension(40, 40));
        backButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        backButton.setBackground(DARK_BACKGROUND);
        backButton.addActionListener(e -> {
            frame.dispose();
            new AdminPanelUI(admin); // Navigate back to Admin Panel
        });
        
        // Top panel for the back button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(DARK_BACKGROUND);
        topPanel.add(backButton);
        contentPanel.add(topPanel);

        // Title label for the screen
        JLabel lblTitle = new JLabel("Manage Questions", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(TEXT_COLOR);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(lblTitle);

        // Fetch all questions from the database and render them
        ArrayList<Questions> questions = JDBC.fetchAllQuestions();
        for (Questions question : questions) {
            renderQuestion(question, admin); // Render each question
        }

        // Scroll pane to make content scrollable
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.getViewport().setBackground(DARK_BACKGROUND);
        scrollPane.setBorder(null);
        frame.add(scrollPane);

        frame.setVisible(true); // Make the frame visible
    }

    /**
     * Renders each question with options for Update and Delete.
     *
     * @param question The question to be rendered.
     * @param admin The admin user who is managing the questions.
     */
    private void renderQuestion(Questions question, Compitetor admin) {
        JPanel questionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        questionPanel.setBackground(PANEL_BACKGROUND);
        questionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Display the question text
        JLabel lblQuestion = new JLabel(question.getQuestionText());
        lblQuestion.setFont(new Font("Arial", Font.PLAIN, 16));
        lblQuestion.setForeground(TEXT_COLOR);
        questionPanel.add(lblQuestion);
        
        // Display difficulty level with color-coded background
        JLabel difficulty = new JLabel(question.getLevel());
        difficulty.setFont(new Font("Arial", Font.PLAIN, 16));

        switch (question.getLevel().toUpperCase()) {
            case "ADVANCE":
                difficulty.setForeground(Color.WHITE);
                difficulty.setBackground(Color.RED);
                break;
            case "INTERMEDIATE":
                difficulty.setForeground(Color.WHITE);
                difficulty.setBackground(Color.BLUE);
                break;
            case "BEGINNER":
                difficulty.setForeground(Color.BLACK);
                difficulty.setBackground(Color.GREEN);
                break;
            default:
                difficulty.setForeground(Color.BLACK); 
                difficulty.setBackground(Color.LIGHT_GRAY);
        }

        difficulty.setOpaque(true);
        questionPanel.add(difficulty);

        // Button panel for Update and Delete actions
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(PANEL_BACKGROUND);

        // Update button
        JButton btnUpdate = createStyledButton("Update", UPDATE_COLOR);
        btnUpdate.addActionListener(e -> new UpdateQuestionPage(question, admin)); // Navigate to Update Question page
        buttonPanel.add(btnUpdate);

        // Delete button with confirmation
        JButton btnDelete = createStyledButton("Delete", DELETE_COLOR);
        btnDelete.addActionListener(e -> {
            int confirmDelete = JOptionPane.showConfirmDialog(
                frame, 
                "Are you sure you want to delete this question?", 
                "Confirm Delete", 
                JOptionPane.YES_NO_OPTION
            );
            
            if (confirmDelete == JOptionPane.YES_OPTION) {
                Boolean response = JDBC.deleteQuestion(question.getId()); // Delete question from DB
                if (response) {
                    JOptionPane.showMessageDialog(
                        null, 
                        "Deleted Successfully", 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    contentPanel.remove(questionPanel);
                    questionPanels.remove(questionPanel);
                    contentPanel.revalidate();
                    contentPanel.repaint(); // Refresh the UI after deletion
                } else {
                    JOptionPane.showMessageDialog(
                        null, 
                        "Deletion Failed", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
        buttonPanel.add(btnDelete);

        questionPanel.add(buttonPanel);
        questionPanels.add(questionPanel);
        contentPanel.add(questionPanel); // Add the question panel to content
    }

    /**
     * Helper method to create a styled button with a specified background color.
     *
     * @param text The text displayed on the button.
     * @param bgColor The background color of the button.
     * @return The styled button.
     */
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(120, 40));
        return button;
    }
}
