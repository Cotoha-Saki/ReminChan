package xyz.cotoha.program.reminchan.tasks.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    void insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

    @Query("SELECT * FROM tasks ORDER BY timestamp DESC")
    LiveData<List<Task>> getAllTasks();

    // 特定の条件に基づいてタスクを取得するその他のクエリをここに追加できます
}
