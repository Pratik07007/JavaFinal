package app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ManageQuestionPages {

    private JFrame frame;
    private JPanel contentPanel;
    private ArrayList<JPanel> questionPanels; // Store question panels to update or delete them

    public ManageQuestionPages(Compitetor admin) {
        frame = new JFrame();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximized window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize the list to store question panels
        questionPanels = new ArrayList<>();

        // Main panel with BoxLayout for vertical stacking
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS)); // Stack vertically
        contentPanel.setBackground(new Color(255, 255, 255)); // Set a background color if needed

        // Back button on top-left
        ImageIcon backIcon = new ImageIcon(new ImageIcon("/Users/pratikdhimal/Desktop/Remove Background Preview.png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        JButton btnBack = new JButton(backIcon);
        btnBack.setFont(new Font("Arial", Font.BOLD, 14)); // Styling the button
        contentPanel.add(btnBack);

        // ActionListener for Back Button
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Back button clicked");
                frame.dispose();
                new AddQuestionPage(admin);
            }
        });

        // Title - Manage Questions (centered at the top)
        JLabel lblTitle = new JLabel("Manage Questions");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24)); // Large, bold font for the title
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the label
        contentPanel.add(lblTitle);

        // Render multiple questions
     // Fetch questions from database
       ArrayList<Questions> questions = JDBC.fetchAllQuestions();

        
        // Iterate through the list and print each question's text
        for (Questions question : questions) {
            System.out.println("Rendering question: " + question.getText());  // Log the question text
            renderQuestion(question,admin);  // Render the question in the UI
        }



        // Wrap the contentPanel inside JScrollPane to make it scrollable
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Make the frame visible
        frame.setVisible(true);
    }

    
    
    public void renderQuestion(Questions question,Compitetor admin) {
        JPanel questionPanel = new JPanel();
        questionPanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // Buttons and label in a horizontal layout

        // Label to display the question or text
        JLabel lblQuestion = new JLabel(question.getText());
        lblQuestion.setFont(new Font("Arial", Font.PLAIN, 16)); // Set font size
        questionPanel.add(lblQuestion);

        // Update button
        JButton btnUpdate = new JButton("Update");
        btnUpdate.setFont(new Font("Arial", Font.PLAIN, 14));
        questionPanel.add(btnUpdate);

        // ActionListener for Update Button
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	new UpdateQuestionPage(question,admin);
            }
        });

        // Delete button
        JButton btnDelete = new JButton("Delete");
        btnDelete.setFont(new Font("Arial", Font.PLAIN, 14));
        questionPanel.add(btnDelete);

        // ActionListener for Delete Button
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirmDelete = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this question?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirmDelete == JOptionPane.YES_OPTION) {                
                 Boolean response = JDBC.deleteQuestion(question.getId());
                    if(response) {
                    	JOptionPane.showMessageDialog(null, "Deleted Succesfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    	 contentPanel.remove(questionPanel);  // Remove the panel from the UI
                         questionPanels.remove(questionPanel); // Remove it from the list to track it
                         contentPanel.revalidate();
                         contentPanel.repaint();
                    }else {
                    	JOptionPane.showMessageDialog(null, "Deletion Failed", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Add the question panel to the main content panel and track it
        questionPanels.add(questionPanel);
        contentPanel.add(questionPanel);
    }
   
}
