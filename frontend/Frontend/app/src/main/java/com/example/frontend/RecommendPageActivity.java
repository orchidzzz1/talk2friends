package com.example.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RecommendPageActivity extends AppCompatActivity {
    private Button returnFriendsPageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_page);


        // initialize return button
        returnFriendsPageButton = (Button) findViewById(R.id.returnToFriendsPage);
        returnFriendsPageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                returnFriendsPage();
            }
        });

    }


    public void returnFriendsPage(){
        Intent intent = new Intent(this, FriendsActivity.class);
        startActivity(intent);
    }

}