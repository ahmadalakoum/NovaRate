package com.example.currencyconverter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatHolder> {

    ArrayList<String> messages;

    public ChatAdapter(ArrayList<String> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_item, parent, false);
        return new ChatHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatHolder holder, int position) {
        holder.message.setText(messages.get(position));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class ChatHolder extends RecyclerView.ViewHolder {
        TextView message;

        public ChatHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.chatMessage);
        }
    }
}
