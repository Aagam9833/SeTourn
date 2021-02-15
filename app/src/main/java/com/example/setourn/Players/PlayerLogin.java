package com.example.setourn.Players;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.setourn.MainActivity;
import com.example.setourn.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PlayerLogin extends AppCompatActivity {

    private static final String TAG = "OrgLogInAct";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_login);

        mAuth = FirebaseAuth.getInstance();
        final EditText playerEmail = findViewById(R.id.player_login_email);
        final EditText playerPassword = findViewById(R.id.player_login_password);
        Button playerLoginButton = findViewById(R.id.player_login_button);
        TextView playerSignUpTextView = findViewById(R.id.player_signUp_textView);
        playerSignUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayerLogin.this, PlayerSignUp.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        playerLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String playerEmailString = playerEmail.getText().toString().trim();
                String playerPasswordString = playerPassword.getText().toString().trim();

                playerEmailString = "Play" + playerEmailString;

                if(playerEmailString.isEmpty()){
                    playerEmail.setError("Please enter Email");
                }else if(playerPasswordString.isEmpty()) {
                    playerPassword.setError("Please enter Password");
                }else{
                    mAuth.signInWithEmailAndPassword(playerEmailString, playerPasswordString)
                            .addOnCompleteListener(PlayerLogin.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Intent intent = new Intent(PlayerLogin.this, PlayerHomePage.class);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(PlayerLogin.this, "Authentication failed." + task.getException(),
                                                Toast.LENGTH_SHORT).show();
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
            Intent intent = new Intent(this, PlayerHomePage.class);
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