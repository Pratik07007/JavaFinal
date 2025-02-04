package app;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.List;
import java.util.ArrayList;

public class UpdatePage extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel questionsPanel;

    public UpdatePage() {
        setTitle("Update Questions");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Main content panel
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(contentPane);

        // Title Label
        JLabel lblTitle = new JLabel("Manage Questions");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setForeground(Color.BLACK);
        contentPane.add(lblTitle, BorderLayout.NORTH);

        // Scrollable Questions Panel
        questionsPanel = new JPanel();
        questionsPanel.setLayout(new BoxLayout(questionsPanel, BoxLayout.Y_AXIS));
        questionsPanel.setBackground(new Color(245, 245, 245));

        JScrollPane scrollPane = new JScrollPane(questionsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        // Load Questions (Replace with Database Fetch)
        loadQuestions();

        setVisible(true);
    }

    private void loadQuestions() {
        // Mock List of Questions (Replace with DB Data)
        List<String> questions = new ArrayList<>();
        questions.add("What is Java?");
        questions.add("Explain OOP concepts.");
        questions.add("What is Polymorphism?");
        questions.add("Difference between JDK, JRE, and JVM?");
        questions.add("What are Java collections?");
        questions.add("What is Java?");
        questions.add("Explain OOP concepts.");
        questions.add("What is Polymorphism?");
        questions.add("Difference between JDK, JRE, and JVM?");
        questions.add("What are Java collections?");
        questions.add("What is Java?");
        questions.add("Explain OOP concepts.");
        questions.add("What is Polymorphism?");
        questions.add("Difference between JDK, JRE, and JVM?");
        questions.add("What are Java collections?");
        questions.add("What is Java?");
        questions.add("Explain OOP concepts.");
        questions.add("What is Polymorphism?");
        questions.add("Difference between JDK, JRE, and JVM?");
        questions.add("What are Java collections?");
        questions.add("What is Java?");
        questions.add("Explain OOP concepts.");
        questions.add("What is Polymorphism?");
        questions.add("Difference between JDK, JRE, and JVM?");
        questions.add("What are Java collections?");
        questions.add("What is Java?");
        questions.add("Explain OOP concepts.");
        questions.add("What is Polymorphism?");
        questions.add("Difference between JDK, JRE, and JVM?");
        questions.add("What are Java collections?");
        questions.add("What is Java?");
        questions.add("Explain OOP concepts.");
        questions.add("What is Polymorphism?");
        questions.add("Difference between JDK, JRE, and JVM?");
        questions.add("What are Java collections?");
        questions.add("What is Java?");
        questions.add("Explain OOP concepts.");
        questions.add("What is Polymorphism?");
        questions.add("Difference between JDK, JRE, and JVM?");
        questions.add("What are Java collections?");
        
        questionsPanel.removeAll(); // Clear panel before reloading

        for (String question : questions) {
            addQuestionPanel(question);
        }

        questionsPanel.revalidate();
        questionsPanel.repaint();
    }

    private void addQuestionPanel(String questionText) {
        JPanel questionPanel = new JPanel(new BorderLayout());
        questionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        questionPanel.setBackground(Color.WHITE);
        questionPanel.setPreferredSize(new Dimension(800, 80));
        questionPanel.setMaximumSize(new Dimension(800, 80));

        // Question Label
        JLabel lblQuestion = new JLabel(questionText);
        lblQuestion.setFont(new Font("Arial", Font.BOLD, 16));
        lblQuestion.setForeground(Color.BLACK);
        questionPanel.add(lblQuestion, BorderLayout.WEST);

        // Button Panel (Update & Delete)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);

        JButton btnUpdate = new JButton("Update");
        styleButton(btnUpdate, new Color(46, 204, 113), Color.BLACK);
        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Update clicked for: " + questionText);
                // Implement update logic here
            }
        });

        JButton btnDelete = new JButton("Delete");
        styleButton(btnDelete, new Color(231, 76, 60), Color.BLACK);
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Delete clicked for: " + questionText);
                // Implement delete logic here
            }
        });

        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);

        questionPanel.add(buttonPanel, BorderLayout.EAST);
        questionsPanel.add(questionPanel);
    }

    // Helper method to style buttons
    private void styleButton(JButton button, Color bgColor, Color fgColor) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(100, 35));
    }
}
