package com.sayan.simplechatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class ChatBoxActivity extends AppCompatActivity {

    String name;
    private Socket socket;

    RecyclerView recyclerView;
    EditText messageText;
    Button send;
    List<Message> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_box);

        recyclerView=findViewById(R.id.messagelist);
        messageText = findViewById(R.id.message);
        send = findViewById(R.id.send);

        messageList=new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        Intent intent = getIntent();
       name= intent.getStringExtra("USERNICKNAME");

        Toast.makeText(this, name, Toast.LENGTH_SHORT).show();

        try {
            socket = IO.socket("http://192.168.170.105:3000/");
            socket.connect();

            socket.emit("join",name);

            socket.on("userjoinedthechat", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    String data =(String) args[0];
//                    Toast.makeText(ChatBoxActivity.this, data, Toast.LENGTH_SHORT).show();
                }
            });

        } catch (URISyntaxException e) {
            e.printStackTrace();
            Log.e("error_socket.io", "onCreate: "+e.getMessage());
            throw new RuntimeException(e);
        }

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!messageText.getText().toString().isEmpty()){
                    socket.emit("messagedetection",name,messageText.getText().toString());
                    messageText.setText("");
                }
            }
        });

        socket.on("output-message", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (args[0] instanceof JSONArray) {
                    // If it's a JSONArray, handle it accordingly
                    JSONArray jsonArray = (JSONArray) args[0];
                    // Log or process the JSONArray as needed
                    Log.d("DATABASE_CONTENT", jsonArray.toString());
                }
            }
        });

        socket.on("message", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (args[0] instanceof JSONObject) {
                    // If it's a JSONObject, process it accordingly
                    JSONObject object = (JSONObject) args[0];
                    try {
                        String nickName = object.getString("senderNickname");
                        String messageContent = object.getString("message");

                        Message m = new Message(nickName, messageContent);
                        messageList.add(m);

                        ChatBoxAdapter adapter = new ChatBoxAdapter(messageList);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.setAdapter(adapter);
                                Toast.makeText(ChatBoxActivity.this, object.toString(), Toast.LENGTH_SHORT).show();

                            }
                        });

                        Log.d("MESSAGE", object.toString());
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else if (args[0] instanceof String) {
                    // If it's a String, handle it accordingly
                    String messageString = (String) args[0];
                    // Handle the string content as needed
                }
            }
        });


        socket.on("userdisconnect", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String data = (String) args[0];

                Toast.makeText(ChatBoxActivity.this,data,Toast.LENGTH_SHORT).show();

            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.disconnect();
    }
}