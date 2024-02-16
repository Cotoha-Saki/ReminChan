package xyz.cotoha.program.reminchan.database;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import xyz.cotoha.program.reminchan.R;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Message> messages = new ArrayList<>();

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_item, parent, false);
        return new MessageViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message currentMessage = messages.get(position);
        holder.messageTextView.setText(currentMessage.getContent());

        // メッセージビューのLayoutParamsを取得
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.messageTextView.getLayoutParams();

        // メッセージの時刻を設定
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String timeString = dateFormat.format(new Date(currentMessage.getTimestamp()));

        holder.messageTimeView.setText(timeString);

        // 既読ステータスの設定
        if (currentMessage.isUserMessage() && currentMessage.isSeen()) {
            holder.messageSeenView.setVisibility(View.VISIBLE);
        } else {
            holder.messageSeenView.setVisibility(View.GONE);
        }

        if (currentMessage.isUserMessage()) {
            // ユーザーのメッセージ: 右寄せ、BOTアイコンを非表示
            layoutParams.gravity = Gravity.END;
            holder.messageTextView.setBackgroundResource(R.drawable.message_background_user);
            holder.botIcon.setVisibility(View.GONE);
        } else {
            // BOTのメッセージ: 左寄せ、BOTアイコンを表示
            layoutParams.gravity = Gravity.START;
            holder.messageTextView.setBackgroundResource(R.drawable.message_background_bot);
            holder.botIcon.setVisibility(View.VISIBLE);
        }

        holder.messageTextView.setLayoutParams(layoutParams);
    }


    @Override
    public int getItemCount() {
        return messages.size();
    }

    // アクセス修飾子を public に変更
    public void setMessages(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;
        LinearLayout messageContainer; // メッセージのコンテナ
        ImageView botIcon; // BOTアイコンの追加

        TextView messageTimeView;
        ImageView messageSeenView;


        public MessageViewHolder(View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.text_message);
            messageContainer = itemView.findViewById(R.id.message_container);
            botIcon = itemView.findViewById(R.id.bot_icon); // idをレイアウトから取得
            messageTimeView = itemView.findViewById(R.id.text_message_time);
            messageSeenView = itemView.findViewById(R.id.image_message_seen);
        }
    }
}
