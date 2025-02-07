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
        // Fetch the questions for the current difficulty level
        List<Questions> questions = JDBC.fetchRandomQuestionsByDifficulty(user.getLevel().toUpperCase());
        Collections.shuffle(questions);

        if (questions.isEmpty()) {
            System.out.println("No questions found.");
            return;
        }

        // Dispose of the previous frame (if it exists)
        if (frame != null) {
            frame.dispose();
        }

        // Create a new frame
        frame = new JFrame("Quiz");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout(10, 10));

        // Create a panel to hold the questions
        questionPanel = new JPanel();
        questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));
        questionPanel.setBackground(new Color(44, 62, 80));  // Updated to match the color theme

        // Initialize the button groups and option buttons
        buttonGroups = new ButtonGroup[questions.size()];
        optionButtons = new JRadioButton[questions.size()][4];

        // Loop through each question and create components
        int questionIndex = 0;
        for (Questions question : questions) {
            // Create a panel for each question
            JPanel singleQuestionPanel = new JPanel();
            singleQuestionPanel.setLayout(new BoxLayout(singleQuestionPanel, BoxLayout.Y_AXIS));
            singleQuestionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));  // Add margin to each question panel
            singleQuestionPanel.setBackground(new Color(44, 62, 80));  // Match the background color of the main panel

            // Display the question
            JLabel questionLabel = new JLabel("<html><b>" + question.getText() + "</b></html>");
            questionLabel.setFont(new Font("Arial", Font.BOLD, 18));
            questionLabel.setForeground(Color.WHITE);  // Set text color to white
            questionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            singleQuestionPanel.add(questionLabel);

            // Create radio buttons for the options
            ButtonGroup group = new ButtonGroup();
            int optionIndex = 0;
            for (String option : question.getOptions()) {
                JRadioButton optionButton = new JRadioButton(option);
                optionButton.setAlignmentX(Component.LEFT_ALIGNMENT);
                optionButton.setFont(new Font("Arial", Font.PLAIN, 16));
                optionButton.setBackground(new Color(44, 62, 80));  // Set the background color of options
                optionButton.setForeground(Color.WHITE);  // Set text color to white
                group.add(optionButton);
                singleQuestionPanel.add(optionButton);
                optionButtons[questionIndex][optionIndex] = optionButton;
                optionIndex++;
            }

            // Add the question panel to the main question panel
            questionPanel.add(singleQuestionPanel);
            buttonGroups[questionIndex] = group;
            questionIndex++;
        }

        // Add the question panel to the frame inside a scroll pane for smooth navigation
        JScrollPane scrollPane = new JScrollPane(questionPanel);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Add the submit button with proper styling and alignment
        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.BOLD, 18));
        submitButton.setBackground(new Color(41, 128, 185));  // Keep submit button color
        submitButton.setForeground(Color.BLACK);
        submitButton.setFocusPainted(false);
        submitButton.setPreferredSize(new Dimension(200, 60));
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int score = 0;

                // Check answers
                for (int i = 0; i < questions.size(); i++) {
                    for (int j = 0; j < 4; j++) {
                        if (optionButtons[i][j].isSelected()) {
                            String selectedOption = optionButtons[i][j].getText();
                            if (selectedOption.equals(questions.get(i).getAnswer())) {
                                score++;
                            }
                        }
                    }
                }

                // Update the score in the database
                updateScoreInDatabase(score);

                // Check if the maximum score (5) has been reached
                if (user.getScoreCount() >= 5) {
                    JOptionPane.showMessageDialog(frame, "You have completed all 5 rounds. Your score will be available in the get details section");
                    frame.dispose();
                    new UserPannelUI(user);
                } else {
                    showNextSetOfQuestions();
                }
            }
        });

        // Add the submit button to the bottom of the frame with padding
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setBackground(new Color(44, 62, 80));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20));
        buttonPanel.add(submitButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        

        // Make the frame visible
        frame.setVisible(true);
    }

    private void updateScoreInDatabase(int score) {
        // Get the current score column based on the user's score count
        String scoreColumn = "score" + (user.getScoreCount() + 1);

        // Update the individual round score
        Boolean response = JDBC.updateScore(user.getId(), scoreColumn, score);
        if(!response) {
        	JOptionPane.showMessageDialog(frame, "Something Went wrong");
        	frame.dispose();
        }

        // Increment the user's score count
        user.incrementScoreCount();

        // Fetch updated scores from the database
        int[] updatedScores = JDBC.fetchUserScores(user.getId());

        // Update the user's scores array
        user.setScores(updatedScores);

        // Calculate overall score
        double overallScore = user.getOverallScore();

        // Update the overall score in the database
        JDBC.updateOverallScore(user.getId(), overallScore);
    }

}
