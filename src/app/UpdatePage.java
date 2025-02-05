package app;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UpdatePage extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable questionTable;
    private DefaultTableModel tableModel;
    private ExecutorService executorService;

    public UpdatePage(Users admin) {
        setTitle("Update Questions");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Initialize executor service for background tasks
        executorService = Executors.newSingleThreadExecutor();

        // Main content panel
        contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(contentPane);

        // Title Label
        JLabel lblTitle = new JLabel("Manage Questions");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setForeground(Color.BLACK);
        contentPane.add(lblTitle, BorderLayout.NORTH);

        // Back Button (with Logo) to AdminPage
        ImageIcon backIcon = new ImageIcon(new ImageIcon("/Users/pratikdhimal/Desktop/Remove Background Preview.png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        JButton btnBack = new JButton(backIcon);
        btnBack.setBorder(BorderFactory.createEmptyBorder()); // Remove border around the button
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Navigate back to AdminPage
                dispose(); // Close the current window
                new AdminPanel(admin); // Open AdminPage
            }
        });
        contentPane.add(btnBack, BorderLayout.WEST);

        // Table setup
        tableModel = new DefaultTableModel(new Object[]{"ID", "Question Text", "Update", "Delete"}, 0);
        questionTable = new JTable(tableModel);
        questionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        questionTable.setRowHeight(30);  // Adjust row height for better visibility

        // Table button rendering for actions (Update/Delete)
        questionTable.getColumn("Update").setCellRenderer(new ButtonRenderer("Update"));
        questionTable.getColumn("Update").setCellEditor(new ButtonEditor("Update"));
        questionTable.getColumn("Delete").setCellRenderer(new ButtonRenderer("Delete"));
        questionTable.getColumn("Delete").setCellEditor(new ButtonEditor("Delete"));

        // Scroll pane for table
        JScrollPane scrollPane = new JScrollPane(questionTable);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        // Load Questions asynchronously
        loadQuestions();

        setVisible(true);
    }

    private void loadQuestions() {
        executorService.submit(() -> {
            List<Questions> questions = JDBC.fetchAndShuffleQuestions();

            if (questions.isEmpty()) {
                System.out.println("No questions found in the database!");
            }

            // Add questions dynamically
            for (Questions question : questions) {
                SwingUtilities.invokeLater(() -> addQuestionToTable(question));
            }
        });
    }

    private void addQuestionToTable(Questions question) {
        tableModel.addRow(new Object[]{question.getId(), question.getText(), "Update", "Delete"});
    }

    private class ButtonRenderer extends JButton implements TableCellRenderer {

        private String actionType;

        public ButtonRenderer(String actionType) {
            this.actionType = actionType;
            setText(actionType);
            setFont(new Font("Arial", Font.BOLD, 14));
            setBackground(actionType.equals("Update") ? new Color(46, 204, 113) : new Color(231, 76, 60));
            setForeground(Color.BLACK); // Always black text
            setFocusPainted(false);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    private class ButtonEditor extends DefaultCellEditor {

        private JButton button;
        private int row;
        private String actionType;

        public ButtonEditor(String actionType) {
            super(new JCheckBox());
            this.actionType = actionType;
            button = new JButton();
            button.setFont(new Font("Arial", Font.BOLD, 14));
            button.setBackground(actionType.equals("Update") ? new Color(46, 204, 113) : new Color(231, 76, 60));
            button.setForeground(Color.BLACK); // Always black text
            button.setFocusPainted(false);

            // Remove color change on click
            button.setOpaque(true); // Ensures button color remains consistent

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    row = questionTable.getSelectedRow();
                    int questionId = (Integer) tableModel.getValueAt(row, 0);
                    String questionText = (String) tableModel.getValueAt(row, 1);

                    if (actionType.equals("Update")) {
                        // Implement update logic
                        System.out.println("Update button clicked for Question ID: " + questionId);
                        // Update logic here
                    } else if (actionType.equals("Delete")) {
                        int deleteConfirm = JOptionPane.showConfirmDialog(null,
                                "Are you sure you want to delete this question?",
                                "Confirm Delete",
                                JOptionPane.YES_NO_OPTION);

                        if (deleteConfirm == JOptionPane.YES_OPTION) {
                            boolean deleted = JDBC.deleteQuestion(questionId);
                            if (deleted) {
                                JOptionPane.showMessageDialog(UpdatePage.this, "Deleted Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                                tableModel.removeRow(row); // Remove from UI
                            } else {
                                JOptionPane.showMessageDialog(null, "Failed to delete question.");
                            }
                        }
                    }
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return button;
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        // Shutdown executor service to free resources
        executorService.shutdown();
    }
}
