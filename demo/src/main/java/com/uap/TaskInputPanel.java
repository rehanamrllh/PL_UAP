package com.uap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Panel for adding and editing tasks with input validation.
 */
public class TaskInputPanel extends JPanel {
    private TaskManager taskManager;
    private MainDashboard parent;

    private JTextField titleField;
    private JTextArea descriptionArea;
    private JComboBox<String> priorityCombo;
    private JComboBox<String> statusCombo;
    private JButton saveButton;
    private JButton cancelButton;
    private JLabel formTitleLabel;

    private Task editingTask = null;

    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SECONDARY_COLOR = new Color(52, 73, 94);
    private static final Color ACCENT_COLOR = new Color(46, 204, 113);
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Color CARD_COLOR = Color.WHITE;

    public TaskInputPanel(TaskManager taskManager, MainDashboard parent) {
        this.taskManager = taskManager;
        this.parent = parent;

        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        initComponents();
    }

    private void initComponents() {
        // Title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(BACKGROUND_COLOR);
        formTitleLabel = new JLabel(" Add New Task");
        formTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        formTitleLabel.setForeground(SECONDARY_COLOR);
        titlePanel.add(formTitleLabel);

        add(titlePanel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = createFormPanel();
        add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createFormPanel() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(CARD_COLOR);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Title field
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        JLabel titleLabel = createLabel("Task Title: *");
        mainPanel.add(titleLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        titleField = new JTextField(30);
        titleField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleField.setPreferredSize(new Dimension(0, 35));
        mainPanel.add(titleField, gbc);

        // Description field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        JLabel descLabel = createLabel("Description:");
        mainPanel.add(descLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        descriptionArea = new JTextArea(6, 30);
        descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descScrollPane = new JScrollPane(descriptionArea);
        descScrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        mainPanel.add(descScrollPane, gbc);

        // Priority field
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel priorityLabel = createLabel("Priority: *");
        mainPanel.add(priorityLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        priorityCombo = new JComboBox<>(new String[] { "HIGH", "MEDIUM", "LOW" });
        priorityCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        priorityCombo.setPreferredSize(new Dimension(0, 35));
        priorityCombo.setSelectedIndex(1); // Default to MEDIUM
        mainPanel.add(priorityCombo, gbc);

        // Status field
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        JLabel statusLabel = createLabel("Status: *");
        mainPanel.add(statusLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        statusCombo = new JComboBox<>(new String[] { "PENDING", "IN_PROGRESS", "COMPLETED" });
        statusCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusCombo.setPreferredSize(new Dimension(0, 35));
        mainPanel.add(statusCombo, gbc);

        // Required field note
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JLabel noteLabel = new JLabel("* Required fields");
        noteLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        noteLabel.setForeground(new Color(127, 140, 141));
        mainPanel.add(noteLabel, gbc);

        return mainPanel;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(SECONDARY_COLOR);
        return label;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panel.setBackground(BACKGROUND_COLOR);

        saveButton = createButton(" Save Task", ACCENT_COLOR);
        saveButton.addActionListener(e -> saveTask());

        cancelButton = createButton(" Cancel", SECONDARY_COLOR);
        cancelButton.addActionListener(e -> {
            clearForm();
            parent.showPanel("TASK_LIST");
        });

        JButton clearButton = createButton(" Clear Form", new Color(52, 152, 219));
        clearButton.addActionListener(e -> clearForm());

        panel.add(saveButton);
        panel.add(cancelButton);
        panel.add(clearButton);

        return panel;
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(150, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });

        return button;
    }

    private void saveTask() {
        try {
            // Validate input
            String title = titleField.getText().trim();
            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Task title is required!",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                titleField.requestFocus();
                return;
            }

            String description = descriptionArea.getText().trim();
            String priority = (String) priorityCombo.getSelectedItem();
            String status = (String) statusCombo.getSelectedItem();

            // Validate priority and status
            if (priority == null || priority.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please select a priority!",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (status == null || status.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please select a status!",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success;
            if (editingTask == null) {
                // Add new task
                Task newTask = taskManager.addTask(title, description, priority, status);
                success = (newTask != null);
            } else {
                // Update existing task
                success = taskManager.updateTask(editingTask.getId(),
                        title, description, priority, status);
            }

            if (success) {
                String message = editingTask == null ? "Task added successfully!" : "Task updated successfully!";
                JOptionPane.showMessageDialog(this,
                        message,
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                clearForm();
                parent.showPanel("TASK_LIST");
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to save task. Please try again.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                    "Validation Error: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error saving task: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void clearForm() {
        titleField.setText("");
        descriptionArea.setText("");
        priorityCombo.setSelectedIndex(1); // Default to MEDIUM
        statusCombo.setSelectedIndex(0); // Default to PENDING
        editingTask = null;
        formTitleLabel.setText(" Add New Task");
        saveButton.setText(" Save Task");
    }

    public void loadTaskForEdit(Task task) {
        if (task == null) {
            clearForm();
            return;
        }

        this.editingTask = task;
        formTitleLabel.setText(" Edit Task");
        saveButton.setText(" Update Task");

        titleField.setText(task.getTitle());
        descriptionArea.setText(task.getDescription());

        // Set priority
        String priority = task.getPriority();
        for (int i = 0; i < priorityCombo.getItemCount(); i++) {
            if (priorityCombo.getItemAt(i).equals(priority)) {
                priorityCombo.setSelectedIndex(i);
                break;
            }
        }

        // Set status
        String status = task.getStatus();
        for (int i = 0; i < statusCombo.getItemCount(); i++) {
            if (statusCombo.getItemAt(i).equals(status)) {
                statusCombo.setSelectedIndex(i);
                break;
            }
        }
    }
}
