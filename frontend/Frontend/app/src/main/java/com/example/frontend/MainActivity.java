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

import com.example.frontend.ClientAPI.ClientAPI;
import com.example.frontend.models.Meeting;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.frontend.databinding.ActivityMainBinding;

import org.json.JSONException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
//    String meetingsList[] = {"Sample Meeting 1", "Sample Meeting 2", "Sample Meeting 3", "Sample Meeting 4"};
    ListView meetingListView;
    Meeting[] meetings;
    private Button goCreateMeetingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ClientAPI api = new ClientAPI(this);
        try {
            meetings = api.getMeetings();
        } catch (JSONException e) {
            meetings = new Meeting[]{};
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

        generateMeetings(meetings);

        // initialize go to create meeting page button
        goCreateMeetingButton = (Button) findViewById(R.id.goMeetingPageButton);
        goCreateMeetingButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openCreateMeetingPage();
            }
        });
    }
    private void generateMeetings(Meeting[] meetings){
        // create layout dynamically
        ListView view =  findViewById(R.id.meetingListView);
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

            view.addView(meetingView);

        }
    }

    public void openCreateMeetingPage(){
        Intent intent = new Intent(this, createMeetingActivity.class);
        startActivity(intent);
    }

}