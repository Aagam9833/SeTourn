package com.example.setourn.Organizer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.setourn.MainActivity;
import com.example.setourn.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class OrganizerLogin extends AppCompatActivity {

    private static final String TAG = "OrgLogInAct";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_login);

        mAuth = FirebaseAuth.getInstance();
        final EditText organizerEmail = findViewById(R.id.organizer_login_email);
        final EditText organizerPassword = findViewById(R.id.organizer_login_password);
        Button organizerLoginButton = findViewById(R.id.organizer_login_button);
        TextView organizerSignUpTextView = findViewById(R.id.organizer_signUp_textView);
        organizerSignUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrganizerLogin.this,OrganizerSignUp.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        organizerLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 String organizerEmailString = organizerEmail.getText().toString().trim();
                 String organizerPasswordString = organizerPassword.getText().toString().trim();

                organizerEmailString = "Org" + organizerEmailString;

                if(organizerEmailString.isEmpty()){
                    organizerEmail.setError("Please enter Email");
                }else if(organizerPasswordString.isEmpty()) {
                    organizerPassword.setError("Please enter Password");
                }else{
                    mAuth.signInWithEmailAndPassword(organizerEmailString, organizerPasswordString)
                            .addOnCompleteListener(OrganizerLogin.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Intent intent = new Intent(OrganizerLogin.this, OrganizerHomePage.class);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(OrganizerLogin.this, "Authentication failed." + task.getException(),
                                                Toast.LENGTH_SHORT).show();
                                        //updateUI(null);
                                    }

                                }

                            });
                }
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null) {
            Intent intent = new Intent(this, OrganizerHomePage.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}