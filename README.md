# ToDo List
A simple Todo list application developed in Java with both in-terminal and GUI implementations. Users can view completed and uncompleted tasks, add new tasks, mark tasks as completed, as well as import and export to-do lists.<br><br>
![image](https://github.com/stevanbeljic/ToDo/assets/60798649/e3272ee8-c60d-4058-a9b1-7ec733b04054)

## GUI Implementation
### Running Instructions
To run:<br>
1. Navigate to project root
2. Navigate to the ``build`` folder
3. Extract ToDo_jar.zip
4. Run the executable
5. Voila!

### How to Use
To enter a new task, simply enter the task name and either press ``Enter`` or the ``Add Task`` button.<br>
Tasks can be marked as completed, or deleted from the table, by pressing the corresponding buttons.<br>

You can save your todo list by clicking ``Save ToDo List``. Saving will not be allowed if the list is empty. Lists are saved as a JSON file.<br>
You can import an existing list by clicking ``Import ToDo List``. Ensure your ``todo_list.json`` file is in the same location as the program executable.<br>

## In-Terminal Implementation
To compile:<br>
1. Navigate to project root in terminal
2. Enter ``cd src``
3. Compile with ``javac Main.java``
4. Run with ``java Main``
5. Voila!

### How to Use
Users are presented with the initial menu:
```
1. Add a task (Add a new task)
2. View Uncompleted Tasks (Lists all tasks that have not been marked Completed)
3. View Completed Tasks (Lists all tasks that have been marked Completed)
4. View all Tasks (Lists all tasks)
5. Mark Task as Completed (Displays a menu to select which task to mark as Completed)
6. Save as todo.txt file (Saves the todo list to a .txt file)
7. Re-import todo.txt file (Loads a todo.txt file, which must be located in the same directory as the executable)
8. Exit program (Terminates the program)
```

## Competencies Demonstrated
- Java Swing GUI Design (with MigLayout and FlatLaF)
- File I/O, with JSON files using Jackson library
- Exception and error handling
