package com.example.frontend;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.frontend.ClientAPI.ClientAPI;

import org.json.JSONException;

public class FriendBannerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_banner);

        // Find views by their IDs
        TextView textViewFriendName = findViewById(R.id.textViewFriendName);
        Button btnRemoveFriend = findViewById(R.id.btnRemoveFriend);


        ClientAPI api = new ClientAPI(this);
        // Set click listener for the remove button
        btnRemoveFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Implement remove friend functionality here
                try {
                    api.removeFriend(textViewFriendName.getText().toString());
                } catch (JSONException e) {

                }
            }
        });
    }
}

