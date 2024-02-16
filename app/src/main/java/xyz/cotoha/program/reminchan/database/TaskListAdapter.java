package xyz.cotoha.program.reminchan.database;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import xyz.cotoha.program.reminchan.R;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskViewHolder> {
    private final MessageViewModel messageViewModel;
    private List<Message> tasks;

    public TaskListAdapter(MessageViewModel messageViewModel) {
        this.messageViewModel = messageViewModel;
    }

    public void setTasks(List<Message> tasks) {
        this.tasks = tasks;
        Log.d("TaskListAdapter", "setTasks called, tasks size: " + (tasks != null ? tasks.size() : "null")); // 追加
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        if (tasks != null) {
            Message currentTask = tasks.get(position);
            holder.taskTextView.setText(currentTask.getContent());
            holder.deleteButton.setOnClickListener(v -> messageViewModel.delete(currentTask));
        }
    }

    @Override
    public int getItemCount() {
        if (tasks != null) {
            return tasks.size();
        } else {
            return 0;
        }
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView taskTextView;
        private ImageButton deleteButton;

        TaskViewHolder(View itemView) {
            super(itemView);
            taskTextView = itemView.findViewById(R.id.task_name);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
}
