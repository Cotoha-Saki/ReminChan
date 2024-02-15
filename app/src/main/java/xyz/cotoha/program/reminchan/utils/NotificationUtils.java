package xyz.cotoha.program.reminchan.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import java.util.Random;

import xyz.cotoha.program.reminchan.R;

public class NotificationUtils {
    private Context context;

    public NotificationUtils(Context context) {
        this.context = context;
    }

    public void showPopupNotification(String message) {
        String channelId = "default_channel_id";
        String channelDescription = "Default Channel";


        // 通知チャネルをシステムに登録（Android 8.0以上必要）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelDescription, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(channelDescription);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // 通知の権限があるかをチェック
        if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            // 通知を構築
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.drawable.ic_anya_two)
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true);

            // 通知を表示
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // 通知の権限がない場合は、権限リクエストのロジックを追加
                // ...
                return;
            }
            NotificationManagerCompat.from(context).notify(new Random().nextInt(), builder.build());
        } else {
            // 通知の権限がない場合、ユーザーに権限を要請するための設定画面を開く
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
            context.startActivity(intent);
        }

        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }
        this.context = context;
    }
}
