import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static TodoList todoList = new TodoList();

    public static void main(String[] args) {
        JFrame frame = new JFrame("Todo List");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JTextArea displayArea = new JTextArea(10, 30);
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        JTextField descriptionField = new JTextField(20);

        
        List<String> dateOptions = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (int i = 0; i < 30; i++) {
            dateOptions.add(currentDate.plusDays(i).format(formatter));
        }
        JComboBox<String> dateDropdown = new JComboBox<>(dateOptions.toArray(new String[0]));

        String[] priorityOptions = {"1 - High", "2 - Medium", "3 - Low"};
        JComboBox<String> priorityDropdown = new JComboBox<>(priorityOptions);

        JButton addButton = new JButton("Add Item");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String description = descriptionField.getText();
                String date = (String) dateDropdown.getSelectedItem();
                int priority = priorityDropdown.getSelectedIndex() + 1;
                todoList.addItem(description, date, priority);
                displayArea.setText(todoList.toString());
            }
        });

        JButton removeButton = new JButton("Remove Item");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = JOptionPane.showInputDialog("Enter item number to remove:");
                int index = Integer.parseInt(input) - 1;
                todoList.removeItem(index);
                displayArea.setText(todoList.toString());
            }
        });

        JButton saveButton = new JButton("Save to File");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                File defaultDir = new File(System.getProperty("user.home"), "Downloads/ToDo Application");
                if (!defaultDir.exists()) {
                    defaultDir.mkdirs();
                }
                fileChooser.setCurrentDirectory(defaultDir);
                int option = fileChooser.showSaveDialog(frame);
                if (option == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try {
                        todoList.saveToFile(file.getAbsolutePath());
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(frame, "Error saving to file: " + ex.getMessage());
                    }
                }
            }
        });

        JButton loadButton = new JButton("Load from File");
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                File defaultDir = new File(System.getProperty("user.home"), "Downloads/ToDo Application");
                if (!defaultDir.exists()) {
                    defaultDir.mkdirs();
                }
                fileChooser.setCurrentDirectory(defaultDir);
                int option = fileChooser.showOpenDialog(frame);
                if (option == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try {
                        todoList.loadFromFile(file.getAbsolutePath());
                        displayArea.setText(todoList.toString());
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(frame, "Error loading from file: " + ex.getMessage());
                    }
                }
            }
        });

        JButton searchButton = new JButton("Search by Date");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String date = JOptionPane.showInputDialog("Enter date to search (YYYY-MM-DD):");
                displayArea.setText(todoList.searchByDate(date));
            }
        });

        JButton sortButton = new JButton("Sort by Priority");
        sortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                todoList.sortByPriority();
                displayArea.setText(todoList.toString());
            }
        });

        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);
        panel.add(new JLabel("Date (YYYY-MM-DD):"));
        panel.add(dateDropdown);
        panel.add(new JLabel("Priority:"));
        panel.add(priorityDropdown);
        panel.add(addButton);
        panel.add(removeButton);
        panel.add(saveButton);
        panel.add(loadButton);
        panel.add(searchButton);
        panel.add(sortButton);
        panel.add(scrollPane);

        frame.getContentPane().add(BorderLayout.CENTER, panel);
        frame.setVisible(true);
    }
}