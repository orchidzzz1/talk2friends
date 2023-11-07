package com.example.frontend.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.frontend.ClientAPI.ClientAPI;
import com.example.frontend.R;
import com.example.frontend.createMeetingActivity;
import com.example.frontend.databinding.FragmentHomeBinding;
import com.example.frontend.models.Meeting;

import org.json.JSONException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    Meeting[] meetings;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Button goCreateMeetingButton = (Button) root.findViewById(R.id.goMeetingPageButton);
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
        ListView v =  root.findViewById(R.id.meetingListView);
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

        //final TextView textView = binding.textHome;
        //homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public void openCreateMeetingPage(){
        Intent intent = new Intent(getActivity(), createMeetingActivity.class);
        startActivity(intent);
    }
}