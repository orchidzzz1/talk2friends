package com.example.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.frontend.ClientAPI.ClientAPI;
import com.example.frontend.R;
import com.example.frontend.createMeetingActivity;
import com.example.frontend.databinding.ActivityMainBinding;
import com.example.frontend.models.Meeting;
import com.example.frontend.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class ProfilePageActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    User user;
    //String meetingsList[] = {"Sample Meeting 1", "Sample Meeting 2", "Sample Meeting 3", "Sample Meeting 4"};
    //ListView meetingListView;
    //private Button goCreateMeetingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ClientAPI api = new ClientAPI(this);
        try {
            user = api.getUser();
        } catch (JSONException e) {
            user = null;
            openLoginPage();
        }
        TextView name = findViewById(R.id.nameTextView);
        name.setText(user.getUserName());
        TextView affiliation = findViewById(R.id.affiliationTextView);
        name.setText(user.getAffiliation());
        TextView role = findViewById(R.id.roleTextView);
        if(user.getInternational()){
            role.setText("International");
        }else{
            role.setText("Native Speaker");
        }
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        //get the spinner from the xml.
        Spinner interestDropDown = findViewById(R.id.interestsSpinner);
        //create a list of items for the spinner.
        String[] interests = user.getInterests();
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, interests);
        //set the spinners adapter to the previously created one.
        interestDropDown.setAdapter(adapter);
        generateCreatedMeetings(user.getCreatedMeetings());
        Button signoutBtn = findViewById(R.id.signOutButton);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        signoutBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if(currentUser != null){
                    mAuth.signOut();
                }
                openLoginPage();
            }
        });

    }
    private void generateCreatedMeetings(Meeting[] meetings){
        // create layout dynamically
        Spinner view =  findViewById(R.id.createdMeetingsSpinner);
        LayoutInflater li = LayoutInflater.from(this);
        for (Meeting meeting: meetings) {
            View meetingView = li.inflate(R.layout.meeting_banner, view, false);
            TextView title = meetingView.findViewById(R.id.textViewTitle);
            title.setText(meeting.getTitle());
            TextView desc = meetingView.findViewById(R.id.textViewDescription);
            desc.setText(meeting.getDescription());
            TextView location = meetingView.findViewById(R.id.textViewLocation);
            location.setText(String.format("Location: %s", meeting.getLocation()));
            TextView datetime = meetingView.findViewById(R.id.textViewDateTime);
            long timeInMillis = Long.parseLong(datetime.getText().toString());
            Instant instant = Instant.ofEpochMilli(timeInMillis);
            LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            // Format the date-time to a string
            String formattedDateTime = dateTime.format(formatter);
            location.setText(String.format("Datetime: %s", formattedDateTime));
            Spinner spinner = meetingView.findViewById(R.id.spinnerParticipants);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, meeting.getParticipants());
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            spinner.setAdapter(adapter);
            Button btnRSVP = meetingView.findViewById(R.id.btnRSVP);
            btnRSVP.setTag(meeting.getMeetingId());
            btnRSVP.setVisibility(View.GONE);

            view.addView(meetingView);

        }
    }



    public void openCreateMeetingPage(){
        Intent intent = new Intent(this, createMeetingActivity.class);
        startActivity(intent);
    }
    public void openLoginPage(){
        Intent intent = new Intent(this, LoginPageActivity.class);
        startActivity(intent);
    }

}