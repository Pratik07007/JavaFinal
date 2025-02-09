package app;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ManageQuestionPages {
    private static final Color DARK_BACKGROUND = new Color(44, 62, 80);
    private static final Color PANEL_BACKGROUND = new Color(44, 62, 80);
    private static final Color TEXT_COLOR = new Color(220, 220, 230);
    
    private static final Color UPDATE_COLOR = new Color(80, 250, 123);
    private static final Color DELETE_COLOR = new Color(255, 85, 85);

    private JFrame frame;
    private JPanel contentPanel;
    private ArrayList<JPanel> questionPanels;

    public ManageQuestionPages(Compitetor admin) {
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
            new AdminPanelUI(admin);
        });
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(DARK_BACKGROUND);
        topPanel.add(backButton);
        contentPanel.add(topPanel);

        JLabel lblTitle = new JLabel("Manage Questions", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(TEXT_COLOR);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(lblTitle);

        ArrayList<Questions> questions = JDBC.fetchAllQuestions();
        for (Questions question : questions) {
            renderQuestion(question, admin);
        }

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.getViewport().setBackground(DARK_BACKGROUND);
        scrollPane.setBorder(null);
        frame.add(scrollPane);

        frame.setVisible(true);
    }

    private void renderQuestion(Questions question, Compitetor admin) {
        JPanel questionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        questionPanel.setBackground(PANEL_BACKGROUND);
        questionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblQuestion = new JLabel(question.getText());
        lblQuestion.setFont(new Font("Arial", Font.PLAIN, 16));
        lblQuestion.setForeground(TEXT_COLOR);
        questionPanel.add(lblQuestion);
        
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

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(PANEL_BACKGROUND);

        JButton btnUpdate = createStyledButton("Update", UPDATE_COLOR);
        btnUpdate.addActionListener(e -> new UpdateQuestionPage(question, admin));
        buttonPanel.add(btnUpdate);

        JButton btnDelete = createStyledButton("Delete", DELETE_COLOR);
        btnDelete.addActionListener(e -> {
            int confirmDelete = JOptionPane.showConfirmDialog(
                frame, 
                "Are you sure you want to delete this question?", 
                "Confirm Delete", 
                JOptionPane.YES_NO_OPTION
            );
            
            if (confirmDelete == JOptionPane.YES_OPTION) {
                Boolean response = JDBC.deleteQuestion(question.getId());
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
                    contentPanel.repaint();
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
        contentPanel.add(questionPanel);
    }

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
