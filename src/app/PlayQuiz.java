package app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;

public class PlayQuiz {
    private Compitetor user;
    private JFrame frame;
    private JPanel questionPanel;
    private ButtonGroup[] buttonGroups;
    private JRadioButton[][] optionButtons;
    private int questionIndex = 0;

    public PlayQuiz(Compitetor user) {
        this.user = user;
        showNextSetOfQuestions();
    }

    private void showNextSetOfQuestions() {
        List<Questions> questions = JDBC.fetchRandomQuestionsByDifficulty(user.getLevel().toUpperCase());
        Collections.shuffle(questions);

        if (questions.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No questions found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (frame != null) {
            frame.dispose();
        }

        // Frame setup
        frame = new JFrame("Quiz - Level: " + user.getLevel().toUpperCase());
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setMinimumSize(new Dimension(800, 600));
        frame.setLayout(new BorderLayout(10, 10));

        // Add window closing listener
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                showExitConfirmation();
            }
        });

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(44, 62, 80));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Title Label
        JLabel titleLabel = new JLabel("Quiz Round " + (user.getScoreCount() + 1) + " of 5");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Exit Button in Header
        JButton exitButton = new JButton("Exit Quiz");
        exitButton.setFont(new Font("Arial", Font.BOLD, 14));
        exitButton.setBackground(new Color(231, 76, 60));
        exitButton.setForeground(Color.BLACK);
        exitButton.setFocusPainted(false);
        exitButton.addActionListener(e -> showExitConfirmation());
        headerPanel.add(exitButton, BorderLayout.EAST);

        frame.add(headerPanel, BorderLayout.NORTH);

        // Question Panel
        questionPanel = new JPanel();
        questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));
        questionPanel.setBackground(new Color(44, 62, 80));
        questionPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        buttonGroups = new ButtonGroup[questions.size()];
        optionButtons = new JRadioButton[questions.size()][4];

        // Create questions with improved spacing
        for (int i = 0; i < questions.size(); i++) {
            Questions question = questions.get(i);
            JPanel questionContainer = createQuestionPanel(question, i);
            
            // Add spacing between questions
            if (i < questions.size() - 1) {
                questionContainer.add(Box.createRigidArea(new Dimension(0, 20)));
            }
            
            questionPanel.add(questionContainer);
        }

        // Scroll Pane
        JScrollPane scrollPane = new JScrollPane(questionPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        frame.add(scrollPane, BorderLayout.CENTER);

        // Submit Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(44, 62, 80));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        
        JButton submitButton = createSubmitButton(questions);
        buttonPanel.add(submitButton);
        
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private JPanel createQuestionPanel(Questions question, int index) {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(new Color(44, 62, 80));
        container.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(52, 73, 94), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Question number and text
        JLabel questionLabel = new JLabel("<html><b>Question " + (index + 1) + ":</b> " + question.getText() + "</html>");
        questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        questionLabel.setForeground(Color.WHITE);
        questionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        container.add(questionLabel);
        container.add(Box.createRigidArea(new Dimension(0, 10)));

        // Options
        ButtonGroup group = new ButtonGroup();
        buttonGroups[index] = group;

        for (int i = 0; i < 4; i++) {
            JRadioButton option = new JRadioButton(question.getOptions()[i]);
            option.setFont(new Font("Arial", Font.PLAIN, 14));
            option.setForeground(Color.WHITE);
            option.setBackground(new Color(44, 62, 80));
            option.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            group.add(option);
            container.add(option);
            container.add(Box.createRigidArea(new Dimension(0, 5)));
            
            optionButtons[index][i] = option;
        }

        return container;
    }

    private JButton createSubmitButton(List<Questions> questions) {
        JButton submitButton = new JButton("Submit Answers");
        submitButton.setFont(new Font("Arial", Font.BOLD, 16));
        submitButton.setBackground(new Color(41, 128, 185));
        submitButton.setForeground(Color.BLACK);
        submitButton.setFocusPainted(false);
        submitButton.setPreferredSize(new Dimension(200, 50));

        submitButton.addActionListener(e -> {
            if (!validateAllQuestionsAnswered()) {
                JOptionPane.showMessageDialog(frame, 
                    "Please answer all questions before submitting.", 
                    "Incomplete Quiz", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            int score = calculateScore(questions);
            updateScoreInDatabase(score);

            if (user.getScoreCount() >= 5) {
                JOptionPane.showMessageDialog(frame, 
                    "Quiz completed! Your final score will be available in the details section.",
                    "Quiz Complete", 
                    JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
                new UserPannelUI(user);
            } else {
                showNextSetOfQuestions();
            }
        });

        return submitButton;
    }

    private boolean validateAllQuestionsAnswered() {
        for (ButtonGroup group : buttonGroups) {
            if (group.getSelection() == null) {
                return false;
            }
        }
        return true;
    }

    private int calculateScore(List<Questions> questions) {
        int score = 0;
        for (int i = 0; i < questions.size(); i++) {
            for (int j = 0; j < 4; j++) {
                if (optionButtons[i][j].isSelected() && 
                    optionButtons[i][j].getText().equals(questions.get(i).getAnswer())) {
                    score++;
                }
            }
        }
        return score;
    }

    private void showExitConfirmation() {
        int response = JOptionPane.showConfirmDialog(frame,
            "Are you sure you want to exit? Your progress will not be saved.",
            "Confirm Exit",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
            
        if (response == JOptionPane.YES_OPTION) {
            frame.dispose();
            new UserPannelUI(user);
        }
    }

    private void updateScoreInDatabase(int score) {
        String scoreColumn = "score" + (user.getScoreCount() + 1);
        Boolean response = JDBC.updateScore(user.getId(), scoreColumn, score);
        
        if (!response) {
            JOptionPane.showMessageDialog(frame,
                "Error updating score. Please try again.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            frame.dispose();
            return;
        }

        user.incrementScoreCount();
        user.setScores(JDBC.fetchUserScores(user.getId()));
        JDBC.updateOverallScore(user.getId(), user.getOverallScore());
    }
}