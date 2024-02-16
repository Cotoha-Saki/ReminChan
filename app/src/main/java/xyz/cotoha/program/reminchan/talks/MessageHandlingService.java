package xyz.cotoha.program.reminchan.talks;

import android.content.Context;
import java.util.Date;
import xyz.cotoha.program.reminchan.database.Message;
import xyz.cotoha.program.reminchan.database.MessageViewModel;
import xyz.cotoha.program.reminchan.reminder.DateParser;

public class MessageHandlingService {
    private Context context;
    private MessageViewModel messageViewModel;

    public MessageHandlingService(Context context, MessageViewModel viewModel) {
        this.context = context;
        this.messageViewModel = viewModel;
    }

    public void handleUserMessage(String messageContent) {
        // ユーザーメッセージの保存処理
        Message userMessage = new Message(
                messageContent,
                "text",
                System.currentTimeMillis(),
                0, // リマインダー日時はここでは設定しない
                false,
                true // ユーザーメッセージフラグは true
        );
        messageViewModel.insert(userMessage);

        // 日時解析の試み
        Date reminderDate = DateParser.parseStringToDate(messageContent);
        if (reminderDate != null) {
            // 日時が正しく解析できた場合、通知タスクを設定
            setReminder(reminderDate, messageContent);
        } else if (messageContent.equalsIgnoreCase("キャンセル")) {
            // キャンセル処理
            handleCancellation();
        } else {
            // 日時指定の再要求
            requestDateTimeAgain();
        }
    }

    private void generateBotResponse() {
        String botResponse = "いつ通知しますか？";
        Message botMessage = new Message(botResponse, "text", System.currentTimeMillis(), 0, false, false);
        messageViewModel.insert(botMessage);
    }

    private void setReminder(Date reminderDate, String taskContent) {
        // リマインダー設定のロジックを実装
        String botResponse = taskContent + " を覚えたよ！";
        Message botMessage = new Message(botResponse, "text", System.currentTimeMillis(), 0, false, false);
        messageViewModel.insert(botMessage);
        // ここにリマインダー設定の具体的なコードを追加します
    }

    private void handleCancellation() {
        // キャンセル処理のロジックを実装
        String botResponse = "キャンセルしました。";
        Message botMessage = new Message(botResponse, "text", System.currentTimeMillis(), 0, false, false);
        messageViewModel.insert(botMessage);
    }

    private void requestDateTimeAgain() {
        // 日時指定の再要求メッージをBOTから送信
        String botResponse = "日時を指定してください。例: 2023年3月15日 14時";
        Message botMessage = new Message(botResponse, "text", System.currentTimeMillis(), 0, false, false);
        messageViewModel.insert(botMessage);
    }
}