package com.uap;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class TaskListPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JTextField searchField;
    private JComboBox<String> statusFilter;
    private DataManager dataManager;

    public TaskListPanel(DataManager dataManager) {
        this.dataManager = dataManager;
        
        setLayout(new BorderLayout());
        setBackground(UIColors.BG_COLOR);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header
        JLabel title = new JLabel("Task List");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(UIColors.SIDEBAR_COLOR);
        add(title, BorderLayout.NORTH);

        // Filter Bar
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(UIColors.BG_COLOR);
        
        searchField = new JTextField(20);
        statusFilter = new JComboBox<>(new String[]{"ALL", "Pending", "Completed"});
        JButton btnClear = new JButton("Clear Filters");
        btnClear.setBackground(UIColors.SIDEBAR_COLOR);
        btnClear.setForeground(Color.WHITE);

        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(searchField);
        filterPanel.add(new JLabel("Status:"));
        filterPanel.add(statusFilter);
        filterPanel.add(btnClear);

        // Table
        String[] cols = {"ID", "Title", "Description", "Priority", "Status", "Created", "Due Date"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(30);
        table.getTableHeader().setBackground(new Color(236, 240, 241));
        table.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(table);
        
        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(UIColors.BG_COLOR);
        JButton btnEdit = UIUtils.createStyledButton("Edit", UIColors.BTN_BLUE);
        JButton btnDelete = UIUtils.createStyledButton("Delete", UIColors.BTN_RED);
        JButton btnComplete = UIUtils.createStyledButton("Mark Completed", UIColors.BTN_GREEN);

        btnPanel.add(btnComplete);
        btnPanel.add(btnEdit);
        btnPanel.add(btnDelete);

        // Grouping Table and Filters
        JPanel centerContainer = new JPanel(new BorderLayout(0, 10));
        centerContainer.setBackground(UIColors.BG_COLOR);
        centerContainer.add(filterPanel, BorderLayout.NORTH);
        centerContainer.add(scrollPane, BorderLayout.CENTER);
        centerContainer.add(btnPanel, BorderLayout.SOUTH);
        
        add(centerContainer, BorderLayout.CENTER);

        // Events
        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) { filterTable(); }
        });
        statusFilter.addActionListener(e -> filterTable());
        btnClear.addActionListener(e -> {
            searchField.setText("");
            statusFilter.setSelectedIndex(0);
            filterTable();
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                String id = (String) table.getValueAt(row, 0);
                dataManager.deleteTask(id);
                loadTableData();
                JOptionPane.showMessageDialog(this, "Task Deleted!");
            }
        });
        
        btnComplete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if(row != -1) {
                String id = (String) table.getValueAt(row, 0);
                for(Task t : dataManager.getTasks()) {
                    if(t.getId().equals(id)) {
                        t.setStatus("Completed");
                        dataManager.updateTask(id, t);
                        break;
                    }
                }
                loadTableData();
            }
        });
        
        btnEdit.addActionListener(e -> {
             int row = table.getSelectedRow();
             if (row != -1) {
                 String id = (String) table.getValueAt(row, 0);
                 JOptionPane.showMessageDialog(this, "Edit feature: Go to Input tab, re-enter details for ID " + id + " (Simplified logic)");
             }
        });
    }

    private void filterTable() {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        
        String text = searchField.getText();
        String status = (String) statusFilter.getSelectedItem();
        
        List<RowFilter<Object, Object>> filters = new ArrayList<>();
        if (text.length() > 0) filters.add(RowFilter.regexFilter("(?i)" + text));
        if (!status.equals("ALL")) filters.add(RowFilter.regexFilter(status, 4));
        
        sorter.setRowFilter(RowFilter.andFilter(filters));
    }

    public void loadTableData() {
        model.setRowCount(0);
        for (Task t : dataManager.getTasks()) {
            model.addRow(new Object[]{t.getId(), t.getTitle(), t.getDescription(), t.getPriority(), t.getStatus(), t.getCreatedDate(), t.getDueDate()});
        }
    }
}
