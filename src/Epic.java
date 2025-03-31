import java.util.Objects;

public class Epic{
    private final String epic;
    private Status status = Status.NEW;

    public Epic(String epic){
        this.epic = epic;
    }

    public String getEpic(){
        return epic;
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

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;
        Epic otherEpic = (Epic) o;
        return Objects.equals(epic, otherEpic.epic );
    }

    @Override
    public int hashCode(){
        return epic.hashCode();
    }
}
