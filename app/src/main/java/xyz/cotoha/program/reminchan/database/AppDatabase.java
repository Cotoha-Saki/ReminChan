package xyz.cotoha.program.reminchan.database;

import static androidx.work.impl.WorkDatabaseMigrations.MIGRATION_1_2;
import static androidx.work.impl.WorkDatabaseMigrations.MIGRATION_3_4;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import xyz.cotoha.program.reminchan.tasks.database.Task;
import xyz.cotoha.program.reminchan.tasks.database.TaskDao;

@Database(entities = {Message.class, Task.class}, version = 4, exportSchema = false) // versionを上げ、Taskクラスをentitiesに追加
public abstract class AppDatabase extends RoomDatabase {

    public abstract MessageDao messageDao();
    public abstract TaskDao taskDao(); // TaskDaoの抽象メソッドを追加

    private static volatile AppDatabase INSTANCE; // volatileキーワードを追加してインスタンスのスレッドセーフを保証

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "app_database")
                            .addMigrations(MIGRATION_3_4) // 新しいマイグレーションを追加
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // スキーマバージョン2から3へのマイグレーションを追加する
    static final Migration MIGRATION_3_4 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Taskテーブルを作成するSQLコマンド
            database.execSQL("CREATE TABLE IF NOT EXISTS `task_table` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `content` TEXT, `timestamp` INTEGER, `isReminderLoop` INTEGER NOT NULL, `reminderTime` INTEGER)");
        }
    };

}

