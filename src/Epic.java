import java.util.Objects;

public class Epic{
    private final String epic;

    public Epic(String epic){
        this.epic = epic;
    }

    public String getEpic(){
        return epic;
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
        return Objects.hash(epic);
    }
}
