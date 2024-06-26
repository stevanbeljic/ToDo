package gui;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Scanner;

public class Driver {

    private static class Task {
        boolean isCompleted;
        String taskName;

        public Task (String name) {
            taskName = name;
        }

        public Task (String name, String completion){
            taskName = name;
            isCompleted = Boolean.parseBoolean(completion);
        }
    }

    private ArrayList<Task> tasks = new ArrayList<>();
    private static Scanner kb;

    private void addTask(){
        System.out.println("Enter a name for the task: ");
        String taskName = kb.nextLine();
        Task t = new Task(taskName);
        tasks.add(t);
    }

    private void printFilteredTasks(boolean finished) {
        String decidingWord = (finished) ? "completed" : "uncompleted";
        System.out.println("Displaying "+decidingWord+" tasks:");

        for(Task t : tasks){
            if (t.isCompleted == finished) {
                System.out.println(t.taskName+ ": "+t.isCompleted);
            }
        }
    }

    private void printTasks() {
        System.out.println("Displaying all tasks");
        for (Task t: tasks){
            System.out.println(t.taskName+ ": "+t.isCompleted);
        }
    }

    private void markAsCompleted() {
        System.out.println("Which task would you like to mark as completed");
        System.out.println("Please select the corresponding number in the menu");

        int menuNumber = 1;
        for(Task t : tasks) {
            System.out.println(menuNumber+". "+t.taskName);
            menuNumber++;
        }

        int menuSelection = Integer.parseInt(kb.nextLine());
        tasks.get(menuSelection-1).isCompleted = true;
        System.out.println(tasks.get(menuSelection-1).taskName +" marked as completed");
    }

    private void saveToFile(){
        try {
            Writer fw = new FileWriter("todo.txt", false);

            for(Task t : tasks){
                fw.write(t.taskName+":"+t.isCompleted+"\n");
            }
            fw.close();
        } catch (Exception e) {
            System.out.println("Failed to save file");
        }
    }

    private void importFile(){
        try {
            BufferedReader fr = new BufferedReader(new FileReader("todo.txt"));
            tasks.clear();

            String line;
            while((line = fr.readLine()) != null){
                int index = line.indexOf(':');
                tasks.add(new Task(line.substring(0, index), line.substring(index+1)));
            }

            fr.close();
        } catch (Exception e){
            System.out.println("Failed to open file");
        }
    }

}
