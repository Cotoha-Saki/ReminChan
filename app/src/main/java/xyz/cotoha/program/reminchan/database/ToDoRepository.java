package xyz.cotoha.program.reminchan.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ToDoRepository {
    private ToDoDao mToDoDao;
    private LiveData<List<ToDo>> mAllToDos;

    // コンストラクタ
    public ToDoRepository(Application application) {
        ToDoRoomDatabase db = ToDoRoomDatabase.getDatabase(application);
        mToDoDao = db.toDoDao();
        mAllToDos = mToDoDao.getAllToDos();
    }

    // すべてのToDoを取得
    LiveData<List<ToDo>> getAllToDos() {
        return mAllToDos;
    }

    // ToDoを非同期で追加
    public void insert(ToDo todo) {
        new insertAsyncTask(mToDoDao).execute(todo);
    }

    private static class insertAsyncTask extends AsyncTask<ToDo, Void, Void> {
        private ToDoDao mAsyncTaskDao;

        insertAsyncTask(ToDoDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final ToDo... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
