package com.example.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.frontend.ClientAPI.ClientAPI;
import com.example.frontend.models.Meeting;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MeetingBannerActivity extends AppCompatActivity {
    String meetingId = "";


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meeting_banner);

        // Find views by their IDs
        TextView textViewTitle = findViewById(R.id.textViewTitle);
        TextView textViewDescription = findViewById(R.id.textViewDescription);
        Spinner spinnerParticipants = findViewById(R.id.spinnerParticipants);
        Button btnRSVP = findViewById(R.id.btnRSVP);

        // Set participants data for the spinner

        // Add more participants as needed

//        // Create an ArrayAdapter using the participants list and a default spinner layout
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, participantsList);
//
//        // Specify the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        // Apply the adapter to the spinner
//        spinnerParticipants.setAdapter(adapter);

        ClientAPI api = new ClientAPI(this);

        // Set click listener for the RSVP button
        btnRSVP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String meetingId = btnRSVP.getTag().toString();
                    api.rsvp(meetingId);
                    openCreateMeetingPage();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }
    public void openCreateMeetingPage(){
        Intent intent = new Intent(this, createMeetingActivity.class);
        startActivity(intent);
    }
}
