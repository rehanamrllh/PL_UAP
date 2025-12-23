package com.uap;

import javax.swing.SwingUtilities;

/**
 * Entry point used by Maven jar manifest (see pom.xml mainClass).
 */
public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ToDoApp().setVisible(true));
    }
}