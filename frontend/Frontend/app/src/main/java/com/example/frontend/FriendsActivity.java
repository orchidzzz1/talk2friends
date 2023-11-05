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

public class FriendsActivity extends AppCompatActivity{
    private ActivityMainBinding binding;
    private Button goRecommendPageButton;

    String friendsList[] = {"Alexis Situ", "Hang Nguyen", "Easton Zhang"};
    ListView friendListView;

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

        // initialize go to recommend page button
        goRecommendPageButton = (Button) findViewById(R.id.goRecommendPageButton);
        goRecommendPageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openRecommendPage();
            }
        });

        // INITIALIZE Friend list view
        friendListView = (ListView) findViewById(R.id.friendsListView);
        ArrayAdapter<String> arrayAdapterFriends = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, friendsList);
        friendListView.setAdapter(arrayAdapterFriends);


    }

    public void openRecommendPage(){
        Intent intent = new Intent(this, RecommendPageActivity.class);
        startActivity(intent);
    }
}
