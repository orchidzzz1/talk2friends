package com.example.frontend;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.frontend.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    String meetingsList[] = {"Sample Meeting 1", "Sample Meeting 2", "Sample Meeting 3", "Sample Meeting 4"};
    ListView meetingListView;

    private Button goCreateMeetingButton;

    //String friendsList[] = {"Alexis Situ", "Hang Nguyen", "Easton Zhang"};
    //ListView friendListView;

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

        // INITIALIZE MEETING LIST VIEW
        meetingListView = (ListView) findViewById(R.id.meetingListView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, meetingsList);
        meetingListView.setAdapter(arrayAdapter);

        // initialize go to create meeting page button
        goCreateMeetingButton = (Button) findViewById(R.id.goMeetingPageButton);
        goCreateMeetingButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openCreateMeetingPage();
            }
        });

        // INITIALIZE Friend list view
        //friendListView = (ListView) findViewById(R.id.friendsListView);
        //ArrayAdapter<String> arrayAdapterFriends = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, friendsList);
        //friendListView.setAdapter(arrayAdapterFriends);


    }

    public void openCreateMeetingPage(){
        Intent intent = new Intent(this, createMeetingActivity.class);
        startActivity(intent);
    }


}