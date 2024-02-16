package xyz.cotoha.program.reminchan.reminder;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import xyz.cotoha.program.reminchan.R;

public class ReminderWorker extends Worker {
    private static final String CHANNEL_ID = "reminder_channel";
    public ReminderWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        // 通知チャンネルの作成（API 26+）
        createNotificationChannel();

        // 通知を表示するロジック
        showNotification();

        return Result.success();
    }

    private void createNotificationChannel() {
        // 通知チャンネルを作成するためのメソッド
        // API 26 (Android 8.0)以上でのみ通知チャンネルが必要
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CharSequence name = getApplicationContext().getString(R.string.channel_name);
            String description = getApplicationContext().getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getApplicationContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showNotification() {
        // 通知を作成して表示するメソッド
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_icon) // アイコン設定
                .setContentTitle("リマインダー") // タイトル設定
                .setContentText("リマインダーの時間です") // テキスト設定
                .setPriority(NotificationCompat.PRIORITY_HIGH) // 重要度設定
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000}) // バイブレーションパターン設定
                .setAutoCancel(true); // タップ時に通知を自動的に閉じるよう設定

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(1, builder.build());
    }
}
