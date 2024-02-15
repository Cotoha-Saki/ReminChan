package xyz.cotoha.program.reminchan.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuItem;
import androidx.appcompat.widget.PopupMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.List;
import com.joestelmach.natty.Parser;
import com.joestelmach.natty.DateGroup;
import java.util.Date;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Data;
import java.util.concurrent.TimeUnit;
import xyz.cotoha.program.reminchan.R;
import xyz.cotoha.program.reminchan.database.Message;
import xyz.cotoha.program.reminchan.database.MessageAdapter;
import xyz.cotoha.program.reminchan.database.MessageViewModel;
import xyz.cotoha.program.reminchan.reminder.ReminderWorker;

public class ChatFragment extends Fragment {

    private MessageViewModel messageViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        setupToolbar(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hideBottomNavigation();

        RecyclerView recyclerView = view.findViewById(R.id.chat_recycler_view);
        final MessageAdapter adapter = new MessageAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true); // 追加: リストを逆順に表示
        layoutManager.setStackFromEnd(true); // 追加: 新しいメッセージをリストの下部から開始
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        messageViewModel = new ViewModelProvider(this).get(MessageViewModel.class);
        messageViewModel.getAllMessages().observe(getViewLifecycleOwner(), new Observer<List<Message>>() {
            @Override
            public void onChanged(@Nullable final List<Message> messages) {
                // Update the cached copy of the messages in the adapter.
                adapter.setMessages(messages);
            }
        });

        ImageButton sendButton = view.findViewById(R.id.button_send);
        EditText editMessage = view.findViewById(R.id.edit_message);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageContent = editMessage.getText().toString();
                if (!messageContent.isEmpty()) {
                    long currentTime = System.currentTimeMillis();
                    Message message = new Message(messageContent, "text", currentTime, 0, false, true);
                    messageViewModel.insert(message);
                    editMessage.setText("");

                    simulateBotResponse();
                }
            }
        });
    }


    private void setupToolbar(View view) {
        androidx.appcompat.widget.Toolbar toolbar = view.findViewById(R.id.chat_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ツールバーのナビゲーションアイコンがクリックされたときの処理
                if (getActivity() != null) {
                    BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.nav_view);
                    bottomNavigationView.setSelectedItemId(R.id.navigation_home);
                }
            }
        });

        ImageButton kebabMenu = view.findViewById(R.id.menu_kebab);
        kebabMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });
    }


    private void showPopupMenu(View view) {
        PopupMenu popup = new PopupMenu(getActivity(), view);
        popup.getMenuInflater().inflate(R.menu.chat_options_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.action_delete_chat) {
                    // チャット履歴を削除するロジック
                    return true;
                } else if (id == R.id.action_clear_tasks_data) {
                    // データを削除するロジック
                    return true;
                }
                return false;
            }
        });
        popup.show();
    }


    private void hideBottomNavigation() {
        if (getActivity() != null) {
            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.nav_view);
            bottomNavigationView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        showBottomNavigation();
    }

    private void showBottomNavigation() {
        if (getActivity() != null) {
            BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.nav_view);
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }

    private void simulateBotResponse() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // ここでBOTのレスポンスを生成して挿入
                String botResponse = "リマインダーの設定日時を教えてください。";
                long currentTime = System.currentTimeMillis();
                Message botMessage = new Message(botResponse, "text", currentTime, 0, false, false);
                messageViewModel.insert(botMessage);
            }
        }, 2000); // 2秒後に実行
    }

    private void parseReminderDate(String message) {
        Parser parser = new Parser();
        List<DateGroup> groups = parser.parse(message);

        long reminderTime = 0; // 初期値として0を設定

        if (!groups.isEmpty()) {
            DateGroup group = groups.get(0); // 最初のDateGroupを取得
            List<Date> dates = group.getDates();

            if (!dates.isEmpty()) {
                Date date = dates.get(0); // 最初に見つかった日時を取得
                reminderTime = date.getTime(); // エポックタイム（ミリ秒）に変換
            }
        }

        if (reminderTime > 0) {
            // 解析した日時でリマインダーを設定
            setReminder(reminderTime);
        } else {
            // 日時が正しく解析できなかった場合の処理（エラーメッセージの表示など）
        }
    }

    private void setReminder(long reminderTime) {
        long delay = reminderTime - System.currentTimeMillis(); // リマインダーまでの遅延時間を計算
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
            // WorkManagerのインスタンスを取得する正しい方法
            WorkManager workManager = WorkManager.getInstance(requireContext().getApplicationContext());
        }
    }


}
