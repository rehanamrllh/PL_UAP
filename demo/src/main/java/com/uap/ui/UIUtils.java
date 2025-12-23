package com.uap.ui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

public class UIUtils {
    public static JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        return btn;
    }
}
