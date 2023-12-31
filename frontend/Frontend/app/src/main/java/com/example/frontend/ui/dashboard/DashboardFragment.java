package com.example.frontend.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.frontend.ClientAPI.ClientAPI;
import com.example.frontend.R;
import com.example.frontend.RecommendPageActivity;
import com.example.frontend.createMeetingActivity;
import com.example.frontend.databinding.FragmentDashboardBinding;
import com.example.frontend.models.Meeting;
import com.example.frontend.models.User;

import org.json.JSONException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    ListView friendListView;
    User user;
    ClientAPI api;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //final TextView textView = binding.textDashboard;
        //dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        Button friendRecommendBtn = (Button) root.findViewById(R.id.friendRecommendBtn);

        api = new ClientAPI(getActivity());
        try {
            user = api.getUser();
        } catch (JSONException e) {
            user = null;
        }

        friendRecommendBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                EditText friendEmailEditText =  root.findViewById(R.id.friendEmailEditText);
                String friendEmail = friendEmailEditText.getText().toString();
                try {
                    api.recommend(friendEmail);
                    friendEmailEditText.setText("");
                    Toast.makeText(getActivity(), "Sent successfully",Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // initialize go to recommend page button
        Button goRecommendPageButton = (Button) root.findViewById(R.id.goRecommendPageButton);
        goRecommendPageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openRecommendPage();
            }
        });

        generateFriendsList(root, user.getFriends());
        generateRequestsList(root, user.getRequests());


        return root;
    }
    private void generateFriendsList(View v, String[] friends) {
        LinearLayout view =  v.findViewById(R.id.friendsListView);
        LayoutInflater li = LayoutInflater.from(getActivity());
        for(String friend: user.getFriends()){
            View userView = li.inflate(R.layout.friend_banner, view, false);
            TextView name = userView.findViewById(R.id.textViewFriendName);
            name.setText(friend);
            view.addView(userView);
            Button addFriend = userView.findViewById(R.id.btnAddFriendRequest);
            addFriend.setVisibility(View.GONE);
            Button removeFriend = userView.findViewById(R.id.btnRemoveFriend);
            removeFriend.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    try {
                        api.removeFriend(friend);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            });


        }
    }
    private void generateRequestsList(View v, String[] requests) {
        LinearLayout view =  v.findViewById(R.id.requestsListView);
        LayoutInflater li = LayoutInflater.from(getActivity());
        for(String friend: user.getFriends()){
            View userView = li.inflate(R.layout.friend_banner, view, false);
            TextView name = userView.findViewById(R.id.textViewFriendName);
            name.setText(friend);
            view.addView(userView);
            Button removeFriend = userView.findViewById(R.id.btnRemoveFriend);
            removeFriend.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    try {
                        api.removeFriend(friend);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            Button addFriend = userView.findViewById(R.id.btnAddFriendRequest);
            addFriend.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    try {
                        api.addFriend(friend);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }

        @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public void openRecommendPage(){
        Intent intent = new Intent(getActivity(), RecommendPageActivity.class);
        startActivity(intent);
    }
}