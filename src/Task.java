import java.util.Objects;

public class Task {
    private final String task;


    public Task(String task){
        this.task = task;
    }

    public String getTask(){
        return task;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;
        Task otherTask = (Task) o;
        return Objects.equals(task, otherTask.task );
    }

    @Override
    public int hashCode(){
        return Objects.hash(task);
    }
}
