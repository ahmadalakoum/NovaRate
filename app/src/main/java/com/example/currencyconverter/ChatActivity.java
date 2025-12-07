package com.example.currencyconverter;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    RecyclerView chatList;
    ChatAdapter adapter;
    ArrayList<String> msgList = new ArrayList<>();
    EditText txtMsg;
    ImageButton btnSend;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_chat);

        // toolbar setup
        MaterialToolbar tb = findViewById(R.id.chatToolbar);
        setSupportActionBar(tb);
        tb.setNavigationOnClickListener(v -> finish());

        // recycler
        chatList = findViewById(R.id.chatRecycler);
        chatList.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ChatAdapter(msgList);
        chatList.setAdapter(adapter);

        // inputs
        txtMsg = findViewById(R.id.inputMessage);
        btnSend = findViewById(R.id.sendButton);

        // send button
        btnSend.setOnClickListener(v -> {
            String m = txtMsg.getText().toString().trim();

            // add message to array + update ui
            if (!m.isEmpty()) {
                msgList.add(m);
                adapter.notifyItemInserted(msgList.size() - 1);
                chatList.scrollToPosition(msgList.size() - 1);
                txtMsg.setText("");
            }
        });
    }
}
