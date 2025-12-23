package com.uap;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.UUID;
import java.util.List;

public class ToDoApp extends JFrame {

    // Colors based on the image
    private final Color SIDEBAR_COLOR = new Color(44, 62, 80);
    private final Color HEADER_COLOR = new Color(52, 152, 219);
    private final Color BG_COLOR = new Color(236, 240, 241);
    private final Color CARD_BG = Color.WHITE;
    private final Color BTN_GREEN = new Color(46, 204, 113);
    private final Color BTN_BLUE = new Color(41, 128, 185);
    private final Color BTN_RED = new Color(231, 76, 60);

    private CardLayout cardLayout;
    private JPanel mainContentPanel;
    private DataManager dataManager;


    // Panels
    private DashboardPanel dashboardPanel;
    private TaskListPanel taskListPanel;
    private AddTaskPanel addTaskPanel;
    private HistoryPanel historyPanel;

    public ToDoApp() {
        dataManager = new DataManager();

        setTitle("To-Do List Manager");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- SIDEBAR ---
        JPanel sidebar = new JPanel();
        sidebar.setBackground(SIDEBAR_COLOR);
        sidebar.setPreferredSize(new Dimension(220, getHeight()));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        JLabel appTitle = new JLabel("To-Do Manager");
        appTitle.setForeground(Color.WHITE);
        appTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        appTitle.setBorder(new EmptyBorder(30, 20, 30, 20));
        appTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(appTitle);

        sidebar.add(createNavButton("Dashboard", "DASHBOARD"));
        sidebar.add(createNavButton("Task List", "LIST"));
        sidebar.add(createNavButton("Add Task", "ADD"));
        sidebar.add(createNavButton("History", "HISTORY"));
        sidebar.add(Box.createVerticalGlue());
        
        JLabel footer = new JLabel("© 2025 UAP Project");
        footer.setForeground(Color.GRAY);
        footer.setBorder(new EmptyBorder(0,0,20,0));
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(footer);

        add(sidebar, BorderLayout.WEST);

        // --- MAIN CONTENT (CardLayout) ---
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBackground(BG_COLOR);

        // Initialize Panels
        dashboardPanel = new DashboardPanel();
        taskListPanel = new TaskListPanel();
        addTaskPanel = new AddTaskPanel();
        historyPanel = new HistoryPanel();

        mainContentPanel.add(dashboardPanel, "DASHBOARD");
        mainContentPanel.add(taskListPanel, "LIST");
        mainContentPanel.add(addTaskPanel, "ADD");
        mainContentPanel.add(historyPanel, "HISTORY");

        add(mainContentPanel, BorderLayout.CENTER);
        
        // Initial Refresh
        refreshAll();
    }

    private JButton createNavButton(String text, String cardName) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(220, 50));
        btn.setForeground(Color.WHITE);
        btn.setBackground(SIDEBAR_COLOR);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(52, 73, 94));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(SIDEBAR_COLOR);
            }
        });

        btn.addActionListener(e -> {
            refreshAll(); // Refresh data before showing
            cardLayout.show(mainContentPanel, cardName);
        });
        return btn;
    }
    
    private void refreshAll() {
        dashboardPanel.updateStats();
        taskListPanel.loadTableData();
        historyPanel.loadHistory();
    }

    // ================== PANELS CLASSES ==================

    // 1. DASHBOARD PANEL
    class DashboardPanel extends JPanel {
        JLabel lblTotal, lblCompleted, lblPending, lblRate;

        public DashboardPanel() {
            setLayout(new BorderLayout());
            setBackground(BG_COLOR);
            setBorder(new EmptyBorder(20, 20, 20, 20));

            JLabel title = new JLabel("Dashboard Overview");
            title.setFont(new Font("Segoe UI", Font.BOLD, 24));
            title.setForeground(SIDEBAR_COLOR);
            add(title, BorderLayout.NORTH);

            JPanel cardsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
            cardsPanel.setBackground(BG_COLOR);
            cardsPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

            lblTotal = createCard(cardsPanel, "Total Tasks", "0", new Color(52, 152, 219));
            lblCompleted = createCard(cardsPanel, "Completed", "0", BTN_GREEN);
            lblPending = createCard(cardsPanel, "Pending", "0", new Color(230, 126, 34));
            lblRate = createCard(cardsPanel, "Completion Rate", "0%", new Color(155, 89, 182));

            add(cardsPanel, BorderLayout.CENTER);
        }

        private JLabel createCard(JPanel parent, String title, String value, Color color) {
            JPanel card = new JPanel(new BorderLayout());
            card.setBackground(CARD_BG);
            card.setBorder(BorderFactory.createMatteBorder(0, 0, 4, 0, color));
            
            JLabel valLabel = new JLabel(value);
            valLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
            valLabel.setForeground(color);
            valLabel.setBorder(new EmptyBorder(20, 20, 5, 20));
            
            JLabel titleLabel = new JLabel(title);
            titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            titleLabel.setForeground(Color.GRAY);
            titleLabel.setBorder(new EmptyBorder(0, 20, 20, 20));

            card.add(valLabel, BorderLayout.CENTER);
            card.add(titleLabel, BorderLayout.SOUTH);
            parent.add(card);
            return valLabel;
        }

        public void updateStats() {
            long total = dataManager.countTasks();
            long comp = dataManager.countCompleted();
            long pend = dataManager.countPending();
            double rate = total == 0 ? 0 : ((double) comp / total) * 100;

            lblTotal.setText(String.valueOf(total));
            lblCompleted.setText(String.valueOf(comp));
            lblPending.setText(String.valueOf(pend));
            lblRate.setText(String.format("%.1f%%", rate));
        }
    }

    // 2. TASK LIST PANEL
    class TaskListPanel extends JPanel {
        JTable table;
        DefaultTableModel model;
        JTextField searchField;
        JComboBox<String> statusFilter;

        public TaskListPanel() {
            setLayout(new BorderLayout());
            setBackground(BG_COLOR);
            setBorder(new EmptyBorder(20, 20, 20, 20));

            // Header
            JLabel title = new JLabel("Task List");
            title.setFont(new Font("Segoe UI", Font.BOLD, 24));
            title.setForeground(SIDEBAR_COLOR);
            add(title, BorderLayout.NORTH);

            // Filter Bar
            JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            filterPanel.setBackground(BG_COLOR);
            
            searchField = new JTextField(20);
            statusFilter = new JComboBox<>(new String[]{"ALL", "Pending", "Completed"});
            JButton btnClear = new JButton("Clear Filters");
            btnClear.setBackground(SIDEBAR_COLOR);
            btnClear.setForeground(Color.WHITE);

            filterPanel.add(new JLabel("Search:"));
            filterPanel.add(searchField);
            filterPanel.add(new JLabel("Status:"));
            filterPanel.add(statusFilter);
            filterPanel.add(btnClear);
            add(filterPanel, BorderLayout.CENTER); // Will fix layout below

            // Table
            String[] cols = {"ID", "Title", "Description", "Priority", "Status", "Created", "Due Date"};
            model = new DefaultTableModel(cols, 0) {
                public boolean isCellEditable(int row, int column) { return false; }
            };
            table = new JTable(model);
            table.setRowHeight(30);
            table.getTableHeader().setBackground(new Color(236, 240, 241));
            table.setAutoCreateRowSorter(true); // Sorting feature

            JScrollPane scrollPane = new JScrollPane(table);
            
            // Buttons
            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            btnPanel.setBackground(BG_COLOR);
            JButton btnEdit = createStyledButton("Edit", BTN_BLUE);
            JButton btnDelete = createStyledButton("Delete", BTN_RED);
            JButton btnComplete = createStyledButton("Mark Completed", BTN_GREEN);

            btnPanel.add(btnComplete);
            btnPanel.add(btnEdit);
            btnPanel.add(btnDelete);

            // Grouping Table and Filters
            JPanel centerContainer = new JPanel(new BorderLayout(0, 10));
            centerContainer.setBackground(BG_COLOR);
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
                    // Find task
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
            
            // Note: Edit button logic requires populating AddTaskPanel, simplified here for brevity
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
            
            List<RowFilter<Object, Object>> filters = new java.util.ArrayList<>();
            if (text.length() > 0) filters.add(RowFilter.regexFilter("(?i)" + text)); // Case insensitive
            if (!status.equals("ALL")) filters.add(RowFilter.regexFilter(status, 4)); // Col 4 is Status
            
            sorter.setRowFilter(RowFilter.andFilter(filters));
        }

        public void loadTableData() {
            model.setRowCount(0);
            for (Task t : dataManager.getTasks()) {
                model.addRow(new Object[]{t.getId(), t.getTitle(), t.getDescription(), t.getPriority(), t.getStatus(), t.getCreatedDate(), t.getDueDate()});
            }
        }
    }

    // 3. ADD TASK PANEL
    class AddTaskPanel extends JPanel {
        JTextField txtTitle, txtDue;
        JTextArea txtDesc;
        JComboBox<String> cbPriority, cbStatus;

        public AddTaskPanel() {
            setLayout(new BorderLayout());
            setBackground(BG_COLOR);
            setBorder(new EmptyBorder(20, 20, 20, 20));

            JLabel title = new JLabel("Add New Task");
            title.setFont(new Font("Segoe UI", Font.BOLD, 24));
            title.setForeground(SIDEBAR_COLOR);
            add(title, BorderLayout.NORTH);

            JPanel formPanel = new JPanel(new GridBagLayout());
            formPanel.setBackground(CARD_BG);
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
            btnPanel.setBackground(BG_COLOR);
            JButton btnSave = createStyledButton("Save Task", BTN_GREEN);
            JButton btnClear = createStyledButton("Clear Form", BTN_BLUE);

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
                
                String id = UUID.randomUUID().toString().substring(0, 4); // Short ID
                LocalDate due = LocalDate.parse(txtDue.getText()); // Format validation happens here
                
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

    // 4. HISTORY PANEL
    class HistoryPanel extends JPanel {
        DefaultTableModel model;
        
        public HistoryPanel() {
            setLayout(new BorderLayout());
            setBackground(BG_COLOR);
            setBorder(new EmptyBorder(20, 20, 20, 20));

            JLabel title = new JLabel("Task History & Statistics");
            title.setFont(new Font("Segoe UI", Font.BOLD, 24));
            title.setForeground(SIDEBAR_COLOR);
            add(title, BorderLayout.NORTH);
            
            // Only showing Completed Tasks Table for history
            String[] cols = {"ID", "Title", "Description", "Priority", "Completed Date"};
            model = new DefaultTableModel(cols, 0);
            JTable table = new JTable(model);
            table.setRowHeight(30);
            table.getTableHeader().setBackground(new Color(236, 240, 241));
            
            JPanel tablePanel = new JPanel(new BorderLayout());
            tablePanel.setBorder(BorderFactory.createTitledBorder("Completed Tasks Archive"));
            tablePanel.setBackground(CARD_BG);
            tablePanel.add(new JScrollPane(table));
            
            add(tablePanel, BorderLayout.CENTER);
            
            JButton refreshBtn = createStyledButton("Refresh History", BTN_BLUE);
            refreshBtn.addActionListener(e -> loadHistory());
            add(refreshBtn, BorderLayout.SOUTH);
        }
        
        public void loadHistory() {
            model.setRowCount(0);
            for (Task t : dataManager.getTasks()) {
                if(t.getStatus().equals("Completed")) {
                    model.addRow(new Object[]{t.getId(), t.getTitle(), t.getDescription(), t.getPriority(), t.getDueDate()}); // Using due date as completion for demo
                }
            }
        }
    }

    // --- UTILS ---
    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        return btn;
    }

    public static void main(String[] args) {
        // Set Look and Feel for better UI
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {}

        SwingUtilities.invokeLater(() -> new ToDoApp().setVisible(true));
    }
}