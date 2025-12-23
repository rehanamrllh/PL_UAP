package com.uap;

final class UiFormat {
    private UiFormat() {
    }

    static String toBackend(String uiValue) {
        if (uiValue == null) {
            return null;
        }
        switch (uiValue) {
            case "High":
                return "HIGH";
            case "Medium":
                return "MEDIUM";
            case "Low":
                return "LOW";
            case "Pending":
                return "PENDING";
            case "In Progress":
                return "IN_PROGRESS";
            case "Completed":
                return "COMPLETED";
            default:
                return uiValue.toUpperCase().replace(" ", "_");
        }
    }

    static String toUi(String backendValue) {
        if (backendValue == null) {
            return null;
        }
        switch (backendValue) {
            case "HIGH":
                return "High";
            case "MEDIUM":
                return "Medium";
            case "LOW":
                return "Low";
            case "PENDING":
                return "Pending";
            case "IN_PROGRESS":
                return "In Progress";
            case "COMPLETED":
                return "Completed";
            default:
                return titleCaseFromUpperSnake(backendValue);
        }
    }

    static String truncate(String text, int maxLength) {
        if (text == null || text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength) + "...";
    }

    private static String titleCaseFromUpperSnake(String value) {
        // Convert SOME_TEXT to Some Text
        String[] words = value.toLowerCase().split("_");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (word.isEmpty()) {
                continue;
            }
            if (result.length() > 0) {
                result.append(' ');
            }
            result.append(Character.toUpperCase(word.charAt(0)));
            if (word.length() > 1) {
                result.append(word.substring(1));
            }
        }
        return result.toString();
    }
}
