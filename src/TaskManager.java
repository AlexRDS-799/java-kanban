import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager{
    HashMap<Integer, Task> Tasks = new HashMap<>();
    HashMap<Integer, Epic> Epics = new HashMap<>();
    HashMap<Integer, ArrayList<Subtask>> Subtasks = new HashMap<>(); //подзадачи только в эпике, поэтмоу ключом используем эпик задачу

    //Сохранить задачу
    public void addTask(Task task){
        Tasks.put(task.hashCode(), task);
    }

    public void addEpic(Epic epic){
        Epics.put(epic.hashCode(), epic);
    }

    public void addSubtask(int identifier, Subtask subtask){ //identifier - hashCode объекта Epic,
       ArrayList<Subtask> subtasks = new ArrayList<>();      //к которому относится подзадача subtask

       if(Subtasks.containsKey(identifier)) {
            Subtasks.get(identifier).add(subtask);
        }
        else {
            subtasks.add(subtask);
            Subtasks.put(identifier, subtasks);
        }
    }

    //Вывести список задач
    public ArrayList<String> tasksList(){
        ArrayList<String> taskList = new ArrayList<>();
        for(Task task: Tasks.values()){
            taskList.add(task.getTask());
        }
        return taskList;
    }

    public ArrayList<String> epicsList(){
        ArrayList<String> epicList = new ArrayList<>();
        for(Epic epic: Epics.values()){
            epicList.add(epic.getEpic());
        }
        return epicList;
    }

    public ArrayList<String> subtaskList(){
       ArrayList<String> subtasksList = new ArrayList<>();
        for (int identifier: Subtasks.keySet()) {
            ArrayList<Subtask> subtasks = Subtasks.get(identifier);
            for (Subtask subtask : subtasks) {
                    subtasksList.add(subtask.getSubTask());
            }
        }
        return subtasksList;
    }

    public ArrayList<String> subtasksInTheEpic(int identifierEpic){
        ArrayList<Subtask> subtasks = Subtasks.get(identifierEpic);
        ArrayList<String> subtasksList = new ArrayList<>();
        for (Subtask subtask: subtasks){
            subtasksList.add(subtask.getSubTask());
        }
        return subtasksList;
    }

    //Удалить задачи
    public void clearTasks(){
    Tasks.clear();
    }

    public void clearEpics(){  //если удалят эпики, удалятся и их подзадачи
    Epics.clear();
    Subtasks.clear();
    }

    public void clearSubtasks(){
    Subtasks.clear();
    }

    //Удалить задачу по идентификатору
    public void removeTask(int identifier){
        Tasks.remove(identifier);
    }

    public void removeEpic(int identifier){
        Epics.remove(identifier);
        Subtasks.remove(identifier);
    }

    public void removeSubtask(int identifierEpic, Subtask subtaskToDelete){
        ArrayList<Subtask> subtasks = Subtasks.get(identifierEpic);
        if(subtasks != null) {
            int index = subtasks.indexOf(subtaskToDelete);
            if (index != -1){
                subtasks.remove(index);
            }
        }

    }

    //Вывести задачу по идентефикатору
    public Task getTask(int identifier){
       return Tasks.get(identifier);
    }

    public Epic getEpic(int identifier){
       return Epics.get(identifier);
    }

    public Subtask getSubtask(int identifierEpic, int identifierSubtask){
        ArrayList<Subtask> subtasks = new ArrayList<>();
        subtasks =Subtasks.get(identifierEpic);
        Subtask subTask = new Subtask("");

        for (Subtask subtask : subtasks) {
            if (subtask.hashCode() == identifierSubtask) {
                subTask = subtask;
                break;
            }
        }
        return subTask;
    }

    //Создать задачу
    public static Task createTask(String taskName){
        return new Task(taskName);
    }

    public static Epic createEpic(String epicName){
        return new Epic(epicName);
    }

    public static Subtask createSubtask(String subtaskName){
        return new Subtask(subtaskName);
    }

    //Обновить задачу
    public void updateTask(int identifierOldTask, Task newTask){
        Tasks.put(identifierOldTask, newTask);
    }

    public void updateEpic(int identifierOldEpic, Epic newEpic){
        Epics.put(newEpic.hashCode(), newEpic);
        Epics.remove(identifierOldEpic);
        Subtasks.remove(identifierOldEpic);
    }

    public void updateSubtask(int identifierEpic,Subtask oldSubtask, Subtask newSubtask){
        ArrayList<Subtask> subtasks = Subtasks.get(identifierEpic);
        if(subtasks != null) {
            int index = subtasks.indexOf(oldSubtask);
            if (index != -1){
                subtasks.set(index, newSubtask);
            }
        }
    }

    //Статус задач
    public Status taskStatus(Task task){
        return task.getStatus();
    }

    public Status epicStatus(Epic epic){
        ArrayList<Subtask> subtasks = Subtasks.get(epic.hashCode());
        Status status = Status.NEW;
        for (Subtask subtask: subtasks){
            if(subtask.getStatus() == Status.IN_PROGRESS){
                status = Status.IN_PROGRESS;
                break;
            }else if (subtask.getStatus() == Status.DONE){
                status = Status.DONE;
            }
        }
        epic.setStatus(status);
        return epic.getStatus();
    }

    public Status subtaslStatus(Subtask subtask){
        return subtask.getStatus();
    }
}
