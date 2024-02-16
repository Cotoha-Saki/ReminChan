package xyz.cotoha.program.reminchan.tasks.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class Task {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "content")
    private String content;

    @ColumnInfo(name = "timestamp")
    private long timestamp;

    @ColumnInfo(name = "isReminderLoop")
    private boolean isReminderLoop;

    // コンストラクタ
    public Task(String content, long timestamp, boolean isReminderLoop) {
        this.content = content;
        this.timestamp = timestamp;
        this.isReminderLoop = isReminderLoop;
    }

    // ゲッター
    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public boolean isReminderLoop() {
        return isReminderLoop;
    }

    // セッター
    public void setId(int id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setReminderLoop(boolean reminderLoop) {
        isReminderLoop = reminderLoop;
    }
}
