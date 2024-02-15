package xyz.cotoha.program.reminchan.reminder;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import javax.xml.transform.Result;

public class ReminderWorker extends Worker {

    public ReminderWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        // 通知を表示するロジックをここに実装
        // 例: NotificationCompat.Builderを使用して通知を作成し、NotificationManagerCompatで表示

        return Result.success(); // 作業が成功したことを示す
    }
}
