package com.example.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.frontend.ClientAPI.ClientAPI;
import com.example.frontend.models.Meeting;
import com.example.frontend.models.User;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class AuthenticationPageActivity extends AppCompatActivity {
    boolean international;
    ArrayList interests = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication_page);
        ClientAPI api = new ClientAPI(this);
        RadioButton radioInternational = (RadioButton)findViewById(R.id.radioInternational);
        RadioButton radioNative = (RadioButton)findViewById(R.id.radioNative);
        Button registerButton = findViewById(R.id.registerButton);
        CheckBox checkBoxInterest1 = findViewById(R.id.sportsCheckBox);
        CheckBox checkBoxInterest2 = findViewById(R.id.videoGamesCheckBox);
        CheckBox checkBoxInterest3 = findViewById(R.id.fashionCheckBox);
        CheckBox checkBoxInterest4 = findViewById(R.id.moviesCheckBox);
        CheckBox checkBoxInterest5 = findViewById(R.id.musicCheckBox);

        radioNative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show the button to generate Zoom link
                international = false;
            }
        });
        radioInternational.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show the button to generate Zoom link
                international = true;
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                EditText registerUsernameEdit = findViewById(R.id.registerUsername);
                String username = registerUsernameEdit.getText().toString();

                EditText registerAffiliationEdit = findViewById(R.id.registerAffiliation);
                String affiliation = registerAffiliationEdit.getText().toString();
                if (checkBoxInterest1.isChecked()) {
                    interests.add("sports");
                }
                if (checkBoxInterest2.isChecked()) {
                    interests.add("video games");
                }
                if (checkBoxInterest3.isChecked()) {
                    interests.add("fashion");
                }
                if (checkBoxInterest4.isChecked()) {
                    interests.add("movies");

                }
                if (checkBoxInterest5.isChecked()) {
                    interests.add("music");
                }
                String[] interestInput = (String[]) interests.toArray(new String[0]);
                User user = new User(username, "",interestInput, affiliation, international, new String[]{}, new String[]{},
                new Meeting[]{});
                try {
                    api.addUser(user);
                    openMeetingsPage();

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });


    }
    public void openMeetingsPage(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}