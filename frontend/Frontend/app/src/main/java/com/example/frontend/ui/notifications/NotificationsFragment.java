package com.example.frontend.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.frontend.ClientAPI.ClientAPI;
import com.example.frontend.LoginPageActivity;
import com.example.frontend.R;
import com.example.frontend.databinding.FragmentNotificationsBinding;
import com.example.frontend.models.Meeting;
import com.example.frontend.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    User user;
    FirebaseAuth mAuth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ClientAPI api = new ClientAPI(getActivity());
        try {
            user = api.getUser();
        } catch (JSONException e) {
            user = null;
            openLoginPage();
        }
        TextView name = root.findViewById(R.id.nameTextView);
        name.setText(user.getUserName());
        TextView affiliation = root.findViewById(R.id.affiliationTextView);
        affiliation.setText(user.getAffiliation());
        TextView role = root.findViewById(R.id.roleTextView);
        if(user.getInternational()){
            role.setText("International");
        }else{
            role.setText("Native Speaker");
        }


        //get the spinner from the xml.
        Spinner interestDropDown = root.findViewById(R.id.interestsSpinner);
        //create a list of items for the spinner.
        String[] interests = user.getInterests();
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, interests);
        //set the spinners adapter to the previously created one.
        interestDropDown.setAdapter(adapter);
        generateCreatedMeetings(root, user.getMeetings());
        Button signoutBtn = root.findViewById(R.id.signOutButton);
        signoutBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if(currentUser != null){
                    mAuth.signOut();
                }
                openLoginPage();
            }
        });

        //final TextView textView = binding.textNotifications;
        //notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }
    private void generateCreatedMeetings(View v, Meeting[] meetings){
        // create layout dynamically
        LinearLayout view =  v.findViewById(R.id.createdMeetingsListView);
        LayoutInflater li = LayoutInflater.from(getActivity());
        for (Meeting meeting: meetings) {
            View meetingView = li.inflate(R.layout.meeting_banner, view, false);
            TextView title = meetingView.findViewById(R.id.textViewTitle);
            title.setText(meeting.getTitle());
            TextView desc = meetingView.findViewById(R.id.textViewDescription);
            desc.setText(meeting.getDescription());
            TextView location = meetingView.findViewById(R.id.textViewLocation);
            location.setText(String.format("Location: %s", meeting.getLocation()));
            TextView datetime = meetingView.findViewById(R.id.textViewDateTime);
            long timeInMillis = meeting.getDatetime();
            Instant instant = Instant.ofEpochMilli(timeInMillis);
            LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            // Format the date-time to a string
            String formattedDateTime = dateTime.format(formatter);
            datetime.setText(String.format("Datetime: %s", formattedDateTime));
//            if(meeting.getParticipants().length > 0){
//                Spinner spinner = meetingView.findViewById(R.id.spinnerParticipants);
//                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, meeting.getParticipants());
//                // Specify the layout to use when the list of choices appears
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                // Apply the adapter to the spinner
//                spinner.setAdapter(adapter);
//            }

            Button btnRSVP = meetingView.findViewById(R.id.btnRSVP);
            btnRSVP.setTag(meeting.getMeetingId());
            btnRSVP.setVisibility(View.GONE);

            view.addView(meetingView);

        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public void openLoginPage(){
        Intent intent = new Intent(getActivity(), LoginPageActivity.class);
        startActivity(intent);
    }
}