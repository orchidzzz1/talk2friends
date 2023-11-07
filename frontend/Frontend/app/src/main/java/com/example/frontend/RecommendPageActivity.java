package com.example.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.frontend.ClientAPI.ClientAPI;
import com.example.frontend.models.Meeting;
import com.example.frontend.ui.dashboard.DashboardFragment;
import com.google.android.gms.common.api.Api;

import org.json.JSONException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecommendPageActivity extends AppCompatActivity {
    private Button returnFriendsPageButton;
    Map<String, List<String>> res;
    ClientAPI api;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_page);
        api = new ClientAPI(this);

        try {
            res = api.getRecommended();
        } catch (JSONException e) {
            res = new HashMap<String, List<String>>(){};
        }
        generateUsers(res);

        // initialize return button
        returnFriendsPageButton = (Button) findViewById(R.id.returnToFriendsPage);
        returnFriendsPageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                returnFriendsPage();
            }
        });

    }
    private void generateUsers(Map<String, List<String>> users){
        // create layout dynamically
        ListView view =  findViewById(R.id.friendsListView);
        LayoutInflater li = LayoutInflater.from(this);
        for (Map.Entry<String, List<String>> user : users.entrySet()) {
            View userView = li.inflate(R.layout.new_friend_banner, view, false);
            TextView name = userView.findViewById(R.id.textViewFriendName);
            TextView interestsView = userView.findViewById(R.id.interestsTextView);

            String key = user.getKey();
            name.setText(key);
            List<String> interests = user.getValue();
            // Iterate through the list of strings
            StringBuilder stringBuilder = new StringBuilder();

            // Iterate through the array and append each string to the StringBuilder
            for (int i = 0; i < interests.size(); i++) {
                stringBuilder.append(interests.get(i));

                // Add a comma if it's not the last element
                if (i < interests.size() - 1) {
                    stringBuilder.append(", ");
                }
            }

            // Convert StringBuilder to a String
            String resultString = stringBuilder.toString();
            interestsView.setText(resultString);
            view.addView(userView);
        }
    }


    public void returnFriendsPage(){
        Intent intent = new Intent(this, DashboardFragment.class);
        startActivity(intent);
    }

}