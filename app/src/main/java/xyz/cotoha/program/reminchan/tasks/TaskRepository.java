package xyz.cotoha.program.reminchan.tasks;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import xyz.cotoha.program.reminchan.database.AppDatabase;
import xyz.cotoha.program.reminchan.tasks.database.Task;
import xyz.cotoha.program.reminchan.tasks.database.TaskDao;

public class TaskRepository {
    private TaskDao taskDao;
    private LiveData<List<Task>> allTasks;

    // コンストラクタでデータベースインスタンスとDAOを取得
    TaskRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        taskDao = db.taskDao();
        allTasks = taskDao.getAllTasks();
    }

    // すべてのタスクを取得する
    LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    // 新しいタスクを挿入する（非同期）
    public void insert(Task task) {
        new insertAsyncTask(taskDao).execute(task);
    }

    // タスクを更新する（非同期）
    public void update(Task task) {
        new updateAsyncTask(taskDao).execute(task);
    }

    // タスクを削除する（非同期）
    public void delete(Task task) {
        new deleteAsyncTask(taskDao).execute(task);
    }

    // 非同期処理のための内部クラス
    private static class insertAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao asyncTaskDao;

        insertAsyncTask(TaskDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Task... params) {
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao asyncTaskDao;

        updateAsyncTask(TaskDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Task... params) {
            asyncTaskDao.update(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao asyncTaskDao;

        deleteAsyncTask(TaskDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Task... params) {
            asyncTaskDao.delete(params[0]);
            return null;
        }
    }
}
