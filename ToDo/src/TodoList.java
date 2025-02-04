import java.io.*;
import java.util.*;

class TodoList {
    private List<TodoItem> items;

    public TodoList() {
        items = new ArrayList<>();
    }

    public void addItem(String description, String date, int priority) {
        items.add(new TodoItem(description, date, priority));
    }

    public void removeItem(int index) {
        if (index >= 0 && index < items.size()) {
            items.remove(index);
        }
    }

    public void saveToFile(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (TodoItem item : items) {
                writer.write(item.toString());
                writer.newLine();
            }
        }
    }

    public void loadFromFile(String filename) throws IOException {
        items.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    addItem(parts[0], parts[1], Integer.parseInt(parts[2]));
                }
            }
        }
    }

    public String searchByDate(String date) {
        StringBuilder result = new StringBuilder();
        for (TodoItem item : items) {
            if (item.getDate().equals(date)) {
                result.append(item).append("\n");
            }
        }
        return result.toString();
    }

    public void sortByPriority() {
        items.sort(Comparator.comparingInt(TodoItem::getPriority));
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            result.append((i + 1)).append(". ").append(items.get(i)).append("\n");
        }
        return result.toString();
    }
}

class TodoItem {
    private String description;
    private String date;
    private int priority;

    public TodoItem(String description, String date, int priority) {
        this.description = description;
        this.date = date;
        this.priority = priority;
    }

    public String getDate() {
        return date;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return description + "," + date + "," + priority;
    }
}
