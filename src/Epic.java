public class Epic extends Task{
    private final String epic;

    public Epic(String epic, String task){
        super(task);
        this.epic = epic;
    }

    public String getEpic(){
        return epic;
    }
}
