public class SubTask extends Task{
    private EpicTask epicTask;

    SubTask(long id, String name, String description) {
        super(id, name, description);
    }

    public EpicTask getEpicTask() {
        return epicTask;
    }

    public void setEpicTask(EpicTask epicTask) {
        this.epicTask = epicTask;
    }

}
