package cn.itcast.writeitdown;

public class todobean {
    private String id;
    private String todo;
    private String time;

    private String state;
    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public String getTodo() {
        return todo;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}

