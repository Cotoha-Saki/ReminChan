package xyz.cotoha.program.reminchan.database;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

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

        // メッセージがユーザーのものかBOTのものかに応じてビューの位置を設定する
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        if (currentMessage.isUserMessage()) {
            // ユーザーのメッセージ: 右寄せ
            params.gravity = Gravity.END;
            holder.messageTextView.setLayoutParams(params);
            // 背景、マージン、その他のスタイリングを設定
        } else {
            // BOTのメッセージ: 左寄せ
            params.gravity = Gravity.START;
            holder.messageTextView.setLayoutParams(params);
            // 背景、マージン、その他のスタイリングを設定
        }
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
        private TextView messageTextView;

        public MessageViewHolder(View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.text_message);
        }
    }
}
