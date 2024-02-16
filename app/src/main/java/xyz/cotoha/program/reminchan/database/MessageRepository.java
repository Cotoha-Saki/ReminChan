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

    public LiveData<List<Message>> getAllMessages() {
        return allMessages;
    }

    public LiveData<Message> getLastUserMessage() {
        return messageDao.getLastUserMessage();
    }

    public void insert(Message message) {
        new insertAsyncTask(messageDao).execute(message);
    }

    public void update(Message message) {
        new updateAsyncTask(messageDao).execute(message);
    }

    public void delete(Message message) {
        new deleteAsyncTask(messageDao).execute(message);
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

    private static class updateAsyncTask extends AsyncTask<Message, Void, Void> {
        private MessageDao asyncTaskDao;

        updateAsyncTask(MessageDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Message... params) {
            asyncTaskDao.update(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Message, Void, Void> {
        private MessageDao asyncTaskDao;

        deleteAsyncTask(MessageDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Message... params) {
            asyncTaskDao.delete(params[0]);
            return null;
        }
    }
}
