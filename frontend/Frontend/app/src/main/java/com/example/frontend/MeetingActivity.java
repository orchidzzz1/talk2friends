package com.example.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.frontend.ClientAPI.ClientAPI;
import com.example.frontend.databinding.ActivityMainBinding;
import com.example.frontend.models.Meeting;
import com.example.frontend.models.User;

import org.json.JSONException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class MeetingActivity extends Fragment {
    private ActivityMainBinding binding;
    ListView meetingListView;
    Meeting[] meetings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Button goCreateMeetingButton = (Button) view.findViewById(R.id.goMeetingPageButton);
        goCreateMeetingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("hi");
                openCreateMeetingPage();
            }
        });
        ClientAPI api = new ClientAPI(getActivity());
        try {
            meetings = api.getMeetings();
        } catch (JSONException e) {
            meetings = new Meeting[]{};
        }
        ListView v =  view.findViewById(R.id.meetingListView);
        LayoutInflater li = LayoutInflater.from(getActivity());
        for (Meeting meeting: meetings) {
            View meetingView = li.inflate(R.layout.meeting_banner, v, false);
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
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, meeting.getParticipants());
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            spinner.setAdapter(adapter);
            Button btnRSVP = meetingView.findViewById(R.id.btnRSVP);
            btnRSVP.setTag(meeting.getMeetingId());

            v.addView(meetingView);

        }

        // initialize go to create meeting page button


        return view;

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button goCreateMeetingButton = (Button) view.findViewById(R.id.goMeetingPageButton);
        goCreateMeetingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("hi");
                Intent intent = new Intent(getActivity(), createMeetingActivity.class);
                startActivity(intent);
            }
        });
    }


//    private void generateMeetings(Meeting[] meetings){
//        // create layout dynamically
//        ListView view =  getView().findViewById(R.id.meetingListView);
//        LayoutInflater li = LayoutInflater.from(getActivity());
//        for (Meeting meeting: meetings) {
//            View meetingView = li.inflate(R.layout.meeting_banner, view, false);
//            TextView title = meetingView.findViewById(R.id.textViewTitle);
//            title.setText(meeting.getTitle());
//            TextView desc = meetingView.findViewById(R.id.textViewDescription);
//            desc.setText(meeting.getDescription());
//            TextView location = meetingView.findViewById(R.id.textViewLocation);
//            location.setText(String.format("Location: %s", meeting.getLocation()));
//            TextView datetime = meetingView.findViewById(R.id.textViewDateTime);
//            long timeInMillis = Long.parseLong(datetime.getText().toString());
//            Instant instant = Instant.ofEpochMilli(timeInMillis);
//            LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//            // Format the date-time to a string
//            String formattedDateTime = dateTime.format(formatter);
//            location.setText(String.format("Datetime: %s", formattedDateTime));
//            Spinner spinner = meetingView.findViewById(R.id.spinnerParticipants);
//            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, meeting.getParticipants());
//            // Specify the layout to use when the list of choices appears
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            // Apply the adapter to the spinner
//            spinner.setAdapter(adapter);
//            Button btnRSVP = meetingView.findViewById(R.id.btnRSVP);
//            btnRSVP.setTag(meeting.getMeetingId());
//
//            view.addView(meetingView);
//
//        }
//    }

    public void openCreateMeetingPage(){
        Intent intent = new Intent(getActivity(), createMeetingActivity.class);
        startActivity(intent);
    }




}
