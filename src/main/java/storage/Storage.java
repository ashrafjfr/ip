package storage;

import exceptions.DeadlineException;
import jarvis.Jarvis;
import tasklist.Task;
import ui.Ui;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileWriter;

public class Storage {
    private static final String LINE = Ui.LINE;
    private static final String LINE_W_NL = Ui.LINE_W_NL;
    public static final String JARVIS_TXT = "jarvis/jarvis.txt";
    private static final String DIVIDER = ",";

    /**
     * This method tries to find the file that saved the list of tasks from previous usage
     * of Jarvis bot. If file not found, a new directory and file will be created using the
     * given file path to store the tasks in a text file.
     * @param taskList main list keeping track of user's tasks
     */
    public static void findTaskFile(ArrayList<Task> taskList) {
        File jarvisFile = new File(JARVIS_TXT);
        try {
            if (!jarvisFile.exists()) {
                jarvisFile.getParentFile().mkdirs();
                jarvisFile.createNewFile();
                System.out.println("A new file has been created to store your tasks Sir!\n"
                        + "What would you like me to do Sir?\n"
                        + LINE);
            } else {
                System.out.println("Give me a moment to load up your tasks Sir!\n"
                        + ".\n" + ".\n" + "Done\n"
                        + "What would you like me to do Sir?\n"
                        + LINE);
                loadTasks(taskList, jarvisFile);
            }
        } catch (IOException e) {
            System.out.println(LINE_W_NL
                    + "There has been an error detected when creating a new file Sir!\n"
                    + "You might want to take a look at it.\n"
                    + LINE);
        }
    }

    /**
     * This method will loop through the previous saved task text file to load tasks into
     * the list of tasks.
     * @param taskList main list keeping track of user's tasks
     * @param jarvisFile the task text file retrieved from findTaskfile method
     */
    public static void loadTasks(ArrayList<Task> taskList, File jarvisFile) {
        int lineCount = 0;
        try {
            Scanner j = new Scanner(jarvisFile);
            while(j.hasNext()){
                inputTask(taskList,j.nextLine(),lineCount);
                lineCount++;
            }
        } catch (FileNotFoundException e) {
            System.out.println(LINE_W_NL
                    + "I can't seem to find the file Sir.\n"
                    + LINE);
        }

    }

    /**
     * This method will input tasks from each line on the text file into the list of tasks.
     * It uses a switch case to decide which type of task is in each line and will add it
     * into the list of tasks.
     * @param taskList main list keeping track of user's tasks
     * @param textLine is the current line of text in the text file
     * @param lineCount is the line number of the current line being loaded
     */
    public static void inputTask(ArrayList<Task> taskList, String textLine, int lineCount) {
        String[] lineInputs = textLine.split(DIVIDER);
        switch(lineInputs[0]){
        case "T":
            tasklist.TaskList.addTodoFileTask(lineInputs[2], taskList);
            break;
        case "D":
            tasklist.TaskList.addDeadlineFileTask(lineInputs[2], lineInputs[3], taskList);
            break;
        case "E":
            tasklist.TaskList.addEventFileTask(lineInputs[2],lineInputs[3], taskList);
            break;
        default:
            break;
        }
        if (lineInputs[1].trim().equals("1")) {
            taskList.get(lineCount).markAsDone();
        }
    }

    /**
     * This method will write tasks from the current list of tasks onto the text file
     * for it to be saved when the user exits from bot. The same file will be loaded up
     * when the user starts the bot again in the future with the saved tasks loaded.
     * @param taskList main list keeping track of user's tasks
     */
    public static void fillJarvisFile(ArrayList<Task> taskList) {
        try {
            FileWriter writer = new FileWriter(JARVIS_TXT);
            for (Task task : taskList) {
                String type = task.getType();
                String doneStatus;
                if (task.isDone()) {
                    doneStatus = "1";
                } else {
                    doneStatus = "0";
                }
                writer.write(type + DIVIDER + doneStatus + DIVIDER + task.getDescription() + System.lineSeparator());
            }
            writer.close();
        } catch (IOException e) {
            System.out.println(LINE_W_NL
                    + "There seems to be an error saving the task Sir.\n"
                    + LINE);
        }

    }
}
