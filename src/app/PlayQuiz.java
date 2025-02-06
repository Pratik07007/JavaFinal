package app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        List<Questions> questions = JDBC.fetchQuestionDifficultyLevel(user.level.toUpperCase());

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
        frame.setSize(600, 500);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        // Create a panel to hold the questions
        questionPanel = new JPanel();
        questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));

        // Initialize the button groups and option buttons
        buttonGroups = new ButtonGroup[questions.size()];
        optionButtons = new JRadioButton[questions.size()][4];

        // Loop through each question and create components
        int questionIndex = 0;
        for (Questions question : questions) {
            // Create a panel for each question
            JPanel singleQuestionPanel = new JPanel();
            singleQuestionPanel.setLayout(new BoxLayout(singleQuestionPanel, BoxLayout.Y_AXIS));

            // Display the question
            JLabel questionLabel = new JLabel("<html><b>" + question.getText() + "</b></html>");
            questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            singleQuestionPanel.add(questionLabel);

            // Create radio buttons for the options
            ButtonGroup group = new ButtonGroup();
            int optionIndex = 0;
            for (String option : question.getOptions()) {
                JRadioButton optionButton = new JRadioButton(option);
                optionButton.setAlignmentX(Component.CENTER_ALIGNMENT);
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

        // Add the question panel to the frame
        JScrollPane scrollPane = new JScrollPane(questionPanel);
        frame.add(scrollPane);

        // Add the submit button
        JButton submitButton = new JButton("Submit");
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
                    JOptionPane.showMessageDialog(frame, "You have completed all 5 rounds. Your score will be availabe in the get details section");
                    System.exit(0);  
                } else {
                   
                    showNextSetOfQuestions();
                }
            }
        });

        // Add the submit button to the frame
        frame.add(submitButton);

        // Make the frame visible
        frame.setVisible(true);
    }

    private void updateScoreInDatabase(int score) {
        // Get the current score column based on the user's score count
        String scoreColumn = "score" + (user.getScoreCount() + 1);

        // Update the score in the database
        JDBC.updateScore(user.id, scoreColumn, score);

        // Increment the user's score count
        user.incrementScoreCount();
    }
}
