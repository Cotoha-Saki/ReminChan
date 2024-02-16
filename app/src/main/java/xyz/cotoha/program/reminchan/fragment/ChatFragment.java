package xyz.cotoha.program.reminchan.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.PopupMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.Date;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Data;

import java.util.Locale;
import java.util.concurrent.TimeUnit;
import xyz.cotoha.program.reminchan.R;
import xyz.cotoha.program.reminchan.database.Message;
import xyz.cotoha.program.reminchan.database.MessageAdapter;
import xyz.cotoha.program.reminchan.database.MessageViewModel;
import xyz.cotoha.program.reminchan.reminder.DateParser;
import xyz.cotoha.program.reminchan.reminder.ReminderWorker;
import xyz.cotoha.program.reminchan.talks.MessageHandlingService;
import xyz.cotoha.program.reminchan.tasks.database.Task;
import xyz.cotoha.program.reminchan.tasks.database.TaskViewModel;
import java.text.SimpleDateFormat;
import java.util.Locale;


// ... その他のインポート

public class ChatFragment extends Fragment {

    private MessageViewModel messageViewModel;
    private TaskViewModel taskViewModel; // TaskViewModel を追加
    private MessageAdapter adapter;
    private RecyclerView recyclerView;
    private TaskViewModel mTaskViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // フラグメントのレイアウトをインフレート
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        // RecyclerViewのセットアップ
        recyclerView = view.findViewById(R.id.chat_recycler_view);
        adapter = new MessageAdapter(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // 最新のメッセージにスクロールするために、レイアウトマネージャーのスタック方向を設定
        layoutManager.setStackFromEnd(true);

        // 送信ボタンのリスナー設定
        ImageButton sendButton = view.findViewById(R.id.button_send);
        EditText editMessage = view.findViewById(R.id.edit_message);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageContent = editMessage.getText().toString().trim();
                if (!messageContent.isEmpty()) {
                    // メッセージ送信の処理（例: ViewModelを使用してデータベースに保存）
                    sendMessage(messageContent);
                    // 入力フィールドをクリア
                    editMessage.setText("");
                }
            }
        });
        // このフラグメントのルートビューを返す
        return view;
    }

    private void sendMessage(String messageContent) {
        // 現在時刻を取得
        long currentTime = System.currentTimeMillis();

        // Messageオブジェクトを作成
        Message newMessage = new Message(messageContent, "text", currentTime, false, true);

        // ViewModelを使用してデータベースにメッセージを挿入
        messageViewModel.insert(newMessage);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // レイアウトからコンポーネントを見つける
        ImageButton sendButton = view.findViewById(R.id.button_send);
        EditText editMessage = view.findViewById(R.id.edit_message);

        // ホームからチャットに遷移したとき、bottomNavigationを非表示にする
        hideBottomNavigation();

        // ツールバーのセットアップ
        setupToolbar(view);

        // RecyclerViewの設定
        setupRecyclerView(view);

        setupMessageInputAndSend(view);

        // ViewModelProviderを使用してMessageViewModelのインスタンスを取得する
        messageViewModel = new ViewModelProvider(this).get(MessageViewModel.class);

        // TaskViewModel の初期化
        mTaskViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())).get(TaskViewModel.class);

        // MessageHandlingServiceのインスタンス化
        MessageHandlingService messageService = new MessageHandlingService(getContext(), messageViewModel);

        // 送信ボタンのクリックリスナー
        sendButton.setOnClickListener(v -> {
            String messageContent = editMessage.getText().toString().trim();
            if (!messageContent.isEmpty()) {
                messageService.handleUserMessage(messageContent);
                editMessage.setText("");
            }
        });

        // ... その他のメソッド
    }

    private void displayTaskList() {
        taskViewModel.getAllTasks().observe(getViewLifecycleOwner(), tasks -> {
            if (tasks.isEmpty()) {
                sendBotResponse("タスクがありません。");
            } else {
                StringBuilder taskListStringBuilder = new StringBuilder("以下が現在設定されているタスクです：\n\n");
                for (Task task : tasks) {
                    taskListStringBuilder.append("・ ")
                            .append(task.getContent())
                            .append(task.isReminderLoop() ? " (ループ)\n" : "\n");
                }
                sendBotResponse(taskListStringBuilder.toString());
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        // アダプタのアイテム数が0より大きい場合にのみスクロール
        if (adapter.getItemCount() > 0) {
            recyclerView.scrollToPosition(adapter.getItemCount() - 1);
        }
    }


    // Toolbarのセットアップとメニューボタンのリスナー設定
    private void setupToolbar(View view) {
        androidx.appcompat.widget.Toolbar toolbar = view.findViewById(R.id.chat_toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            // BottomNavigationViewでホームを選択することでホームに遷移する
            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.nav_view);
            bottomNavigationView.setSelectedItemId(R.id.navigation_home); // ここで指定するIDはホームを示すメニューアイテムのIDです
        });

        ImageButton kebabMenuButton = toolbar.findViewById(R.id.menu_kebab);
        kebabMenuButton.setOnClickListener(this::showPopupMenu);
    }



    private void showPopupMenu(View view) {
        PopupMenu popup = new PopupMenu(getActivity(), view);
        popup.getMenuInflater().inflate(R.menu.chat_options_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.action_delete_chat) {
                // チャット履歴を削除する処理
                return true;
            } else if (id == R.id.action_clear_tasks_data) {
                // タスクデータをクリアする処理
                return true;
            }
            return false;
        });
        popup.show();
    }



    // BottomNavigationView を非表示にする
    private void hideBottomNavigation() {
        if (getActivity() != null) {
            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.nav_view);
            if (bottomNavigationView != null) {
                bottomNavigationView.setVisibility(View.GONE);
            }
        }
    }


    private void showBottomNavigation() {
        if (getActivity() != null) {
            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.nav_view);
            bottomNavigationView.setVisibility(View.VISIBLE); // BottomNavigationViewを表示する
        }
    }



    private void setupRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.chat_recycler_view);
        adapter = new MessageAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        // その他のRecyclerViewの設定...
    }

    private void parseReminderDate(String message) {
        Date date = DateParser.parseStringToDate(message);
        if (date != null) {
            setReminder(date.getTime());
        } else {
            sendBotResponse("指定された日時が解析できませんでした。もう一度試してください。");
        }
    }

    private void setReminder(long reminderTime) {
        long delay = reminderTime - System.currentTimeMillis();
        if (delay > 0) {
            // リマインダー設定のための通知をスケジュールするロジックをここに記述します。
            // 以下は実装の例ですが、実際の通知設定方法によって異なります。
            Data data = new Data.Builder()
                    .putString("message", "リマインダーの時間です！")
                    .build();
            OneTimeWorkRequest reminderWork = new OneTimeWorkRequest.Builder(ReminderWorker.class)
                    .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                    .setInputData(data)
                    .build();
            WorkManager.getInstance(requireContext()).enqueue(reminderWork);
        } else {
            sendBotResponse("過去の日時にはリマインダーを設定できません。");
        }
    }
    private void setupMessageInputAndSend(View view) {
        EditText editMessage = view.findViewById(R.id.edit_message);
        ImageButton sendButton = view.findViewById(R.id.button_send);

        sendButton.setOnClickListener(v -> {
            String messageContent = editMessage.getText().toString().trim();
            if (!messageContent.isEmpty()) {
                handleUserMessage(messageContent);
                editMessage.setText("");
            }
        });
    }


    private void handleUserMessage(String messageContent) {
        Date parsedDate = DateParser.parseStringToDate(messageContent);
        if (parsedDate != null) {
            // 日時が解析できた場合
            setReminderForMessage(parsedDate);
            sendBotResponse("「" + messageContent + "」を" + new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault()).format(parsedDate) + "に設定しました。");
        } else {
            // 日時解析に失敗した場合
            if (messageContent.equalsIgnoreCase("キャンセル")) {
                // キャンセル処理
                sendBotResponse("タスク設定をキャンセルしました。");
            } else {
                // ユーザーに日時の再入力を促す
                sendBotResponse("日時を「明日の午後3時」のように入力してください。");
            }
        }
    }

    private void setReminderForMessage(Date date) {
        long delay = date.getTime() - System.currentTimeMillis(); // リマインダーまでの遅延時間を計算
        if (delay > 0) {
            // リマインダー情報をWorkRequestに渡すためのDataオブジェクトを作成
            Data data = new Data.Builder()
                    .putString("message", "リマインダーの時間です！") // 通知に表示するメッセージ
                    .build();

            // OneTimeWorkRequestを作成
            OneTimeWorkRequest reminderWork = new OneTimeWorkRequest.Builder(ReminderWorker.class)
                    .setInitialDelay(delay, TimeUnit.MILLISECONDS) // 計算した遅延時間を設定
                    .setInputData(data) // データをWorkRequestに渡す
                    .build();

            // WorkManagerにWorkRequestをスケジュール
            WorkManager.getInstance(requireContext()).enqueue(reminderWork);
        } else {
            // 時間が過去の場合はエラーメッセージを表示するなどの処理
            Log.e("setReminderForMessage", "指定された時間が過去です。");
        }
    }

    private void setRepeatingReminder(Date date) {
        // 毎日繰り返す間隔を設定
        long repeatInterval = 24 * 60; // 24時間（分単位）

        // 初回のリマインダーの遅延時間を計算（ミリ秒単位）
        long initialDelay = date.getTime() - System.currentTimeMillis();

        // 繰り返しのWorkRequestを作成
        PeriodicWorkRequest periodicReminderWork =
                new PeriodicWorkRequest.Builder(ReminderWorker.class, repeatInterval, TimeUnit.MINUTES)
                        .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS) // 初回の遅延時間を設定
                        .build();

        // WorkManagerにWorkRequestをスケジュール
        WorkManager.getInstance(getContext()).enqueue(periodicReminderWork);
    }

    private void sendBotResponse(String response) {
        long currentTime = System.currentTimeMillis();
        Message botMessage = new Message(response, "text", currentTime, 0, false, false);
        messageViewModel.insert(botMessage);
    }
}
