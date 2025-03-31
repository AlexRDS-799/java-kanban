import java.util.Objects;

public class Subtask {

   private final String subtask;
   //private final int identifier;

    public Subtask(String subtask){

        this.subtask = subtask;
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
        return Objects.hash(subtask);
    }

}
