package com.example.frontend;// Import necessary packages
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.frontend.ClientAPI.ClientAPI;
import com.example.frontend.R;
import com.example.frontend.store.LocalStorage;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONException;

public class GoogleAuth{

    private FirebaseAuth mAuth;
    private Activity activity;
    private static final int RC_SIGN_IN = 9001;

    public GoogleAuth(Activity activity) {
        this.activity = activity;
        mAuth = FirebaseAuth.getInstance();
    }

    public void signInWithGoogle() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);

        // Launches the Google Sign In activity
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = GoogleSignIn.getSignedInAccountFromIntent(data).getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(activity, "Google Sign In failed: " + e.getStatusCode(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null && user.getEmail() != null && user.getEmail().endsWith("@usc.edu")) {
                            // If yes, call the authorize method from the ClientAPI
                            Context context = this.activity;
                            user.getIdToken(true)
                                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                                            if (task.isSuccessful()) {
                                                String idToken = task.getResult().getToken();

                                                LocalStorage.saveToken(context, idToken);
                                                LocalStorage.saveEmail(context, user.getEmail());
                                                ClientAPI api = new ClientAPI(context);

                                                try {
                                                    boolean verified = api.authorize();
                                                    if(verified){
                                                        openMainPage();
                                                    }else{
                                                        api.sendVerification();
                                                        openVerifyPage();
                                                    }
                                                } catch (JSONException e) {
                                                    throw new RuntimeException(e);
                                                }
                                            } else {
                                                openLogin();
                                            }
                                        }
                                    });
                        }else{
                            mAuth.signOut();

                        }
                        Toast.makeText(activity, "Authentication successful.", Toast.LENGTH_SHORT).show();
                        // You can now use 'user' to get user details or perform other actions.
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(activity, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void openVerifyPage(){
        Intent intent = new Intent(this.activity, VerificationPageActivity.class);
        this.activity.startActivity(intent);
    }
    public void openMainPage(){
        Intent intent = new Intent(this.activity, MainActivity.class);
        this.activity.startActivity(intent);
    }
    public void openLogin(){
        Intent intent = new Intent(this.activity, LoginPageActivity.class);
        this.activity.startActivity(intent);
    }
}
