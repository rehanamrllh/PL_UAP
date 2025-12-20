package com.uap;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class MainDashboard extends JFrame {
    private TaskManager taskManager;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    private TaskListPanel taskListPanel;
    private TaskInputPanel taskInputPanel;
    private HistoryPanel historyPanel;
    private DashboardHomePanel homePanel;

    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SECONDARY_COLOR = new Color(52, 73, 94);
    private static final Color ACCENT_COLOR = new Color(46, 204, 113);
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Color CARD_COLOR = Color.WHITE;

    public MainDashboard() {
        taskManager = new TaskManager();

        setTitle(" To-Do List Manager");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveDataBeforeExit();
            }
        });
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel navigationPanel = createNavigationPanel();
        add(navigationPanel, BorderLayout.WEST);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(BACKGROUND_COLOR);

        homePanel = new DashboardHomePanel(taskManager, this);
        taskListPanel = new TaskListPanel(taskManager, this);
        taskInputPanel = new TaskInputPanel(taskManager, this);
        historyPanel = new HistoryPanel(taskManager);

        contentPanel.add(homePanel, "HOME");
        contentPanel.add(taskListPanel, "TASK_LIST");
        contentPanel.add(taskInputPanel, "TASK_INPUT");
        contentPanel.add(historyPanel, "HISTORY");

        add(contentPanel, BorderLayout.CENTER);

        showPanel("HOME");
    }

    private JPanel createNavigationPanel() {
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BorderLayout());
        navPanel.setPreferredSize(new Dimension(250, 0));
        navPanel.setBackground(SECONDARY_COLOR);

        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(PRIMARY_COLOR);
        logoPanel.setPreferredSize(new Dimension(250, 100));
        logoPanel.setLayout(new BorderLayout());

        JLabel logoLabel = new JLabel(" To-Do Manager", SwingConstants.CENTER);
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        logoLabel.setForeground(Color.WHITE);
        logoPanel.add(logoLabel, BorderLayout.CENTER);

        navPanel.add(logoPanel, BorderLayout.NORTH);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setBackground(SECONDARY_COLOR);
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        JButton btnHome = createNavButton(" Dashboard", "HOME");
        JButton btnTaskList = createNavButton(" Task List", "TASK_LIST");
        JButton btnAddTask = createNavButton(" Add Task", "TASK_INPUT");
        JButton btnHistory = createNavButton(" History", "HISTORY");


        buttonsPanel.add(btnHome);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonsPanel.add(btnTaskList);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonsPanel.add(btnAddTask);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonsPanel.add(btnHistory);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        navPanel.add(buttonsPanel, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(SECONDARY_COLOR);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        JLabel footerLabel = new JLabel("<html><center>© 2025<br>UAP Project</center></html>",
                SwingConstants.CENTER);
        footerLabel.setForeground(new Color(149, 165, 166));
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerPanel.add(footerLabel);

        navPanel.add(footerPanel, BorderLayout.SOUTH);

        return navPanel;
    }

    private JButton createNavButton(String text, String panelName) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(220, 45));
        button.setPreferredSize(new Dimension(220, 45));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(SECONDARY_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(PRIMARY_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(SECONDARY_COLOR);
            }
        });

        if (panelName != null) {
            button.addActionListener(e -> showPanel(panelName));
        }

        return button;
    }

    public void showPanel(String panelName) {
        if ("HOME".equals(panelName)) {
            homePanel.refreshStatistics();
        } else if ("TASK_LIST".equals(panelName)) {
            taskListPanel.refreshTable();
        } else if ("HISTORY".equals(panelName)) {
            historyPanel.refreshHistory();
        } else if ("TASK_INPUT".equals(panelName)) {
            taskInputPanel.clearForm();
        }

        cardLayout.show(contentPanel, panelName);
    }

    public void showEditPanel(Task task) {
        taskInputPanel.loadTaskForEdit(task);
        cardLayout.show(contentPanel, "TASK_INPUT");
    }

    private void saveDataBeforeExit() {
        try {
            taskManager.saveData();
            System.out.println("Data saved successfully");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error saving data: " + e.getMessage(),
                    "Save Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }

    class DashboardHomePanel extends JPanel {
        private TaskManager manager;
        private MainDashboard parent;
        private JLabel totalTasksLabel;
        private JLabel completedTasksLabel;
        private JLabel pendingTasksLabel;
        private JLabel completionRateLabel;

        public DashboardHomePanel(TaskManager manager, MainDashboard parent) {
            this.manager = manager;
            this.parent = parent;

            setLayout(new BorderLayout());
            setBackground(BACKGROUND_COLOR);
            setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

            initComponents();
        }

        private void initComponents() {
            JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            titlePanel.setBackground(BACKGROUND_COLOR);
            JLabel titleLabel = new JLabel("Dashboard Overview");
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
            titleLabel.setForeground(SECONDARY_COLOR);
            titlePanel.add(titleLabel);

            add(titlePanel, BorderLayout.NORTH);

            JPanel statsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
            statsPanel.setBackground(BACKGROUND_COLOR);
            statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

            JPanel totalCard = createStatCard("Total Tasks", "0", "", PRIMARY_COLOR);
            totalTasksLabel = (JLabel) ((JPanel) totalCard.getComponent(1)).getComponent(0);

            JPanel completedCard = createStatCard("Completed", "0", "", ACCENT_COLOR);
            completedTasksLabel = (JLabel) ((JPanel) completedCard.getComponent(1)).getComponent(0);

            JPanel pendingCard = createStatCard("Pending", "0", "", new Color(230, 126, 34));
            pendingTasksLabel = (JLabel) ((JPanel) pendingCard.getComponent(1)).getComponent(0);

            JPanel rateCard = createStatCard("Completion Rate", "0%", "", new Color(155, 89, 182));
            completionRateLabel = (JLabel) ((JPanel) rateCard.getComponent(1)).getComponent(0);

            statsPanel.add(totalCard);
            statsPanel.add(completedCard);
            statsPanel.add(pendingCard);
            statsPanel.add(rateCard);

            add(statsPanel, BorderLayout.CENTER);

            JPanel actionsPanel = createQuickActionsPanel();
            add(actionsPanel, BorderLayout.SOUTH);

            refreshStatistics();
        }

        private JPanel createStatCard(String title, String value, String icon, Color color) {
            JPanel card = new JPanel(new BorderLayout(10, 10));
            card.setBackground(CARD_COLOR);
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                    BorderFactory.createEmptyBorder(20, 20, 20, 20)));

            JPanel iconPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            iconPanel.setBackground(CARD_COLOR);
            JLabel iconLabel = new JLabel(icon);
            iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 40));
            iconPanel.add(iconLabel);

            JPanel textPanel = new JPanel();
            textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
            textPanel.setBackground(CARD_COLOR);

            JLabel valueLabel = new JLabel(value);
            valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
            valueLabel.setForeground(color);
            valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel titleLabel = new JLabel(title);
            titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            titleLabel.setForeground(new Color(127, 140, 141));
            titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            textPanel.add(valueLabel);
            textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            textPanel.add(titleLabel);

            card.add(iconPanel, BorderLayout.WEST);
            card.add(textPanel, BorderLayout.CENTER);

            return card;
        }

        private JPanel createQuickActionsPanel() {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
            panel.setBackground(BACKGROUND_COLOR);
            panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

            JButton btnAddTask = createActionButton(" Add New Task", ACCENT_COLOR);
            btnAddTask.addActionListener(e -> parent.showPanel("TASK_INPUT"));

            JButton btnViewTasks = createActionButton(" View All Tasks", PRIMARY_COLOR);
            btnViewTasks.addActionListener(e -> parent.showPanel("TASK_LIST"));

            panel.add(btnAddTask);
            panel.add(btnViewTasks);

            return panel;
        }

        private JButton createActionButton(String text, Color color) {
            JButton button = new JButton(text);
            button.setFont(new Font("Segoe UI", Font.BOLD, 14));
            button.setForeground(Color.WHITE);
            button.setBackground(color);
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setPreferredSize(new Dimension(200, 45));
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

        public void refreshStatistics() {
            TaskManager.TaskStatistics stats = manager.getStatistics();
            totalTasksLabel.setText(String.valueOf(stats.total));
            completedTasksLabel.setText(String.valueOf(stats.completed));
            pendingTasksLabel.setText(String.valueOf(stats.pending + stats.inProgress));
            completionRateLabel.setText(String.format("%.1f%%", stats.getCompletionRate()));
        }
    }
}
