import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

class Task {
    private String description;
    private boolean isCompleted;

    public Task(String description) {
        this.description = description;
        this.isCompleted = false;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void markAsCompleted() {
        isCompleted = true;
    }

    public void editDescription(String newDescription) {
        this.description = newDescription;
    }

    @Override
    public String toString() {
        return description + (isCompleted ? " (Completed)" : " (Pending)");
    }
}

public class TodoListAppGUI extends JFrame {
    private ArrayList<Task> tasks;
    private DefaultListModel<String> taskListModel;
    private JList<String> taskList;

    public TodoListAppGUI() {
        tasks = new ArrayList<>();
        taskListModel = new DefaultListModel<>();

        setTitle("To-Do List Application");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Task list display
        taskList = new JList<>(taskListModel);
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(taskList);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 4));

        JButton addButton = new JButton("Add Task");
        JButton editButton = new JButton("Edit Task");
        JButton completeButton = new JButton("Mark Completed");
        JButton removeButton = new JButton("Remove Task");
        JButton filterPendingButton = new JButton("Show Pending");
        JButton filterCompletedButton = new JButton("Show Completed");
        JButton saveButton = new JButton("Save Tasks");
        JButton loadButton = new JButton("Load Tasks");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(completeButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(filterPendingButton);
        buttonPanel.add(filterCompletedButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add task action
        addButton.addActionListener(e -> {
            String taskDescription = JOptionPane.showInputDialog(this, "Enter task description:");
            if (taskDescription != null && !taskDescription.trim().isEmpty()) {
                addTask(taskDescription);
            }
        });

        // Edit task action
        editButton.addActionListener(e -> {
            int selectedIndex = taskList.getSelectedIndex();
            if (selectedIndex != -1) {
                String newDescription = JOptionPane.showInputDialog(this, "Edit task description:", tasks.get(selectedIndex).getDescription());
                if (newDescription != null && !newDescription.trim().isEmpty()) {
                    editTask(selectedIndex, newDescription);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a task to edit.");
            }
        });

        // Complete task action
        completeButton.addActionListener(e -> {
            int selectedIndex = taskList.getSelectedIndex();
            if (selectedIndex != -1) {
                markTaskAsCompleted(selectedIndex);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a task to mark as completed.");
            }
        });

        // Remove task action
        removeButton.addActionListener(e -> {
            int selectedIndex = taskList.getSelectedIndex();
            if (selectedIndex != -1) {
                removeTask(selectedIndex);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a task to remove.");
            }
        });

        // Filter pending tasks action
        filterPendingButton.addActionListener(e -> filterTasks(false));

        // Filter completed tasks action
        filterCompletedButton.addActionListener(e -> filterTasks(true));

        // Save tasks action
        saveButton.addActionListener(e -> saveTasksToFile());

        // Load tasks action
        loadButton.addActionListener(e -> loadTasksFromFile());

        add(mainPanel);
    }

    private void addTask(String description) {
        Task task = new Task(description);
        tasks.add(task);
        taskListModel.addElement(task.toString());
    }

    private void editTask(int index, String newDescription) {
        Task task = tasks.get(index);
        task.editDescription(newDescription);
        taskListModel.set(index, task.toString());
    }

    private void markTaskAsCompleted(int index) {
        Task task = tasks.get(index);
        task.markAsCompleted();
        taskListModel.set(index, task.toString());
    }

    private void removeTask(int index) {
        tasks.remove(index);
        taskListModel.remove(index);
    }

    private void filterTasks(boolean showCompleted) {
        taskListModel.clear();
        for (Task task : tasks) {
            if (task.isCompleted() == showCompleted) {
                taskListModel.addElement(task.toString());
            }
        }
    }

    private void saveTasksToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("tasks.dat"))) {
            oos.writeObject(tasks);
            JOptionPane.showMessageDialog(this, "Tasks saved successfully.");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving tasks: " + ex.getMessage());
        }
    }

    private void loadTasksFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("tasks.dat"))) {
            tasks = (ArrayList<Task>) ois.readObject();
            taskListModel.clear();
            for (Task task : tasks) {
                taskListModel.addElement(task.toString());
            }
            JOptionPane.showMessageDialog(this, "Tasks loaded successfully.");
        } catch (IOException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Error loading tasks: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TodoListAppGUI app = new TodoListAppGUI();
            app.setVisible(true);
        });
    }
}
