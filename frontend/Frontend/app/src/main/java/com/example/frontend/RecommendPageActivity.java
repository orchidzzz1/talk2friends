package com.example.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.frontend.ClientAPI.ClientAPI;
import com.example.frontend.models.Meeting;
import com.example.frontend.store.LocalStorage;
import com.example.frontend.ui.dashboard.DashboardFragment;
import com.google.android.gms.common.api.Api;

import org.json.JSONException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
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
            System.out.println(res);
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
        LocalStorage store = new LocalStorage();
        // create layout dynamically
        LinearLayout view =  findViewById(R.id.recommendFriendsListView);
        LayoutInflater li = LayoutInflater.from(this);
        for (Map.Entry<String, List<String>> user : users.entrySet()) {
            View userView = li.inflate(R.layout.new_friend_banner, view, false);
            TextView name = userView.findViewById(R.id.textViewFriendName);
            TextView interestsView = userView.findViewById(R.id.interestsTextView);

            String key = user.getKey();
            // check if key == user's email or already in requests/friends list
            if(key.equals(store.getEmail(this)) || Arrays.asList(store.getRequests(this)).contains(key) ||Arrays.asList(store.getFriends(this)).contains(key)){
                continue;
            }
            name.setText(key);
            System.out.println(key);
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
            Button addBtn = userView.findViewById(R.id.btnAddFriend);
            addBtn.setTag(key);
            addBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    try {
                        api.addFriend(addBtn.getTag().toString());
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
            });

            // Convert StringBuilder to a String
            String resultString = stringBuilder.toString();
            interestsView.setText(resultString);
            view.addView(userView);
        }
    }


    public void returnFriendsPage(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}