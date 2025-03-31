import java.util.Objects;

public class Task {
    private final String task;
    private Status status = Status.NEW;

    public Task(String task){
        this.task = task;
    }

    public void setStatus(Status newStatus){
        switch (newStatus){
            case NEW:
                this.status = newStatus;
                break;
            case IN_PROGRESS:
                this.status = newStatus;
                break;
            case DONE:
                this.status = newStatus;
                break;
            default:
                System.out.println("Invalid status");

        }
    }

    public Status getStatus(){
        return status;
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
        return task.hashCode();
    }
}
