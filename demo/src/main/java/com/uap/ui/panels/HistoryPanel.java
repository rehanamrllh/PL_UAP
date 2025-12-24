package com.uap.ui.panels;

import com.uap.data.DataManager;
import com.uap.model.Task;
import com.uap.ui.UIColors;
import com.uap.ui.UIUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class HistoryPanel extends JPanel {
    private DefaultTableModel model;
    private DataManager dataManager;

    public HistoryPanel(DataManager dataManager) {
        this.dataManager = dataManager;

        setLayout(new BorderLayout());
        setBackground(UIColors.BG_COLOR);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Task History & Statistics");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(UIColors.SIDEBAR_COLOR);
        add(title, BorderLayout.NORTH);

        // Only showing Completed Tasks Table for history
        String[] cols = { "ID", "Title", "Description", "Priority", "Due Date" };
        model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.getTableHeader().setBackground(new Color(236, 240, 241));

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Completed Tasks Archive"));
        tablePanel.setBackground(UIColors.CARD_BG);
        tablePanel.add(new JScrollPane(table));

        add(tablePanel, BorderLayout.CENTER);

        JButton refreshBtn = UIUtils.createStyledButton("Refresh History", UIColors.BTN_BLUE);
        refreshBtn.addActionListener(e -> loadHistory());
        add(refreshBtn, BorderLayout.SOUTH);
    }

    public void loadHistory() {
        model.setRowCount(0);
        for (Task t : dataManager.getTasks()) {
            if (t.getStatus().equals("Completed")) {
                model.addRow(
                        new Object[] { t.getId(), t.getTitle(), t.getDescription(), t.getPriority(), t.getDueDate() });
            }
        }
    }
}
