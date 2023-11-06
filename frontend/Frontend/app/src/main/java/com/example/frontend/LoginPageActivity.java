package com.example.frontend;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginPageActivity extends AppCompatActivity {

    private Button goRegisterPageButton;
    private FirebaseAuth mAuth;
    private GoogleAuth firebaseGoogleAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        mAuth = FirebaseAuth.getInstance();

//        // initialize go to create meeting page button
//        goRegisterPageButton = (Button) findViewById(R.id.registerButton);
//        goRegisterPageButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                openRegisterPage();
//            }
//        });
        Button GoogleLoginBtn = findViewById(R.id.googleLoginButton);
        firebaseGoogleAuth = new GoogleAuth(this);
        GoogleLoginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                firebaseGoogleAuth.signInWithGoogle();

            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            mAuth.signOut();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        firebaseGoogleAuth.onActivityResult(requestCode, resultCode, data);
    }
}