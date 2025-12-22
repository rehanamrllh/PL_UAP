package com.uap;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class HistoryPanel extends JPanel {
    private TaskManager taskManager;
    private JTable historyTable;
    private DefaultTableModel tableModel;

    private JLabel totalCompletedLabel;
    private JLabel completionRateLabel;
    private JLabel highPriorityLabel;
    private JLabel avgTasksLabel;

    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SECONDARY_COLOR = new Color(52, 73, 94);
    private static final Color ACCENT_COLOR = new Color(46, 204, 113);
    private static final Color WARNING_COLOR = new Color(243, 156, 18);
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Color CARD_COLOR = Color.WHITE;

    public HistoryPanel(TaskManager taskManager) {
        this.taskManager = taskManager;

        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        initComponents();
    }

    private void initComponents() {
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(BACKGROUND_COLOR);
        JLabel titleLabel = new JLabel("Task History & Statistics");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(SECONDARY_COLOR);
        titlePanel.add(titleLabel);

        add(titlePanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
        contentPanel.setBackground(BACKGROUND_COLOR);

        JPanel statsPanel = createStatisticsPanel();
        contentPanel.add(statsPanel, BorderLayout.NORTH);

        JPanel tablePanel = createHistoryTablePanel();
        contentPanel.add(tablePanel, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);

        refreshHistory();
    }

    private JPanel createStatisticsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 15, 0));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JPanel completedCard = createStatCard("Total Completed", "0", "", ACCENT_COLOR);
        totalCompletedLabel = (JLabel) ((JPanel) completedCard.getComponent(1)).getComponent(0);

        JPanel rateCard = createStatCard("Completion Rate", "0%", "", PRIMARY_COLOR);
        completionRateLabel = (JLabel) ((JPanel) rateCard.getComponent(1)).getComponent(0);

        JPanel highPriorityCard = createStatCard("High Priority Done", "0", "", WARNING_COLOR);
        highPriorityLabel = (JLabel) ((JPanel) highPriorityCard.getComponent(1)).getComponent(0);

        JPanel avgCard = createStatCard("Active Tasks", "0", "", SECONDARY_COLOR);
        avgTasksLabel = (JLabel) ((JPanel) avgCard.getComponent(1)).getComponent(0);

        panel.add(completedCard);
        panel.add(rateCard);
        panel.add(highPriorityCard);
        panel.add(avgCard);

        return panel;
    }

    private JPanel createStatCard(String title, String value, String icon, Color color) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        JPanel iconPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        iconPanel.setBackground(CARD_COLOR);
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        iconPanel.add(iconLabel);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(CARD_COLOR);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(color);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLabel.setForeground(new Color(127, 140, 141));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(valueLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textPanel.add(titleLabel);

        card.add(iconPanel, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createHistoryTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        JLabel sectionTitle = new JLabel("Completed Tasks");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionTitle.setForeground(SECONDARY_COLOR);
        sectionTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panel.add(sectionTitle, BorderLayout.NORTH);

        String[] columnNames = { "ID", "Title", "Description", "Priority", "Created Date", "Due Date" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        historyTable = new JTable(tableModel);
        historyTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        historyTable.setRowHeight(30);
        historyTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        historyTable.getTableHeader().setBackground(PRIMARY_COLOR);
        historyTable.getTableHeader().setForeground(Color.BLACK);
        historyTable.setSelectionBackground(new Color(46, 204, 113, 100));
        historyTable.setSelectionForeground(Color.WHITE);
        historyTable.setGridColor(new Color(189, 195, 199));

        historyTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        historyTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        historyTable.getColumnModel().getColumn(2).setPreferredWidth(250);
        historyTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        historyTable.getColumnModel().getColumn(4).setPreferredWidth(120);
        historyTable.getColumnModel().getColumn(5).setPreferredWidth(120);

        JScrollPane scrollPane = new JScrollPane(historyTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(CARD_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton refreshButton = new JButton(" Refresh History");
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setBackground(PRIMARY_COLOR);
        refreshButton.setFocusPainted(false);
        refreshButton.setBorderPainted(false);
        refreshButton.setPreferredSize(new Dimension(180, 40));
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.addActionListener(e -> refreshHistory());

        refreshButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                refreshButton.setBackground(PRIMARY_COLOR.darker());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                refreshButton.setBackground(PRIMARY_COLOR);
            }
        });

        buttonPanel.add(refreshButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    public void refreshHistory() {
        try {
            TaskManager.TaskStatistics stats = taskManager.getStatistics();
            totalCompletedLabel.setText(String.valueOf(stats.completed));
            completionRateLabel.setText(String.format("%.1f%%", stats.getCompletionRate()));

            long completedHighPriority = taskManager.getCompletedTasks().stream()
                    .filter(t -> "HIGH".equals(t.getPriority()))
                    .count();
            highPriorityLabel.setText(String.valueOf(completedHighPriority));

            avgTasksLabel.setText(String.valueOf(stats.pending + stats.inProgress));

            tableModel.setRowCount(0);
            List<Task> completedTasks = taskManager.getCompletedTasks();

            for (Task task : completedTasks) {
                Object[] row = {
                        task.getId(),
                        task.getTitle(),
                        truncateText(task.getDescription(), 40),
                        convertToUIFormat(task.getPriority()),
                        task.getFormattedCreatedDate(),
                        task.getFormattedDueDate()
                };
                tableModel.addRow(row);
            }

            if (completedTasks.isEmpty()) {
                Object[] row = {
                        "",
                        "No completed tasks yet",
                        "Complete some tasks to see them here!",
                        "",
                        "",
                        ""
                };
                tableModel.addRow(row);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading history: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private String truncateText(String text, int maxLength) {
        if (text == null || text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength) + "...";
    }

    // Helper method to convert backend format to UI format
    private String convertToUIFormat(String backendValue) {
        if (backendValue == null)
            return null;
        switch (backendValue) {
            case "HIGH":
                return "High";
            case "MEDIUM":
                return "Medium";
            case "LOW":
                return "Low";
            case "PENDING":
                return "Pending";
            case "IN_PROGRESS":
                return "In Progress";
            case "COMPLETED":
                return "Completed";
            default: {
                // Convert SOME_TEXT to Some Text
                String[] words = backendValue.toLowerCase().split("_");
                StringBuilder result = new StringBuilder();
                for (String word : words) {
                    if (result.length() > 0)
                        result.append(" ");
                    result.append(Character.toUpperCase(word.charAt(0)));
                    if (word.length() > 1)
                        result.append(word.substring(1));
                }
                return result.toString();
            }
        }
    }
}
