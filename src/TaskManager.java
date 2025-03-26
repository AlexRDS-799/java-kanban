import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager{
    HashMap<Integer, Task> Tasks = new HashMap<>();
    HashMap<Integer, Epic> Epics = new HashMap<>();
    HashMap<Epic, Subtask> Subtasks = new HashMap<>(); //подзадачи только в эпике, поэтмоу ключом используем эпик задачу

    public TaskManager(){

    }

    public void addTask(Task task){
        Tasks.put(task.getTask().hashCode(), task);

    }

    public void addEpic(Epic epic){
        Epics.put(epic.getEpic().hashCode(), epic);
    }

    public void addSubtask(Epic epic, Subtask subtask){
        Subtasks.put(epic, subtask);
    }

    public void TasksList(){
        for(Task task: Tasks.values()){
            System.out.println(task.getTask());
        }
    }

    public void EpicList(){
        for(Epic epic: Epics.values()){
            System.out.println(epic.getEpic());
        }
    }

    public void SubtaskList(){
        for(Subtask subtask: Subtasks.values()){
            System.out.println(subtask.getSubTask());
        }
    }

    public String toString(){
        ArrayList<String> tasks = new ArrayList<>();
        for(Task task: Tasks.values()){
            task.getTask();
            tasks.add(task.getTask());
        }

        return "TaskManager \n" +
              "Задачи Tasks:" + tasks;
    }

}
