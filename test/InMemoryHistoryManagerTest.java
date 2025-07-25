import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import com.yandex.app.service.Interfaces.TaskManager;
import com.yandex.app.service.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    static TaskManager taskManager;
    Task task1;
    Epic epic1;
    Subtask subtask1;
    Subtask subtask2;
    public DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @BeforeEach
    void beforeEach() {
        taskManager = Managers.getDefault();
        task1 = new Task("Name", "task1");
        task1.setDuration(Duration.ofMinutes(10));
        task1.setStartTime(LocalDateTime.parse("2000-01-01 00:00:00", formatter));

        taskManager.addNewTask(task1);
        epic1 = new Epic("Name", "epic1");

        taskManager.addNewEpic(epic1);
        subtask1 = new Subtask("Name", "subtask1", epic1.getId());
        subtask2 = new Subtask("Name", "subtask2", epic1.getId());

        subtask1.setDuration(Duration.ofMinutes(10));
        subtask1.setStartTime(LocalDateTime.parse("1900-01-01 00:00:00", formatter));

        subtask2.setDuration(Duration.ofMinutes(10));
        subtask2.setStartTime(LocalDateTime.parse("2100-01-01 00:00:00", formatter));

        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);

        taskManager.getEpic(epic1.getId());
        getTaskNumberTimes(task1, 7);
        taskManager.getSubtask(subtask1.getId());
        taskManager.getSubtask(subtask2.getId());
        getTaskNumberTimes(task1, 1);

    }

    @Test
    void historyEmpty() { //после удаления всех задач, история должна быть пустой
        taskManager.clearAllTasks();
        taskManager.clearAllEpics();
        taskManager.clearAllSubtasks();
        assertEquals(0, taskManager.getHistory().size());
    }

    @Test
    void repeatedTasksInHistory() { //Проверяем что после добавления 8 одинаковых тасков, в историю будет добавлен только 1
        assertEquals(4, taskManager.getHistory().size());
        assertEquals(epic1, taskManager.getHistory().getFirst()); //предыдущая версия epic1 равна epic хранящемуся в истории
        assertEquals(task1, taskManager.getHistory().getLast()); //сохранится только таск, вызваный последним
    }

    @Test
    void removeTaskFromHistory() {
        taskManager.deleteTask(task1.getId()); //после удаления таска, из истории удалится 1 объект
        assertEquals(3, taskManager.getHistory().size());
    }

    @Test
    void removeEpicFromHistory() {
        taskManager.deleteEpic(epic1.getId());//после удаления epic удалятся и сабтаски этого эпика(3 объекта)
        assertEquals(1, taskManager.getHistory().size());
    }

    @Test
    void removeSubtaskFromHistory() {
        taskManager.deleteSubtask(subtask1.getId());//удален 1 объект
        assertEquals(3, taskManager.getHistory().size());
    }

    @Test
    void startTimeInEpic() {
        LocalDateTime epic1Time = epic1.getStartTime();
        LocalDateTime subtask1Time = subtask1.getStartTime();
        //Проверяем что эпику присвоен минимальный startTime, а именно subtask1.getStartTime
        assertEquals(epic1Time, subtask1Time);
    }

    @Test
    void getTasksForStartTime() {
        /* В результате сортировки по времени задачи должны расположиться так: сабтаск1,таск1,сабтаск2
        task1.getStartTime() = 2000-01-01 00:00:00
        epic1.getStartTime() = 1900-01-01 00:00:00 (минимальное среди сабтсасков)
        subtask1.getStartTime() = 1900-01-01 00:00:00
        subtask2.getStartTime() = 2100-01-01 00:00:00
        */
        LocalDateTime task1Time = task1.getStartTime();
        LocalDateTime subtask1Time = subtask1.getStartTime();
        LocalDateTime subtask2Time = subtask2.getStartTime();

        Set<Task> tasksStartTime = taskManager.getPrioritizedTasks();
        List<Task> tasksPrior = new ArrayList<>(tasksStartTime);
        //Проверяем что задача epic1 с таким же startTime как у сабтаска не попала в сет
        assertEquals(3, tasksPrior.size());
        //Проверяем что задачи отсортированы по startTime
        assertEquals(tasksPrior.get(0).getStartTime(), subtask1Time);
        assertEquals(tasksPrior.get(1).getStartTime(), task1Time);
        assertEquals(tasksPrior.get(2).getStartTime(), subtask2Time);
    }

    public void getTaskNumberTimes(Task task, int times) {
        for (int i = 1; i <= times; i++) {
            taskManager.getTask(task.getId());
        }
    }

}