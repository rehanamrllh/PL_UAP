package com.uap.ui.panels;

import com.uap.data.DataManager;
import com.uap.model.Task;
import com.uap.ui.LocalDatePickerField;
import com.uap.ui.UIColors;
import com.uap.ui.UIUtils;

import java.util.function.Consumer;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AddTaskPanel extends JPanel {
    private JTextField txtTitle;
    private LocalDatePickerField dueDatePicker;
    private JTextArea txtDesc;
    private JComboBox<String> cbPriority, cbStatus;
    private DataManager dataManager;
    private final Runnable refreshAll;
    private final Consumer<String> showCard;

    private JLabel headerLabel;
    private JButton btnSave;

    private String editingId;
    private LocalDate editingCreatedDate;

    public AddTaskPanel(DataManager dataManager, Runnable refreshAll, Consumer<String> showCard) {
        this.dataManager = dataManager;
        this.refreshAll = refreshAll;
        this.showCard = showCard;

        setLayout(new BorderLayout());
        setBackground(UIColors.BG_COLOR);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        headerLabel = new JLabel("Add New Task");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(UIColors.SIDEBAR_COLOR);
        add(headerLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UIColors.CARD_BG);
        formPanel.setBorder(new EmptyBorder(20, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addFormRow(formPanel, gbc, 0, "Task Title:*", txtTitle = new JTextField(20));

        txtDesc = new JTextArea(4, 20);
        txtDesc.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        addFormRow(formPanel, gbc, 1, "Description:", new JScrollPane(txtDesc));

        cbPriority = new JComboBox<>(new String[] { "High", "Medium", "Low" });
        addFormRow(formPanel, gbc, 2, "Priority:*", cbPriority);

        cbStatus = new JComboBox<>(new String[] { "Pending", "Completed" });
        addFormRow(formPanel, gbc, 3, "Status:*", cbStatus);

        dueDatePicker = new LocalDatePickerField("YYYY-MM-DD");
        addFormRow(formPanel, gbc, 4, "Due Date (YYYY-MM-DD):", dueDatePicker);

        add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(UIColors.BG_COLOR);
        btnSave = UIUtils.createStyledButton("Save Task", UIColors.BTN_GREEN);
        JButton btnClear = UIUtils.createStyledButton("Clear Form", UIColors.BTN_BLUE);

        btnPanel.add(btnSave);
        btnPanel.add(btnClear);
        add(btnPanel, BorderLayout.SOUTH);

        btnClear.addActionListener(e -> resetToAddMode());
        btnSave.addActionListener(e -> saveTask());
    }

    public void startEdit(Task task) {
        if (task == null) {
            return;
        }

        editingId = task.getId();
        editingCreatedDate = task.getCreatedDate();

        headerLabel.setText("Edit Task");
        btnSave.setText("Update Task");

        txtTitle.setText(task.getTitle());
        txtDesc.setText(task.getDescription());
        cbPriority.setSelectedItem(task.getPriority());
        cbStatus.setSelectedItem(task.getStatus());
        dueDatePicker.setDate(task.getDueDate());
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String label, Component comp) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.1;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.9;
        panel.add(comp, gbc);
    }

    private void saveTask() {
        try {
            if (txtTitle.getText().isEmpty())
                throw new Exception("Title is required!");

            boolean isEdit = editingId != null;
            String id = isEdit ? editingId : dataManager.generateUniqueId();
            LocalDate created = isEdit && editingCreatedDate != null ? editingCreatedDate : LocalDate.now();

            LocalDate due = dueDatePicker.getDate();
            if (due == null) {
                throw new DateTimeParseException("Empty date", "", 0);
            }

            Task newTask = new Task(
                    id,
                    txtTitle.getText(),
                    txtDesc.getText(),
                    (String) cbPriority.getSelectedItem(),
                    (String) cbStatus.getSelectedItem(),
                    created,
                    due);

            if (isEdit) {
                dataManager.updateTask(id, newTask);
                JOptionPane.showMessageDialog(this, "Task Updated Successfully!");
            } else {
                dataManager.addTask(newTask);
                JOptionPane.showMessageDialog(this, "Task Saved Successfully!");
            }

            resetToAddMode();
            refreshAll.run();
            showCard.accept("LIST");

        } catch (DateTimeParseException dtpe) {
            JOptionPane.showMessageDialog(this, "Invalid Date Format. Use YYYY-MM-DD", "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetToAddMode() {
        txtTitle.setText("");
        txtDesc.setText("");
        dueDatePicker.clear();
        cbPriority.setSelectedIndex(0);
        cbStatus.setSelectedIndex(0);

        editingId = null;
        editingCreatedDate = null;
        headerLabel.setText("Add New Task");
        btnSave.setText("Save Task");
    }
}
