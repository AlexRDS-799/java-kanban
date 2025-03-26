public class Subtask extends Task {

   private final String subtask;

    public Subtask(String task,String subtask){
        super(task);
        this.subtask = subtask;
    }

    public String getSubTask(){
        return subtask;
    }
}
