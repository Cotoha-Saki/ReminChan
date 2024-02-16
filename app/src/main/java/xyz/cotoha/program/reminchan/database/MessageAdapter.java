package xyz.cotoha.program.reminchan.database;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String timeString = dateFormat.format(new Date(currentMessage.getTimestamp()));
        holder.messageTimeView.setText(timeString);

        if (currentMessage.isUserMessage() && currentMessage.isSeen()) {
            holder.messageSeenView.setVisibility(View.VISIBLE);
        } else {
            holder.messageSeenView.setVisibility(View.INVISIBLE); // 既読マークを適切に表示/非表示
        }

        // ユーザーメッセージとBOTメッセージのレイアウト調整
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.messageContainer.getLayoutParams();
        if (currentMessage.isUserMessage()) {
            layoutParams.gravity = Gravity.END;
            holder.messageContainer.setBackgroundResource(R.drawable.message_background_user);
            holder.botIcon.setVisibility(View.GONE);
        } else {
            layoutParams.gravity = Gravity.START;
            holder.messageContainer.setBackgroundResource(R.drawable.message_background_bot);
            holder.botIcon.setVisibility(View.VISIBLE);
        }
        holder.messageContainer.setLayoutParams(layoutParams);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView, messageTimeView;
        ImageView messageSeenView, botIcon;
        LinearLayout messageContainer;

        public MessageViewHolder(View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.text_message);
            messageTimeView = itemView.findViewById(R.id.text_message_time);
            messageSeenView = itemView.findViewById(R.id.image_message_seen);
            botIcon = itemView.findViewById(R.id.bot_icon);
            messageContainer = itemView.findViewById(R.id.message_container);
        }
    }
}
