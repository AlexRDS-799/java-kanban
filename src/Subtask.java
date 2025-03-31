import java.util.Objects;

public class Subtask {

   private final String subtask;
   private Status status = Status.NEW;

    public Subtask(String subtask){
        this.subtask = subtask;
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

    public String getSubTask(){
        return subtask;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;
        Subtask otherSubtask = (Subtask) o;
        return Objects.equals(subtask, otherSubtask.subtask );
    }

    @Override
    public int hashCode(){
        return subtask.hashCode();
    }

}
