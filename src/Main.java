import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Scanner sc = new Scanner (System.in);
        Epic epic = new Epic("Постр дом");
        Epic epic1 = new Epic("123");

        Subtask subtask1 = new Subtask("Найти застройщикаа");
        Subtask subtask2 = new Subtask("Найти материал");
        Subtask subtask3 = new Subtask("риал");

        Task task = new Task("КУпить ноут");
        Task task1 = new Task("Buy note");

        taskManager.addEpic(epic);
        taskManager.addEpic(epic1);

        taskManager.addSubtask(epic, subtask1);
        taskManager.addSubtask(epic, subtask2);
        taskManager.addSubtask(epic1, subtask3);

        taskManager.addTask(task);
        taskManager.addTask(task1);

        taskManager.EpicList();
        taskManager.SubtaskList();
        taskManager.TasksList();

//        while(true){
//
//           System.out.println("enter task");
//           String tsk = sc.nextLine();
//            if(tsk.equals("")){
//                break;
//            }
//
//           System.out.println("enter subtask");
//           String subtsk = sc.nextLine();
//           if(subtsk.equals("")){
//               Task task = new Task(tsk);
//               taskManager.addTask(task);
//               System.out.println(taskManager);
//
//               taskManager.EpicList();
//               taskManager.SubtaskList();
//               taskManager.TasksList();
//           }else{
//               Epic epic = new Epic(tsk);
//               Subtask subtask = new Subtask(subtsk);
//               taskManager.addEpic(epic);
//               taskManager.addSubtask(epic, subtask);
//
//               System.out.println(taskManager);
//           }
//
//           taskManager.EpicList();
//           taskManager.SubtaskList();
//           taskManager.TasksList();
//
//        }

    }
}
