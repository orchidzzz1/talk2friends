package com.example.frontend;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.frontend.ClientAPI.ClientAPI;
import com.example.frontend.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.frontend.databinding.ActivityMainBinding;

import org.json.JSONException;

import java.util.List;
import java.util.Map;

public class FriendsActivity extends AppCompatActivity{
    private ActivityMainBinding binding;
    private Button goRecommendPageButton;
    ListView friendListView;
    User user;

    //Button searchButton = findViewById(R.id.friendSearchButton);
    //EditText friendNameEditText = findViewById(R.id.friendNameEditText);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        Button friendRecommendBtn = (Button) findViewById(R.id.friendRecommendBtn);
        EditText friendEmailEditText =  findViewById(R.id.friendEmailEditText);
        String friendEmail = friendEmailEditText.getText().toString();
        ClientAPI api = new ClientAPI(this);
        try {
            user = api.getUser();
        } catch (JSONException e) {
            user = null;
        }

        friendRecommendBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                try {
                    api.recommend(friendEmail);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // initialize go to recommend page button
        goRecommendPageButton = (Button) findViewById(R.id.goRecommendPageButton);
        goRecommendPageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openRecommendPage();
            }
        });
        generateFriends(user.getFriends());
    }
    private void generateFriends(String[] friends){
        // create layout dynamically
        ListView view =  findViewById(R.id.friendsListView);
        LayoutInflater li = LayoutInflater.from(this);
        for(String friend: friends){
            View userView = li.inflate(R.layout.friend_banner, view, false);
            TextView name = userView.findViewById(R.id.textViewFriendName);
            name.setText(friend);
            view.addView(userView);

        }

    }

    public void openRecommendPage(){
        Intent intent = new Intent(this, RecommendPageActivity.class);
        startActivity(intent);
    }
}
