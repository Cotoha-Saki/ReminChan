package xyz.cotoha.program.reminchan.message;

import xyz.cotoha.program.reminchan.database.Message;

public class BotMessage extends Message {
    // BOTメッセージ専用のプロパティやメソッドをここに追加
    // この例では、基本的なMessageクラスを継承しています

    public BotMessage(String content, String type, long timestamp, boolean isBot) {
        super(content, type, timestamp, 0, isBot, false);
    }

    // 必要に応じて追加のメソッドやプロパティ
}
