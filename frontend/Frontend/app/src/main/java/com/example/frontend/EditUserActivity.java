package com.example.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.frontend.ClientAPI.ClientAPI;
import com.example.frontend.models.Meeting;
import com.example.frontend.models.User;
import org.json.JSONException;
import java.util.ArrayList;

public class EditUserActivity extends AppCompatActivity {
    boolean international;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        Intent intent = getIntent();
        ClientAPI api = new ClientAPI(this);
        Button editButton = findViewById(R.id.saveUserButton);
        Button cancelBtn = findViewById(R.id.cancelUserButton);
        EditText editName = findViewById(R.id.editUsername);
        TextView editAffiliation = findViewById(R.id.editAffiliation);
        RadioButton internationalBtn = findViewById(R.id.radioInternational);
        RadioButton native_speakerBtn = findViewById(R.id.radioNative);
        CheckBox sports = findViewById(R.id.sportsCheckBox);
        CheckBox videoGames = findViewById(R.id.videoGamesCheckBox);
        CheckBox fashion = findViewById(R.id.fashionCheckBox);
        CheckBox movies = findViewById(R.id.moviesCheckBox);
        CheckBox music = findViewById(R.id.musicCheckBox);



        // prefill with info
        editName.setText(intent.getStringExtra("username"));
        editAffiliation.setText(intent.getStringExtra("affiliation"));
        if(intent.getBooleanExtra("role", true)){
            internationalBtn.setSelected(true);
            native_speakerBtn.setSelected(false);
            international = true;
        }else{
            internationalBtn.setSelected(false);
            native_speakerBtn.setSelected(true);
            international = false;
        }
        String[] interests = intent.getStringArrayExtra("interests");
        for(String interest: interests){
            if(interest.equals("sports")){
                sports.setChecked(true);
            }else if(interest.equals("fashion")){
                fashion.setChecked(true);
            }if(interest.equals("movies")){
                movies.setChecked(true);
            }if(interest.equals("video games")){
                videoGames.setChecked(true);
            }if(interest.equals("music")){
                music.setChecked(true);
            }
        }
        native_speakerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show the button to generate Zoom link
                international = false;
            }
        });
        internationalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show the button to generate Zoom link
                international = true;
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList new_interests = new ArrayList<>();
                if (sports.isChecked()) {
                    new_interests.add("sports");
                }
                if (videoGames.isChecked()) {
                    new_interests.add("video games");
                }
                if (fashion.isChecked()) {
                    new_interests.add("fashion");
                }
                if (movies.isChecked()) {
                    new_interests.add("movies");
                }
                if (music.isChecked()) {
                    new_interests.add("music");
                }
//                String[] interestInput = (String[]) new_interests.toArray(new String[0]);
                ArrayList interests_to_add = new ArrayList<>();
                for(String interest: interests){
                    if(!new_interests.contains(interest)){
                        try {
                            api.removeInterest(interest);

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }else{
                        new_interests.remove(interest);
                    }
                }
                String[] interestInput = (String[]) new_interests.toArray(new String[0]);
                for(String interest: interestInput){
                    try {
                        api.addInterest(interest);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                String username = editName.getText().toString();
                String affiliation = editAffiliation.getText().toString();

                if(username.equals("") || affiliation.equals("") ){
                    return;
                }

                User user = new User(username, "",interests, affiliation, international, new String[]{}, new String[]{}, new Meeting[]{});
                try {
                    api.updateUser(user);
                    openMeetingsPage();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMeetingsPage();
            }
        });


    }

    public void openMeetingsPage(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
