package com.uap.app;

import com.uap.data.DataManager;
import com.uap.model.Task;
import com.uap.ui.UIColors;
import com.uap.ui.panels.AddTaskPanel;
import com.uap.ui.panels.DashboardPanel;
import com.uap.ui.panels.HistoryPanel;
import com.uap.ui.panels.TaskListPanel;

import java.awt.*;

import java.util.function.Consumer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ToDoApp extends JFrame {
    private final CardLayout cardLayout;
    private final JPanel mainContentPanel;
    private final DataManager dataManager;

    private final DashboardPanel dashboardPanel;
    private final TaskListPanel taskListPanel;
    private final AddTaskPanel addTaskPanel;
    private final HistoryPanel historyPanel;

    public ToDoApp() {
        dataManager = new DataManager();

        setTitle("To-Do List Manager");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

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

        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBackground(UIColors.BG_COLOR);

        Consumer<String> showCard = cardName -> cardLayout.show(mainContentPanel, cardName);
        Runnable refreshAll = this::refreshAll;

        dashboardPanel = new DashboardPanel(dataManager);
        addTaskPanel = new AddTaskPanel(dataManager, refreshAll, showCard);
        taskListPanel = new TaskListPanel(dataManager, refreshAll, (Task task) -> {
            addTaskPanel.startEdit(task);
            showCard.accept("ADD");
        });
        historyPanel = new HistoryPanel(dataManager);

        mainContentPanel.add(dashboardPanel, "DASHBOARD");
        mainContentPanel.add(taskListPanel, "LIST");
        mainContentPanel.add(addTaskPanel, "ADD");
        mainContentPanel.add(historyPanel, "HISTORY");

        sidebar.add(createNavButton("Dashboard", "DASHBOARD", showCard, refreshAll));
        sidebar.add(createNavButton("Task List", "LIST", showCard, refreshAll));
        sidebar.add(createNavButton("Add Task", "ADD", showCard, refreshAll));
        sidebar.add(createNavButton("History", "HISTORY", showCard, refreshAll));
        sidebar.add(Box.createVerticalGlue());

        JLabel footer = new JLabel("© 2025 UAP Project");
        footer.setForeground(Color.GRAY);
        footer.setBorder(new EmptyBorder(0, 0, 20, 0));
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(footer);

        add(sidebar, BorderLayout.WEST);
        add(mainContentPanel, BorderLayout.CENTER);

        refreshAll();
    }

    private JButton createNavButton(String text, String cardName, Consumer<String> showCard, Runnable refreshAll) {
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
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(UIColors.HOVER_COLOR);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(UIColors.SIDEBAR_COLOR);
            }
        });

        btn.addActionListener(e -> {
            refreshAll.run();
            showCard.accept(cardName);
        });

        return btn;
    }

    private void refreshAll() {
        dashboardPanel.updateStats();
        taskListPanel.loadTableData();
        historyPanel.loadHistory();
    }

    public static void main(String[] args) {
        launch();
    }

    public static void launch() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() -> new ToDoApp().setVisible(true));
    }

}


