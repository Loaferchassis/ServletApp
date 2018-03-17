public class TaskObject {
    Integer id;
    String userName;
    String task;
    Integer is_done;

    public TaskObject(Integer id, String userName, String task, Integer is_done) {
        this.id = id;
        this.userName = userName;
        this.task = task;
        this.is_done = is_done;
    }

    public Integer getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getTask() {
        return task;
    }

    public Integer getIs_done() {
        return is_done;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public void setIs_done(Integer is_done) {
        this.is_done = is_done;
    }
}
