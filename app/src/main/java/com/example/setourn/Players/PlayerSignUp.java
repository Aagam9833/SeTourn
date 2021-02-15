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

import com.example.setourn.Organizer.OrganizerLogin;
import com.example.setourn.Organizer.OrganizerSignUp;
import com.example.setourn.Organizer.Organizers;
import com.example.setourn.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PlayerSignUp extends AppCompatActivity {

    private static final String TAG = "PlaySignUpAct";
    private FirebaseAuth mAuth;
    Players currentPlayer;
    Boolean allClear = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_sign_up);

        mAuth = FirebaseAuth.getInstance();

        final EditText playerSignUpEmail = findViewById(R.id.player_signUp_email);
        final EditText playerSignUpUsername = findViewById(R.id.player_signUp_username);



        final EditText playerSignUpPassword = findViewById(R.id.player_signUp_password);
        Button playerSignUpButton = findViewById(R.id.player_signUp_button);
        TextView playerLoginTextView = findViewById(R.id.player_login_textView);
        playerLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayerSignUp.this, PlayerLogin.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });


        playerSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String playerEmailString = playerSignUpEmail.getText().toString().trim();
                String playerPasswordString = playerSignUpPassword.getText().toString().trim();

                playerEmailString = "Play" + playerEmailString;

                final String playerUsername = playerSignUpUsername.getText().toString().trim();

                FirebaseDatabase.getInstance().getReference().child("Users")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                    Players players = snapshot.getValue(Players.class);
                                    if(players.getUsername().equals(playerUsername)){
                                        Toast.makeText(PlayerSignUp.this, "Username Already Exists!", Toast.LENGTH_SHORT).show();
                                    }else{
                                        currentPlayer = new Players(playerUsername,"Player");
                                        allClear = true;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                if(playerEmailString.isEmpty()){
                    playerSignUpEmail.setError("Enter A Valid Email");
                }else if(playerPasswordString.isEmpty()){
                    playerSignUpPassword.setError("Enter a valid password");
                }else {
                    mAuth.createUserWithEmailAndPassword(playerEmailString, playerPasswordString)
                            .addOnCompleteListener(PlayerSignUp.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(PlayerSignUp.this,"Account Created Successfully!", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(PlayerSignUp.this, PlayerLogin.class);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                        FirebaseDatabase.getInstance().getReference("Users")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(currentPlayer);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(PlayerSignUp.this, "Authentication failed." + task.getException(),
                                                Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }
            }
        });

    }
}