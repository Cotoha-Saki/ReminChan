package xyz.cotoha.program.reminchan.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ToDo.class}, version = 1, exportSchema = false)
public abstract class ToDoRoomDatabase extends RoomDatabase {
    public abstract ToDoDao toDoDao();

    private static volatile ToDoRoomDatabase INSTANCE;

    static ToDoRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ToDoRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    ToDoRoomDatabase.class, "todo_database")
                            // ここでマイグレーション処理やコールバックを設定する場合があります
                            .fallbackToDestructiveMigration() // マイグレーション戦略の一例
                            .build();

                }
            }
        }
        return INSTANCE;
    }
}
