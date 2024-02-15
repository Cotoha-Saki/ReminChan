package xyz.cotoha.program.reminchan.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "todo_table")
public class ToDo {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String task;
    private String timestamp;

    // idのゲッター
    public int getId() {
        return id;
    }

    // idのセッター
    public void setId(int id) {
        this.id = id;
    }

    // taskのゲッター
    public String getTask() {
        return task;
    }

    // taskのセッター
    public void setTask(String task) {
        this.task = task;
    }

    // timestampのゲッター
    public String getTimestamp() {
        return timestamp;
    }

    // timestampのセッター
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

