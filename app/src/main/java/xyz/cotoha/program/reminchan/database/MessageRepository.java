package xyz.cotoha.program.reminchan.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class MessageRepository {
    private MessageDao messageDao;
    private LiveData<List<Message>> allMessages;

    MessageRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        messageDao = db.messageDao();
        allMessages = messageDao.getAllMessages();
    }

    LiveData<List<Message>> getAllMessages() {
        return allMessages;
    }

    public void insert(Message message) {
        new insertAsyncTask(messageDao).execute(message);
    }

    private static class insertAsyncTask extends AsyncTask<Message, Void, Void> {
        private MessageDao asyncTaskDao;

        insertAsyncTask(MessageDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Message... params) {
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }

    // 必要に応じて他の操作（update, delete等）のためのメソッドを追加
}
