package com.uap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;

public class TaskInputPanel extends JPanel {
    private TaskManager taskManager;
    private MainDashboard parent;

    private JTextField titleField;
    private JTextArea descriptionArea;
    private JComboBox<String> priorityCombo;
    private JComboBox<String> statusCombo;
    private JFormattedTextField dueDateField;
    private JButton datePickerButton;
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
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(BACKGROUND_COLOR);
        formTitleLabel = new JLabel(" Add New Task");
        formTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        formTitleLabel.setForeground(SECONDARY_COLOR);
        titlePanel.add(formTitleLabel);

        add(titlePanel, BorderLayout.NORTH);

        JPanel formPanel = createFormPanel();
        add(formPanel, BorderLayout.CENTER);

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
        priorityCombo = new JComboBox<>(new String[] { "High", "Medium", "Low" });
        priorityCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        priorityCombo.setPreferredSize(new Dimension(0, 35));
        priorityCombo.setSelectedIndex(1);
        mainPanel.add(priorityCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        JLabel statusLabel = createLabel("Status: *");
        mainPanel.add(statusLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        statusCombo = new JComboBox<>(new String[] { "Pending", "In Progress", "Completed" });
        statusCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusCombo.setPreferredSize(new Dimension(0, 35));
        mainPanel.add(statusCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0;
        JLabel dueDateLabel = createLabel("Due Date:");
        mainPanel.add(dueDateLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JPanel dueDatePanel = new JPanel(new BorderLayout(5, 0));
        dueDatePanel.setBackground(CARD_COLOR);

        dueDateField = new JFormattedTextField();
        dueDateField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dueDateField.setPreferredSize(new Dimension(0, 35));
        dueDateField.setToolTipText("Format: YYYY-MM-DD");
        dueDateField.setForeground(Color.BLACK);
        dueDateField.setText("yyyy-mm-dd");

        dueDateField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (dueDateField.getText().equals("yyyy-mm-dd")) {
                    dueDateField.setText("");
                    dueDateField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                String text = dueDateField.getText().trim();
                if (text.isEmpty() || text.equals("yyyy-mm-dd")) {
                    dueDateField.setText("yyyy-mm-dd");
                    dueDateField.setForeground(Color.BLACK);
                } else {
                    dueDateField.setForeground(Color.BLACK);
                }
            }
        });

        dueDatePanel.add(dueDateField, BorderLayout.CENTER);

        datePickerButton = new JButton("...");
        datePickerButton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        datePickerButton.setPreferredSize(new Dimension(45, 35));
        datePickerButton.setFocusPainted(false);
        datePickerButton.setBackground(PRIMARY_COLOR);
        datePickerButton.setForeground(Color.BLACK);
        datePickerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        datePickerButton.setToolTipText("Choose date from calendar");
        datePickerButton.addActionListener(e -> showDatePicker());
        dueDatePanel.add(datePickerButton, BorderLayout.EAST);

        mainPanel.add(dueDatePanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
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

            LocalDate dueDate = null;
            String dueDateText = dueDateField.getText().trim();
            if (!dueDateText.isEmpty() && !dueDateText.equals("yyyy-mm-dd")) {
                try {
                    dueDate = LocalDate.parse(dueDateText, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                } catch (DateTimeParseException e) {
                    JOptionPane.showMessageDialog(this,
                            "Invalid date format! Please use YYYY-MM-DD format.",
                            "Validation Error",
                            JOptionPane.ERROR_MESSAGE);
                    dueDateField.requestFocus();
                    return;
                }
            }

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
                Task newTask = taskManager.addTask(title, description,
                        UiFormat.toBackend(priority),
                        UiFormat.toBackend(status),
                        dueDate);
                success = (newTask != null);
            } else {
                success = taskManager.updateTask(editingTask.getId(),
                        title, description,
                        UiFormat.toBackend(priority),
                        UiFormat.toBackend(status),
                        dueDate);
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
        priorityCombo.setSelectedIndex(1);
        statusCombo.setSelectedIndex(0);
        dueDateField.setText("YYYY-MM-DD");
        dueDateField.setForeground(Color.BLACK);
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

        String priority = task.getPriority();
        String priorityUI = UiFormat.toUi(priority);
        for (int i = 0; i < priorityCombo.getItemCount(); i++) {
            if (priorityCombo.getItemAt(i).equals(priorityUI)) {
                priorityCombo.setSelectedIndex(i);
                break;
            }
        }

        String status = task.getStatus();
        String statusUI = UiFormat.toUi(status);
        for (int i = 0; i < statusCombo.getItemCount(); i++) {
            if (statusCombo.getItemAt(i).equals(statusUI)) {
                statusCombo.setSelectedIndex(i);
                break;
            }
        }

        if (task.getDueDate() != null) {
            dueDateField.setText(task.getFormattedDueDate());
            dueDateField.setForeground(Color.BLACK);
        } else {
            dueDateField.setText("yyyy-mm-dd");
            dueDateField.setForeground(Color.GRAY);
        }
    }

    private void showDatePicker() {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Select Due Date", true);
        dialog.setLayout(new BorderLayout());

        JPanel calendarPanel = new JPanel(new GridLayout(7, 7, 5, 5));
        calendarPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        calendarPanel.setBackground(Color.WHITE);

        Calendar calendar = Calendar.getInstance();
        String currentDateText = dueDateField.getText().trim();
        if (!currentDateText.isEmpty() && !currentDateText.equals("yyyy-mm-dd")) {
            try {
                LocalDate selectedDate = LocalDate.parse(currentDateText, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                calendar.set(selectedDate.getYear(), selectedDate.getMonthValue() - 1, selectedDate.getDayOfMonth());
            } catch (Exception e) {
            }
        }

        final Calendar cal = (Calendar) calendar.clone();

        JPanel monthPanel = new JPanel(new FlowLayout());
        monthPanel.setBackground(PRIMARY_COLOR);

        JButton prevButton = new JButton("◀");
        prevButton.setFocusPainted(false);
        prevButton.setBackground(PRIMARY_COLOR);
        prevButton.setForeground(Color.WHITE);
        prevButton.setBorderPainted(false);

        JLabel monthLabel = new JLabel();
        monthLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        monthLabel.setForeground(Color.BLACK);

        JButton nextButton = new JButton("▶");
        nextButton.setFocusPainted(false);
        nextButton.setBackground(PRIMARY_COLOR);
        nextButton.setForeground(Color.WHITE);
        nextButton.setBorderPainted(false);

        monthPanel.add(prevButton);
        monthPanel.add(monthLabel);
        monthPanel.add(nextButton);

        Runnable updateCalendar = new Runnable() {
            public void run() {
                calendarPanel.removeAll();

                String[] months = { "January", "February", "March", "April", "May", "June",
                        "July", "August", "September", "October", "November", "December" };
                monthLabel.setText(months[cal.get(Calendar.MONTH)] + " " + cal.get(Calendar.YEAR));

                String[] days = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
                for (String day : days) {
                    JLabel label = new JLabel(day, SwingConstants.CENTER);
                    label.setFont(new Font("Segoe UI", Font.BOLD, 12));
                    label.setForeground(PRIMARY_COLOR);
                    calendarPanel.add(label);
                }

                Calendar tempCal = (Calendar) cal.clone();
                tempCal.set(Calendar.DAY_OF_MONTH, 1);
                int firstDayOfWeek = tempCal.get(Calendar.DAY_OF_WEEK);
                int daysInMonth = tempCal.getActualMaximum(Calendar.DAY_OF_MONTH);

                for (int i = 1; i < firstDayOfWeek; i++) {
                    calendarPanel.add(new JLabel(""));
                }

                LocalDate today = LocalDate.now();
                for (int day = 1; day <= daysInMonth; day++) {
                    final int dayNum = day;
                    JButton dayButton = new JButton(String.valueOf(day));
                    dayButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    dayButton.setFocusPainted(false);
                    dayButton.setBackground(Color.WHITE);
                    dayButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

                    LocalDate buttonDate = LocalDate.of(cal.get(Calendar.YEAR),
                            cal.get(Calendar.MONTH) + 1, day);
                    if (buttonDate.equals(today)) {
                        dayButton.setBackground(new Color(230, 240, 255));
                        dayButton.setForeground(PRIMARY_COLOR);
                        dayButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
                    }

                    dayButton.addActionListener(e -> {
                        LocalDate selectedDate = LocalDate.of(cal.get(Calendar.YEAR),
                                cal.get(Calendar.MONTH) + 1,
                                dayNum);
                        dueDateField.setText(selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                        dueDateField.setForeground(Color.BLACK);
                        dialog.dispose();
                    });

                    calendarPanel.add(dayButton);
                }

                calendarPanel.revalidate();
                calendarPanel.repaint();
            }
        };

        prevButton.addActionListener(e -> {
            cal.add(Calendar.MONTH, -1);
            updateCalendar.run();
        });

        nextButton.addActionListener(e -> {
            cal.add(Calendar.MONTH, 1);
            updateCalendar.run();
        });

        updateCalendar.run();

        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setBackground(Color.WHITE);

        JButton todayButton = new JButton("Today");
        todayButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        todayButton.setBackground(ACCENT_COLOR);
        todayButton.setForeground(Color.WHITE);
        todayButton.setFocusPainted(false);
        todayButton.setBorderPainted(false);
        todayButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        todayButton.addActionListener(e -> {
            LocalDate today = LocalDate.now();
            dueDateField.setText(today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            dueDateField.setForeground(Color.BLACK);
            dialog.dispose();
        });

        JButton clearButton = new JButton("Clear");
        clearButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        clearButton.setBackground(SECONDARY_COLOR);
        clearButton.setForeground(Color.WHITE);
        clearButton.setFocusPainted(false);
        clearButton.setBorderPainted(false);
        clearButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        clearButton.addActionListener(e -> {
            dueDateField.setText("yyyy-mm-dd");
            dueDateField.setForeground(Color.GRAY);
            dialog.dispose();
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cancelButton.setBackground(new Color(127, 140, 141));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setBorderPainted(false);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.addActionListener(e -> dialog.dispose());

        bottomPanel.add(todayButton);
        bottomPanel.add(clearButton);
        bottomPanel.add(cancelButton);

        dialog.add(monthPanel, BorderLayout.NORTH);
        dialog.add(calendarPanel, BorderLayout.CENTER);
        dialog.add(bottomPanel, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

}
