package gui;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Application {

    private class Task {
        boolean isCompleted;
        String taskName;
        JButton delete;
        JButton markCompleted;

        public Task (String name) {
            taskName = name;
            isCompleted = false;
            delete = new JButton("Delete");
            markCompleted = new JButton("Mark Complete");
            activateDeleteButton();
            activateCompleteButton();
        }

        public Task (String name, String completion){
            taskName = name;
            isCompleted = Boolean.parseBoolean(completion);
            delete = new JButton("Delete");
            markCompleted = new JButton("Mark Complete");
            activateDeleteButton();
            activateCompleteButton();
        }

        private void activateCompleteButton(){
            this.isCompleted = true;
            this.markCompleted.setEnabled(false);
            this.markCompleted.setVisible(false);
        }

        private void activateDeleteButton(){
            this.delete.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Deletion implementation");
                }
            });
        }
    }

    JFrame frame = new JFrame("ToDo App");
    JPanel rootPanel;
    private JTextField newTaskField = new JTextField();
    private JButton newTaskButton = new JButton("Add Task");
    private DefaultTableModel tableModel = new DefaultTableModel();
    private JTable taskTable = new JTable();
    private JScrollPane taskNameScrollPane = new JScrollPane();

    public Application() {
        rootPanel = new JPanel(new MigLayout());
        rootPanel.add(new JLabel("ToDo App"), "wrap");

        newTaskField.setPreferredSize(new Dimension(120, 20));
        rootPanel.add(newTaskField, "span 2");
        rootPanel.add(newTaskButton, "wrap");

        tableModel.addColumn("Task");
        tableModel.addColumn("Completion");
        tableModel.addColumn("Mark Completed");
        tableModel.addColumn("Delete");

        taskTable.setModel(tableModel);
        taskNameScrollPane.setViewportView(taskTable);
        taskNameScrollPane.setPreferredSize(new Dimension(620, 100));
        taskNameScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants. VERTICAL_SCROLLBAR_ALWAYS);
        rootPanel.add(taskNameScrollPane,"span");

        newTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fieldText = newTaskField.getText();
                if(fieldText.isBlank()){
                    return;
                }
                Task newTask = new Task(fieldText);
                tableModel.addRow(new Object[]{newTask.taskName, newTask.isCompleted ? "Complete" : "Incomplete", newTask.markCompleted, new JPanel().add(newTask.delete)});
                newTaskField.setText("");
                newTaskField.requestFocusInWindow();
            }
        });

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
