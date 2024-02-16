package xyz.cotoha.program.reminchan.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "message_table")
public class Message {
    @PrimaryKey(autoGenerate = true)
    public int id;

    private boolean seen;

    public String content; // メッセージの内容
    public String messageType; // メッセージタイプ（"text", "image", "video"）
    public long timestamp; // 送信日時（エポックタイム）
    public long reminderTime; // リマインダー日時（エポックタイム）
    public boolean isReminderLoop; // リマインダーのループ設定
    public boolean isUserMessage; // ユーザーが送信したメッセージかどうか



    // コンストラクタ
    public Message(String content, String messageType, long timestamp, long reminderTime, boolean isReminderLoop, boolean isUserMessage) {
        this.content = content;
        this.messageType = messageType;
        this.timestamp = timestamp;
        this.reminderTime = reminderTime;
        this.isReminderLoop = isReminderLoop;
        this.isUserMessage = isUserMessage;
    }

    // ゲッター

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }
    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getMessageType() {
        return messageType;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getReminderTime() {
        return reminderTime;
    }

    public boolean isReminderLoop() {
        return isReminderLoop;
    }

    public boolean isUserMessage() {
        return isUserMessage;
    }

    // セッター
    public void setId(int id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setReminderTime(long reminderTime) {
        this.reminderTime = reminderTime;
    }

    public void setReminderLoop(boolean reminderLoop) {
        isReminderLoop = reminderLoop;
    }

    public void setUserMessage(boolean userMessage) {
        isUserMessage = userMessage;
    }
}

