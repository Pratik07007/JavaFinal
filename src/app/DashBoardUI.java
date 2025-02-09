package app;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * The DashBoardUI class represents the main dashboard UI that displays a set of buttons for admin actions.
 * Each button is associated with a specific action that can be performed by the admin.
 */
public class DashBoardUI {

    /**
     * Constructor to create and display the dashboard UI with a set of buttons for different actions.
     * 
     * @param title The title of the dashboard, typically a welcome message for the admin.
     * @param actions A map containing button labels and their associated actions. The keys are button labels,
     *                and the values are the actions to be performed when the buttons are clicked.
     */
    public DashBoardUI(String title, Map<String, Runnable> actions) {
        // Create the frame for the dashboard UI
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());

        // Create the main panel to hold the buttons and title
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(44, 62, 80));

        // Set up GridBagConstraints for button layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create and set the title label for the dashboard
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);

        // Add the title label to the panel
        gbc.gridy = 0;
        mainPanel.add(titleLabel, gbc);

        // Add action buttons to the panel
        int row = 1;
        for (String buttonText : actions.keySet()) {
            JButton button = createButton(buttonText, frame, actions.get(buttonText));
            gbc.gridy = row++;
            mainPanel.add(button, gbc);
        }

        // Add the main panel to the frame
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    /**
     * Creates a button with the given label and associates it with a specific action.
     * 
     * @param text The label to be displayed on the button.
     * @param frame The JFrame to dispose when the button is clicked.
     * @param action The action to be performed when the button is clicked.
     * @return A JButton with the specified label and action.
     */
    private JButton createButton(String text, JFrame frame, Runnable action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setBackground(new Color(41, 128, 185));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(500, 80));
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Set the action for the button when clicked
        button.addActionListener(e -> {
            frame.dispose(); // Close the current frame
            try {
                action.run(); // Execute the associated action
            } catch (Exception ex) {
                System.out.println(ex); // Handle any exceptions that occur during action execution
            }
        });
        return button;
    }
}
