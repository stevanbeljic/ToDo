import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

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

    private static ArrayList<Task> tasks = new ArrayList<>();
    private static Scanner kb;

    private static void addTask(){
        System.out.println("Enter a name for the task: ");
        String taskName = kb.nextLine();
        Task t = new Task(taskName);
        tasks.add(t);
    }

    private static void printFilteredTasks(boolean finished) {
        String decidingWord = (finished) ? "completed" : "uncompleted";
        System.out.println("Displaying "+decidingWord+" tasks:");

        for(Task t : tasks){
            if (t.isCompleted == finished) {
                System.out.println(t.taskName+ ": "+t.isCompleted);
            }
        }
    }

    private static void printTasks() {
        System.out.println("Displaying all tasks");
        for (Task t: tasks){
            System.out.println(t.taskName+ ": "+t.isCompleted);
        }
    }

    private static void markAsCompleted() {
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

    private static void saveToFile(){
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

    private static void importFile(){
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

    public static void main(String[] args) {

        boolean continueRunning = true;
        kb = new Scanner(System.in);
        int menuSelection;

        while(continueRunning){
            System.out.println("\n\nPlease select a menu option to view your todo list:");
            System.out.println("1. Add a task");
            System.out.println("2. View Uncompleted Tasks");
            System.out.println("3. View Completed Tasks");
            System.out.println("4. View all Tasks");
            System.out.println("5. Mark Task as Completed");
            System.out.println("6. Save as todo.txt file");
            System.out.println("7. Re-import todo.txt file");
            System.out.println("8. Exit program");

            menuSelection  = Integer.parseInt(kb.nextLine());

            switch(menuSelection) {
                case 1:
                    addTask();
                    break;
                case 2:
                    printFilteredTasks(false);
                    break;
                case 3:
                    printFilteredTasks(true);
                    break;
                case 4:
                    printTasks();
                    break;
                case 5:
                    markAsCompleted();
                    break;
                case 6:
                    saveToFile();
                    break;
                case 7:
                    importFile();
                    break;
                case 8:
                    continueRunning = false;
                    break;
                default:
                    System.out.println("Unknown response. Please select a menu option.");
            }
        }
        kb.close();

    }
}