package com.uap;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class AddTaskPanel extends JPanel {
    private JTextField txtTitle, txtDue;
    private JTextArea txtDesc;
    private JComboBox<String> cbPriority, cbStatus;
    private DataManager dataManager;

    public AddTaskPanel(DataManager dataManager) {
        this.dataManager = dataManager;
        
        setLayout(new BorderLayout());
        setBackground(UIColors.BG_COLOR);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Add New Task");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(UIColors.SIDEBAR_COLOR);
        add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UIColors.CARD_BG);
        formPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Form Fields
        addFormRow(formPanel, gbc, 0, "Task Title:*", txtTitle = new JTextField(20));
        
        txtDesc = new JTextArea(4, 20);
        txtDesc.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        addFormRow(formPanel, gbc, 1, "Description:", new JScrollPane(txtDesc));

        cbPriority = new JComboBox<>(new String[]{"High", "Medium", "Low"});
        addFormRow(formPanel, gbc, 2, "Priority:*", cbPriority);

        cbStatus = new JComboBox<>(new String[]{"Pending", "Completed"});
        addFormRow(formPanel, gbc, 3, "Status:*", cbStatus);

        addFormRow(formPanel, gbc, 4, "Due Date (YYYY-MM-DD):", txtDue = new JTextField());

        add(formPanel, BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(UIColors.BG_COLOR);
        JButton btnSave = UIUtils.createStyledButton("Save Task", UIColors.BTN_GREEN);
        JButton btnClear = UIUtils.createStyledButton("Clear Form", UIColors.BTN_BLUE);

        btnPanel.add(btnSave);
        btnPanel.add(btnClear);
        add(btnPanel, BorderLayout.SOUTH);

        // Actions
        btnClear.addActionListener(e -> clearForm());
        btnSave.addActionListener(e -> saveTask());
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String label, Component comp) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.1;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.weightx = 0.9;
        panel.add(comp, gbc);
    }

    private void saveTask() {
        try {
            if(txtTitle.getText().isEmpty()) throw new Exception("Title is required!");
            
            String id = UUID.randomUUID().toString().substring(0, 4);
            LocalDate due = LocalDate.parse(txtDue.getText());
            
            Task newTask = new Task(
                id, 
                txtTitle.getText(), 
                txtDesc.getText(), 
                (String)cbPriority.getSelectedItem(), 
                (String)cbStatus.getSelectedItem(), 
                LocalDate.now(), 
                due
            );

            dataManager.addTask(newTask);
            JOptionPane.showMessageDialog(this, "Task Saved Successfully!");
            clearForm();
            
        } catch (DateTimeParseException dtpe) {
            JOptionPane.showMessageDialog(this, "Invalid Date Format. Use YYYY-MM-DD", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        txtTitle.setText("");
        txtDesc.setText("");
        txtDue.setText("");
        cbPriority.setSelectedIndex(0);
    }
}
