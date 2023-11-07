package com.example.frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.frontend.ClientAPI.ClientAPI;

import org.json.JSONException;

public class VerificationPageActivity extends AppCompatActivity {
    ClientAPI api;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_page);
        Button verifyButton = findViewById(R.id.verifyButton);
        Context context = this;
        api = new ClientAPI(context);
        verifyButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                try {
                    EditText editText = findViewById(R.id.verificationText);
                    // Get the text from the EditText
                    String inputValue = editText.getText().toString();
                    int code = Integer.parseInt(inputValue);
                    boolean verified = api.verify(code);
                    if(verified){
                        openRegistrationPage();
                    }else{
                        Toast.makeText(context, "Wrong code.", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            }
        });
        Button resendButton = findViewById(R.id.resendButton);
        resendButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                api.sendVerification();

            }
        });
    }
    public void openRegistrationPage(){
        Intent intent = new Intent(this, AuthenticationPageActivity.class);
        this.startActivity(intent);
    }
}