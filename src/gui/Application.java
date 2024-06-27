package gui;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.formdev.flatlaf.FlatIntelliJLaf;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class Application {

    private class Task {
        boolean isCompleted;
        String taskName;
        JButton delete;
        JButton markCompleted;

        public Task(String name) {
            this.taskName = name;
            this.isCompleted = false;
            delete = new JButton("Delete");
            markCompleted = new JButton("Mark Complete");
        }

        public Task(String name, String completion){
            this.taskName = name;
            this.isCompleted = Boolean.parseBoolean(completion);
            delete = new JButton("Delete");
            markCompleted = new JButton("Mark Complete");
        }
    }

    JFrame frame;
    JPanel rootPanel;
    private JTextField newTaskField;
    private JButton newTaskButton;
    private DefaultTableModel tableModel;
    private JTable taskTable;
    private JScrollPane taskNameScrollPane;
    private JButton importListButton;
    private JButton exportListButton;
    private JLabel errorLabel;

    public Application() {
        FlatIntelliJLaf.setup();

        exportListButton = new JButton("Save ToDo List");
        errorLabel = new JLabel("");

        frame = new JFrame("ToDo App");
        frame.setResizable(false);
        JLabel titleLabel = new JLabel("Stevan's ToDo App");
        JLabel initLabel = new JLabel("Enter task:");
        newTaskField = new JTextField();
        newTaskButton = new JButton("Add Task");
        tableModel = new DefaultTableModel();
        taskTable = new JTable();
        taskNameScrollPane = new JScrollPane();
        importListButton = new JButton("Import ToDo List");
        rootPanel = new JPanel(new MigLayout());
        rootPanel.requestFocusInWindow();
        rootPanel.add(titleLabel, "wrap");

        newTaskField.setPreferredSize(new Dimension(150, 20));
        rootPanel.add(initLabel);
        rootPanel.add(newTaskField, "cell 0 1 3 1");
        rootPanel.add(newTaskButton, "cell 4 1");
        rootPanel.add(importListButton, "cell 5 1");
        rootPanel.add(exportListButton, "cell 6 1, wrap");
        rootPanel.add(errorLabel, "span, wrap");

        tableModel.addColumn("Task");
        tableModel.addColumn("Completion");
        tableModel.addColumn("Mark Completed");
        tableModel.addColumn("Delete");
        titleLabel.putClientProperty("FlatLaf.styleClass", "h1");

        taskTable.setModel(tableModel);
        taskTable.setShowVerticalLines(false);
        taskTable.setRowSelectionAllowed(false);
        taskTable.setCellSelectionEnabled(false);
        taskTable.setRowHeight(30);

        taskNameScrollPane.setViewportView(taskTable);
        taskNameScrollPane.setPreferredSize(new Dimension(620, 150));
        taskNameScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants. VERTICAL_SCROLLBAR_ALWAYS);
        rootPanel.add(taskNameScrollPane,"span");

        /*
            Add a new Task to the todo list
         */
        Action addTask = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fieldText = newTaskField.getText();
                if(fieldText.isBlank()){
                    return;
                }

                Task newTask = new Task(fieldText);
                tableModel.addRow(new Object[]{newTask.taskName, newTask.isCompleted ? "Complete" : "Incomplete", "Mark Completed", "Delete"});
                newTaskField.setText("");
                newTaskField.requestFocusInWindow();
            }
        };

        newTaskField.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER){
                    addTask.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
                }
            }

        });

        newTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTask.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
            }
        });

        /*
            Save the todo list
         */
        exportListButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tableModel.getRowCount() < 1) {
                    errorLabel.setText("No data to export");
                    return;
                }

                ObjectMapper mapper = new ObjectMapper();
                ObjectNode rootNode = mapper.createObjectNode();
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    ObjectNode taskNode = mapper.createObjectNode();
                    taskNode.put("taskName", (String) tableModel.getValueAt(i, 0));
                    taskNode.put("completion", (String) tableModel.getValueAt(i, 1));
                    rootNode.set("task_" + i, taskNode);
                }

                try {
                    String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
                    System.out.println(jsonString);
                    mapper.writerWithDefaultPrettyPrinter().writeValue(new File("todo_list.json"), rootNode);
                    errorLabel.setText("File exported as todo_list.json");
                } catch (Exception er) {
                    er.printStackTrace();
                    errorLabel.setText("Error exporting data");
                }

            }
        });

        importListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(tableModel.getRowCount() > 0){
                    errorLabel.setText("Import list added onto existing list");
                }

                ObjectMapper mapper = new ObjectMapper();
                try {
                    File file = new File("todo_list.json");

                    JsonNode root = mapper.readTree(file);
                    for(JsonNode subNode : root) {
                        String name = subNode.get("taskName").asText();
                        String completion = subNode.get("completion").asText();
                        tableModel.addRow(new Object[]{name, completion, completion.equals("Completed") ? null : "Mark Completed", "Delete" });
                    }
                } catch (Exception er) {
                    er.printStackTrace();
                    errorLabel.setText("Unable to load todo_list.json file");
                }
            }
        });

        /*
            Mark item in todo list as completed
         */
        Action markCompleted = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTable table = (JTable)e.getSource();
                int selectedRow = Integer.valueOf(e.getActionCommand());
                Object markComplete = table.getModel().getValueAt(selectedRow, 2);
                ((DefaultTableModel)table.getModel()).setValueAt("Completed", selectedRow, 1);
                ((DefaultTableModel)table.getModel()).setValueAt(null, selectedRow, 2);
            }
        };

        /*
            Delete a task from the todo list
         */
        Action delete = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTable table = (JTable)e.getSource();
                int selectedRow = Integer.valueOf(e.getActionCommand());
                Object markComplete = table.getModel().getValueAt(selectedRow, 2);
                ((DefaultTableModel)table.getModel()).setValueAt("Completed", selectedRow, 1);
                ((DefaultTableModel)table.getModel()).removeRow(selectedRow);
            }
        };

        ButtonColumn buttonColumnComplete = new ButtonColumn(taskTable, markCompleted, 2);
        ButtonColumn buttonColumnDelete = new ButtonColumn(taskTable, delete, 3);

        frame.add(rootPanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    /**
     * Main driver method for the application
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Application::new);
    }



}
