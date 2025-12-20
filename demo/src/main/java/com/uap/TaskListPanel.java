package com.uap;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Panel for displaying tasks in a table with sorting and searching features.
 */
public class TaskListPanel extends JPanel {
    private TaskManager taskManager;
    private MainDashboard parent;
    private JTable taskTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> statusFilter;
    private JComboBox<String> priorityFilter;
    private TableRowSorter<DefaultTableModel> sorter;

    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SECONDARY_COLOR = new Color(52, 73, 94);
    private static final Color ACCENT_COLOR = new Color(46, 204, 113);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Color CARD_COLOR = Color.WHITE;

    public TaskListPanel(TaskManager taskManager, MainDashboard parent) {
        this.taskManager = taskManager;
        this.parent = parent;

        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        initComponents();
    }

    private void initComponents() {
        // Title and filter panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BACKGROUND_COLOR);

        // Title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(BACKGROUND_COLOR);
        JLabel titleLabel = new JLabel(" Task List");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(SECONDARY_COLOR);
        titlePanel.add(titleLabel);

        topPanel.add(titlePanel, BorderLayout.NORTH);

        // Filter and search panel
        JPanel filterPanel = createFilterPanel();
        topPanel.add(filterPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // Table panel
        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        refreshTable();
    }

    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Search field
        JLabel searchLabel = new JLabel(" Search:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(searchLabel);

        searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterTable();
            }
        });
        panel.add(searchField);

        // Status filter
        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(statusLabel);

        statusFilter = new JComboBox<>(new String[] { "ALL", "PENDING", "IN_PROGRESS", "COMPLETED" });
        statusFilter.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        statusFilter.addActionListener(e -> filterTable());
        panel.add(statusFilter);

        // Priority filter
        JLabel priorityLabel = new JLabel("Priority:");
        priorityLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(priorityLabel);

        priorityFilter = new JComboBox<>(new String[] { "ALL", "HIGH", "MEDIUM", "LOW" });
        priorityFilter.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        priorityFilter.addActionListener(e -> filterTable());
        panel.add(priorityFilter);

        // Clear filters button
        JButton clearButton = new JButton("Clear Filters");
        clearButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        clearButton.setBackground(SECONDARY_COLOR);
        clearButton.setForeground(Color.WHITE);
        clearButton.setFocusPainted(false);
        clearButton.setBorderPainted(false);
        clearButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        clearButton.addActionListener(e -> clearFilters());
        panel.add(clearButton);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        // Create table model
        String[] columnNames = { "ID", "Title", "Description", "Priority", "Status", "Created Date" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };

        taskTable = new JTable(tableModel);
        taskTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        taskTable.setRowHeight(30);
        taskTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        taskTable.getTableHeader().setBackground(PRIMARY_COLOR);
        taskTable.getTableHeader().setForeground(Color.WHITE);
        taskTable.setSelectionBackground(new Color(52, 152, 219));
        taskTable.setSelectionForeground(Color.WHITE);
        taskTable.setGridColor(new Color(189, 195, 199));

        // Set column widths
        taskTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        taskTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        taskTable.getColumnModel().getColumn(2).setPreferredWidth(300);
        taskTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        taskTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        taskTable.getColumnModel().getColumn(5).setPreferredWidth(120);

        // Add row sorter for sorting functionality
        sorter = new TableRowSorter<>(tableModel);
        taskTable.setRowSorter(sorter);

        // Add double-click listener to edit task
        taskTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = taskTable.getSelectedRow();
                    if (row != -1) {
                        editSelectedTask();
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(taskTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panel.setBackground(BACKGROUND_COLOR);

        JButton btnAdd = createButton(" Add Task", ACCENT_COLOR);
        btnAdd.addActionListener(e -> parent.showPanel("TASK_INPUT"));

        JButton btnEdit = createButton(" Edit", PRIMARY_COLOR);
        btnEdit.addActionListener(e -> editSelectedTask());

        JButton btnDelete = createButton(" Delete", DANGER_COLOR);
        btnDelete.addActionListener(e -> deleteSelectedTask());

        JButton btnRefresh = createButton(" Refresh", SECONDARY_COLOR);
        btnRefresh.addActionListener(e -> refreshTable());

        panel.add(btnAdd);
        panel.add(btnEdit);
        panel.add(btnDelete);
        panel.add(btnRefresh);

        return panel;
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(140, 40));
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

    public void refreshTable() {
        try {
            // Clear existing rows
            tableModel.setRowCount(0);

            // Get all tasks
            List<Task> tasks = taskManager.getAllTasks();

            // Add tasks to table
            for (Task task : tasks) {
                Object[] row = {
                        task.getId(),
                        task.getTitle(),
                        truncateText(task.getDescription(), 50),
                        task.getPriority(),
                        task.getStatus(),
                        task.getFormattedCreatedDate()
                };
                tableModel.addRow(row);
            }

            // Apply current filters
            filterTable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading tasks: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filterTable() {
        try {
            String searchText = searchField.getText().toLowerCase().trim();
            String selectedStatus = (String) statusFilter.getSelectedItem();
            String selectedPriority = (String) priorityFilter.getSelectedItem();

            List<Task> tasks = taskManager.getAllTasks();

            // Apply filters
            if (!"ALL".equals(selectedStatus)) {
                tasks = taskManager.getTasksByStatus(selectedStatus);
            }

            if (!"ALL".equals(selectedPriority)) {
                String finalSelectedPriority = selectedPriority;
                tasks = tasks.stream()
                        .filter(t -> t.getPriority().equals(finalSelectedPriority))
                        .collect(java.util.stream.Collectors.toList());
            }

            if (!searchText.isEmpty()) {
                String finalSearchText = searchText;
                tasks = tasks.stream()
                        .filter(t -> t.getTitle().toLowerCase().contains(finalSearchText) ||
                                t.getDescription().toLowerCase().contains(finalSearchText))
                        .collect(java.util.stream.Collectors.toList());
            }

            // Update table
            tableModel.setRowCount(0);
            for (Task task : tasks) {
                Object[] row = {
                        task.getId(),
                        task.getTitle(),
                        truncateText(task.getDescription(), 50),
                        task.getPriority(),
                        task.getStatus(),
                        task.getFormattedCreatedDate()
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error filtering tasks: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFilters() {
        searchField.setText("");
        statusFilter.setSelectedIndex(0);
        priorityFilter.setSelectedIndex(0);
        refreshTable();
    }

    private void editSelectedTask() {
        int row = taskTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a task to edit",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int taskId = (int) tableModel.getValueAt(taskTable.convertRowIndexToModel(row), 0);
            Task task = taskManager.getTaskById(taskId);

            if (task != null) {
                parent.showEditPanel(task);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Task not found",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading task: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedTask() {
        int row = taskTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a task to delete",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int taskId = (int) tableModel.getValueAt(taskTable.convertRowIndexToModel(row), 0);
            String taskTitle = (String) tableModel.getValueAt(taskTable.convertRowIndexToModel(row), 1);

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete:\n" + taskTitle + "?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = taskManager.deleteTask(taskId);

                if (success) {
                    JOptionPane.showMessageDialog(this,
                            "Task deleted successfully",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Failed to delete task",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error deleting task: " + e.getMessage(),
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
}
