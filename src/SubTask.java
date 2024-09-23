public class SubTask extends Task{
    private EpicTask epicTask;

    SubTask(long id, String name, String description) {
        super(id, name, description);
        nameTaskType = "SubTask";
    }

    public EpicTask getEpicTask() {
        return epicTask;
    }

    public void setEpicTask(EpicTask epicTask) {
        this.epicTask = epicTask;
    }

    @Override
    public void setStatus(Status status) {
        super.setStatus(status);
        epicTask.updateStatus();
    }

    @Override
    public String toString() {
        return super.toString() + "\n" + "\towned: EpicTask Id#" + epicTask.getId();
    }

}
