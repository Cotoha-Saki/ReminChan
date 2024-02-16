package xyz.cotoha.program.reminchan.tasks.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    private TaskRepository mRepository;
    private LiveData<List<Task>> mAllTasks;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        mRepository = new TaskRepository(application);
        mAllTasks = mRepository.getAllTasks();
    }

    public LiveData<List<Task>> getAllTasks() {
        return mAllTasks;
    }

    public void insert(Task task) {
        mRepository.insert(task);
    }
}
