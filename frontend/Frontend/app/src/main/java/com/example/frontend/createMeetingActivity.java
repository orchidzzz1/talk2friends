package com.example.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class createMeetingActivity extends AppCompatActivity {

    private Button createMeetingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meeting);

        // initialize create meeting button
        createMeetingButton = (Button) findViewById(R.id.createNewMeetingButton);
        createMeetingButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openMeetingsPage();
            }
        });
    }

    public void openMeetingsPage(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}