package com.sayan.simplechatapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatBoxAdapter extends RecyclerView.Adapter <ChatBoxAdapter.ViewHolder>{

    private List<Message> messageList;

    public ChatBoxAdapter(List<Message> messageList) {

        this.messageList = messageList;

    }

    @NonNull
    @Override
    public ChatBoxAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item,parent,false);
        return new ChatBoxAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatBoxAdapter.ViewHolder holder, int position) {
        Message m = messageList.get(position);
        holder.nickname.setText(m.getNickName());
        holder.message.setText(m.getMessage());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nickname;
        public TextView message;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nickname =  itemView.findViewById(R.id.nickname);
            message =  itemView.findViewById(R.id.message);

        }
    }
}
