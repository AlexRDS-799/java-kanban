import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;

import com.yandex.app.service.File_Backed.FileBackedTaskManager;
import com.yandex.app.service.Interfaces.TaskManager;
import com.yandex.app.service.Managers;
import com.yandex.app.service.TaskManagerExceptions.ManagerSaveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;


public class FileBackedTaskManagerTest {
    FileBackedTaskManager filedBackedTaskManager;
    Task task1;
    Epic epic1;
    File taskFile;
    File historyFile;

    @BeforeEach
    void beforeEach() {
        try {
            taskFile = File.createTempFile("SaveTask", ".txt");
            historyFile = File.createTempFile("SaveHistory", ".txt");
            filedBackedTaskManager = Managers.getFileBackedManager(taskFile.getPath(), historyFile.getPath());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при создании менеджера");
        }

        task1 = new Task("Task1", "DescriptionTask1");
        epic1 = new Epic("Epic1", "DescriptionEpic1");

    }

    @Test
    void savedTasksInFile() {
        filedBackedTaskManager.addNewTask(task1);
        filedBackedTaskManager.addNewEpic(epic1);
        Subtask subtask = new Subtask("Subtask1", "DescriptionSubtask1", epic1.getId());
        filedBackedTaskManager.addNewSubtask(subtask);
        String task1Line = "1,TASK,Task1,NEW,DescriptionTask1";
        String epic1Line = "2,EPIC,Epic1,NEW,DescriptionEpic1";
        String subtask1Line = "3,SUBTASK,Subtask1,NEW,DescriptionSubtask1,2";
        try {
            BufferedReader bfr = new BufferedReader(new FileReader(taskFile));
            String firstLine = bfr.readLine();
            String taskLine = bfr.readLine();
            String epicLine = bfr.readLine();
            String subtaskLine = bfr.readLine();
            //Проверяем что данные записанные в файл, записаны в верном формате и последовательности
            assertEquals(task1Line, taskLine);
            assertEquals(epic1Line, epicLine);
            assertEquals(subtask1Line, subtaskLine);

            //Проверяем что прочитанные данные могут нам вернуть полноценный Task
            Task taskAfterReading = filedBackedTaskManager.taskFromString(taskLine);
            Epic epicAfterReading = (Epic) filedBackedTaskManager.taskFromString(epicLine);
            Subtask subtaskAfterReading = (Subtask) filedBackedTaskManager.taskFromString(subtaskLine);

            assertEquals(task1, taskAfterReading);
            assertEquals(epic1, epicAfterReading);
            assertEquals(subtask, subtaskAfterReading);

        } catch (FileNotFoundException e) {
            throw new ManagerSaveException("Ошибка - такого файла не существует!");
        } catch (IOException ex) {
            throw new ManagerSaveException("Ошибка при чтении файла! ");
        }
    }

    @Test
    void savedHistory() {
        filedBackedTaskManager.addNewTask(task1);
        filedBackedTaskManager.addNewEpic(epic1);
        Subtask subtask = new Subtask("Subtask1", "DescriptionSubtask1", epic1.getId());
        filedBackedTaskManager.addNewSubtask(subtask);

        filedBackedTaskManager.getTask(task1.getId());
        filedBackedTaskManager.getEpic(epic1.getId());
        filedBackedTaskManager.getSubtask(subtask.getId());

        //Get методы работают, история менеджера должна состоять из 3-х элементов
        assertEquals(3, filedBackedTaskManager.getHistory().size());

        //Создадим новый менеджер с другими патчами и проверм что там история будет пустая
        try {
            File newTaskFile = File.createTempFile("SaveTask", ".txt");
            File newHistoryFile = File.createTempFile("SaveHistory", ".txt");
            TaskManager newFiledManager = Managers.getFileBackedManager(newTaskFile.getPath(), newHistoryFile.getPath());
            assertEquals(0, newFiledManager.getHistory().size());
            //Передаем в пустой менеджер значение менеджера с информацией из файлов, в которые сохранены данные
            //История должна передать данные и теперь хранить 3 элемента
            TaskManager newManagerWithOldHistory = Managers.getFileBackedManager(taskFile.getPath(), historyFile.getPath());
            System.out.println(newManagerWithOldHistory.getHistory());
            assertEquals(3, newManagerWithOldHistory.getHistory().size());

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при создании нового менеджера для проверки истории! ");
        }


    }


}
