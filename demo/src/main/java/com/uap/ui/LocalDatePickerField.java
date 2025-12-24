package com.uap.ui;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;

public class LocalDatePickerField extends JPanel {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    private final JTextField textField;
    private final JButton pickerButton;

    private LocalDate date;

    public LocalDatePickerField() {
        this("");
    }

    public LocalDatePickerField(String placeholder) {
        super(new BorderLayout(5, 0));

        textField = new JTextField();
        textField.setToolTipText("Format: YYYY-MM-DD");
        if (placeholder != null && !placeholder.isEmpty()) {
            textField.setText(placeholder);
        }

        pickerButton = new JButton("...");
        pickerButton.setPreferredSize(new Dimension(45, 35));
        pickerButton.setFocusPainted(false);
        pickerButton.setBackground(Color.WHITE);
        pickerButton.setForeground(Color.BLACK);
        pickerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pickerButton.setToolTipText("Choose date from calendar");

        pickerButton.addActionListener(e -> openPickerDialog());

        add(textField, BorderLayout.CENTER);
        add(pickerButton, BorderLayout.EAST);

        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                syncFromText();
            }
        });
    }

    public LocalDate getDate() {
        syncFromText();
        return date;
    }

    public void setDate(LocalDate newDate) {
        LocalDate old = this.date;
        this.date = newDate;
        textField.setText(newDate == null ? "" : FORMATTER.format(newDate));
        firePropertyChange("date", old, newDate);
    }

    public void clear() {
        setDate(null);
    }

    public JTextField getTextField() {
        return textField;
    }

    private void syncFromText() {
        String raw = textField.getText() == null ? "" : textField.getText().trim();
        if (raw.isEmpty() || "yyyy-mm-dd".equalsIgnoreCase(raw) || "YYYY-MM-DD".equals(raw)) {
            if (date != null) {
                LocalDate old = date;
                date = null;
                firePropertyChange("date", old, null);
            }
            return;
        }

        LocalDate parsed = LocalDate.parse(raw, FORMATTER);
        if (!parsed.equals(date)) {
            LocalDate old = date;
            date = parsed;
            firePropertyChange("date", old, parsed);
        }
    }

    private void openPickerDialog() {
        Window owner = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = owner instanceof Frame
                ? new JDialog((Frame) owner, "Select Date", true)
                : new JDialog(owner, "Select Date", Dialog.ModalityType.APPLICATION_MODAL);

        dialog.setLayout(new BorderLayout());

        JPanel calendarPanel = new JPanel(new GridLayout(7, 7, 5, 5));
        calendarPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        calendarPanel.setBackground(Color.WHITE);

        Calendar calendar = Calendar.getInstance();
        try {
            LocalDate current = getDate();
            if (current != null) {
                calendar.set(current.getYear(), current.getMonthValue() - 1, current.getDayOfMonth());
            }
        } catch (DateTimeParseException ignored) {
        }

        final Calendar viewCal = (Calendar) calendar.clone();

        JPanel monthPanel = new JPanel(new FlowLayout());
        monthPanel.setBackground(UIColors.BTN_BLUE);

        JButton prevButton = new JButton("◀");
        JButton nextButton = new JButton("▶");
        JLabel monthLabel = new JLabel();

        for (JButton b : new JButton[] { prevButton, nextButton }) {
            b.setFocusPainted(false);
            b.setBorderPainted(false);
            b.setContentAreaFilled(false);
            b.setForeground(Color.WHITE);
            b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        monthLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        monthLabel.setForeground(Color.WHITE);

        monthPanel.add(prevButton);
        monthPanel.add(monthLabel);
        monthPanel.add(nextButton);

        Runnable render = () -> {
            calendarPanel.removeAll();

            String[] months = { "January", "February", "March", "April", "May", "June",
                    "July", "August", "September", "October", "November", "December" };
            monthLabel.setText(months[viewCal.get(Calendar.MONTH)] + " " + viewCal.get(Calendar.YEAR));

            String[] days = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
            for (String day : days) {
                JLabel label = new JLabel(day, SwingConstants.CENTER);
                label.setFont(new Font("Segoe UI", Font.BOLD, 12));
                label.setForeground(UIColors.BTN_BLUE);
                calendarPanel.add(label);
            }

            Calendar temp = (Calendar) viewCal.clone();
            temp.set(Calendar.DAY_OF_MONTH, 1);
            int firstDayOfWeek = temp.get(Calendar.DAY_OF_WEEK);
            int daysInMonth = temp.getActualMaximum(Calendar.DAY_OF_MONTH);

            for (int i = 1; i < firstDayOfWeek; i++) {
                calendarPanel.add(new JLabel(""));
            }

            LocalDate today = LocalDate.now();
            int year = viewCal.get(Calendar.YEAR);
            int month = viewCal.get(Calendar.MONTH) + 1;

            for (int day = 1; day <= daysInMonth; day++) {
                final int d = day;
                JButton dayButton = new JButton(String.valueOf(day));
                dayButton.setFocusPainted(false);
                dayButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

                LocalDate buttonDate = LocalDate.of(year, month, d);
                if (buttonDate.equals(today)) {
                    dayButton.setBackground(new Color(230, 240, 255));
                    dayButton.setForeground(UIColors.BTN_BLUE);
                } else {
                    dayButton.setBackground(Color.WHITE);
                }

                dayButton.addActionListener(e -> {
                    setDate(LocalDate.of(year, month, d));
                    dialog.dispose();
                });

                calendarPanel.add(dayButton);
            }

            calendarPanel.revalidate();
            calendarPanel.repaint();
        };

        prevButton.addActionListener(e -> {
            viewCal.add(Calendar.MONTH, -1);
            render.run();
        });

        nextButton.addActionListener(e -> {
            viewCal.add(Calendar.MONTH, 1);
            render.run();
        });

        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setBackground(Color.WHITE);

        JButton todayButton = UIUtils.createStyledButton("Today", UIColors.BTN_GREEN);
        JButton clearButton = UIUtils.createStyledButton("Clear", UIColors.SIDEBAR_COLOR);
        JButton cancelButton = UIUtils.createStyledButton("Cancel", UIColors.TEXT_GRAY);

        todayButton.addActionListener(e -> {
            setDate(LocalDate.now());
            dialog.dispose();
        });
        clearButton.addActionListener(e -> {
            clear();
            dialog.dispose();
        });
        cancelButton.addActionListener(e -> dialog.dispose());

        bottomPanel.add(todayButton);
        bottomPanel.add(clearButton);
        bottomPanel.add(cancelButton);

        render.run();

        dialog.add(monthPanel, BorderLayout.NORTH);
        dialog.add(calendarPanel, BorderLayout.CENTER);
        dialog.add(bottomPanel, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}
