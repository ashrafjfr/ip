package storage;

import ui.Jarvis;
import tasklist.Task;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileWriter;

public class Storage {
    private static final String LINE = Jarvis.LINE;
    private static final String LINE_W_NL = Jarvis.LINE_W_NL;
    private static final String JARVIS_TXT = "ui/jarvis.txt";
    private static final String DIVIDER = ",";

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

    public static void inputTask(ArrayList<Task> taskList, String textLine, int lineCount) {
        String[] lineInputs = textLine.split(DIVIDER);
        switch(lineInputs[0]){
        case "T":
            Jarvis.addTodoFileTask(lineInputs[2], taskList);
            break;
        case "D":
            Jarvis.addDeadlineFileTask(lineInputs[2], lineInputs[3], taskList);
            break;
        case "E":
            Jarvis.addEventFileTask(lineInputs[2],lineInputs[3], taskList);
            break;
        default:
            break;
        }
        if (lineInputs[1].trim().equals("1")) {
            taskList.get(lineCount).markAsDone();
        }
    }

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