package app;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class DashBoardUI {
    public DashBoardUI(String title, Map<String, Runnable> actions) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(44, 62, 80));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);

        gbc.gridy = 0;
        mainPanel.add(titleLabel, gbc);

        int row = 1;
        for (String buttonText : actions.keySet()) {
            JButton button = createButton(buttonText, frame, actions.get(buttonText));
            gbc.gridy = row++;
            mainPanel.add(button, gbc);
        }

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private JButton createButton(String text, JFrame frame, Runnable action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setBackground(new Color(41, 128, 185));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(500, 80));
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        button.addActionListener(e -> {
            frame.dispose();
            try {
                action.run();
            } catch (Exception ex) {
                System.out.println(ex);
            }
        });
        return button;
    }
}
