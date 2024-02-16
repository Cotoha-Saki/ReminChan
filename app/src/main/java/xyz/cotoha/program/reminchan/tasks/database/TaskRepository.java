package xyz.cotoha.program.reminchan.tasks.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import xyz.cotoha.program.reminchan.database.AppDatabase;

public class TaskRepository {
    private TaskDao taskDao;
    private LiveData<List<Task>> allTasks;

    TaskRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        taskDao = db.taskDao();
        allTasks = taskDao.getAllTasks();
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public void insert(Task task) {
        new InsertAsyncTask(taskDao).execute(task);
    }

    public void update(Task task) {
        new UpdateAsyncTask(taskDao).execute(task);
    }

    public void delete(Task task) {
        new DeleteAsyncTask(taskDao).execute(task);
    }

    private static class InsertAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao asyncTaskDao;

        InsertAsyncTask(TaskDao dao) {
            this.asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Task... params) {
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao asyncTaskDao;

        UpdateAsyncTask(TaskDao dao) {
            this.asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Task... params) {
            asyncTaskDao.update(params[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao asyncTaskDao;

        DeleteAsyncTask(TaskDao dao) {
            this.asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Task... params) {
            asyncTaskDao.delete(params[0]);
            return null;
        }
    }
}
