import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager{
    HashMap<Integer, Task> Tasks = new HashMap<>();
    HashMap<Integer, Epic> Epics = new HashMap<>();
    HashMap<Epic, ArrayList<Subtask>> Subtasks = new HashMap<>(); //подзадачи только в эпике, поэтмоу ключом используем эпик задачу

    public TaskManager(){

    }

    public void addTask(Task task){
        Tasks.put(task.getTask().hashCode(), task);
    }

    public void addEpic(Epic epic){
        Epics.put(epic.getEpic().hashCode(), epic);
    }

    public void addSubtask(Epic epic, Subtask subtask){
       ArrayList<Subtask> subtasks = new ArrayList<>();
        if(Subtasks.containsKey(epic)) {
            Subtasks.get(epic).add(subtask);
        }
        else {
            System.out.println("else");
            subtasks.add(subtask);
            Subtasks.put(epic, subtasks);
        }
    }

    public void TasksList(){
        System.out.println("Список задач: ");
        for(Task task: Tasks.values()){
            System.out.println(task.getTask());
        }
    }

    public void EpicList(){
        System.out.println("Список эпиков: ");
        for(Epic epic: Epics.values()){
            System.out.println(epic.getEpic());
        }
    }

    public void SubtaskList(){
        System.out.println("Список подзадач");
        for (Epic epic: Subtasks.keySet()) {
            System.out.println(epic.getEpic() + ": ");
            for (ArrayList<Subtask> subtasks : Subtasks.values()) {
                for (Subtask subtask : subtasks) {
                    System.out.println("->" + subtask.getSubTask());
                }
            }
        }
    }

//    @Override
//    public String toString(){
//        return
//    }

}
