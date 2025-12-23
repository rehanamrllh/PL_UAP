package com.uap;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class ToDoApp extends JFrame {

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
        sidebar.setBackground(UIColors.SIDEBAR_COLOR);
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
        mainContentPanel.setBackground(UIColors.BG_COLOR);

        // Initialize Panels
        dashboardPanel = new DashboardPanel(dataManager);
        taskListPanel = new TaskListPanel(dataManager);
        addTaskPanel = new AddTaskPanel(dataManager);
        historyPanel = new HistoryPanel(dataManager);

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
        btn.setBackground(UIColors.SIDEBAR_COLOR);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(UIColors.HOVER_COLOR);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(UIColors.SIDEBAR_COLOR);
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

    public static void main(String[] args) {
        // Set Look and Feel for better UI
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {}

        SwingUtilities.invokeLater(() -> new ToDoApp().setVisible(true));
    }
}