package com.uap.ui.panels;

import com.uap.data.DataManager;
import com.uap.ui.UIColors;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class DashboardPanel extends JPanel {
    private JLabel lblTotal, lblCompleted, lblPending, lblRate;
    private DataManager dataManager;

    public DashboardPanel(DataManager dataManager) {
        this.dataManager = dataManager;

        setLayout(new BorderLayout());
        setBackground(UIColors.BG_COLOR);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Dashboard Overview");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(UIColors.SIDEBAR_COLOR);
        add(title, BorderLayout.NORTH);

        JPanel cardsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        cardsPanel.setBackground(UIColors.BG_COLOR);
        cardsPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        lblTotal = createCard(cardsPanel, "Total Tasks", "0", new Color(52, 152, 219));
        lblCompleted = createCard(cardsPanel, "Completed", "0", UIColors.BTN_GREEN);
        lblPending = createCard(cardsPanel, "Pending", "0", UIColors.ACCENT_ORANGE);
        lblRate = createCard(cardsPanel, "Completion Rate", "0%", UIColors.ACCENT_PURPLE);

        add(cardsPanel, BorderLayout.CENTER);
    }

    private JLabel createCard(JPanel parent, String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(UIColors.CARD_BG);
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
