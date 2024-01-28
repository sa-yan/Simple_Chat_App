package com.sayan.simplechatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private Button btn;
    private EditText nickname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button)findViewById(R.id.enterchat) ;
        nickname = (EditText) findViewById(R.id.nickname);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickName = nickname.getText().toString();
                if(!nickName.isEmpty()){
                    Intent intent = new Intent(getApplicationContext(), ChatBoxActivity.class);
                    intent.putExtra("USERNICKNAME",nickName);
                    startActivity(intent);
                }
            }
        });

    }
}