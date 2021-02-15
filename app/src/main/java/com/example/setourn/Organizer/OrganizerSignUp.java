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

import com.example.setourn.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

public class OrganizerSignUp extends AppCompatActivity {

    private static final String TAG = "OrgSignUpAct";
    private FirebaseAuth mAuth;
    Organizers currentOrganizer;
    Boolean allClear = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_sign_up);

        mAuth = FirebaseAuth.getInstance();

        final EditText organizerSignUpEmail = findViewById(R.id.organizer_signUp_email);
        final EditText organizerSignUpUsername = findViewById(R.id.organizer_signUp_username);



        final EditText organizerSignUpPassword = findViewById(R.id.organizer_signUp_password);
        Button organizerSignUpButton = findViewById(R.id.organizer_signUp_button);
        TextView organizerLoginTextView = findViewById(R.id.organizer_login_textView);
        organizerLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrganizerSignUp.this, OrganizerLogin.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });


        organizerSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String organizerEmailString = organizerSignUpEmail.getText().toString().trim();
                String organizerPasswordString = organizerSignUpPassword.getText().toString().trim();

                organizerEmailString = "Org" + organizerEmailString;

                final String organizerUserName = organizerSignUpUsername.getText().toString().trim();

                FirebaseDatabase.getInstance().getReference().child("Users")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Organizers organizers = snapshot.getValue(Organizers.class);
                                    if(organizers.getUsername().equals(organizerUserName)){
                                        Toast.makeText(OrganizerSignUp.this,"Username Already Exists",Toast.LENGTH_SHORT).show();
                                    }else{
                                        currentOrganizer = new Organizers(organizerUserName,"Organizer");
                                        allClear = true;
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });

                if(organizerEmailString.isEmpty()){
                    organizerSignUpEmail.setError("Enter A Valid Email");
                }else if(organizerPasswordString.isEmpty()){
                    organizerSignUpPassword.setError("Enter a valid password");
                }else {
                    if (allClear){
                        mAuth.createUserWithEmailAndPassword(organizerEmailString, organizerPasswordString)
                                .addOnCompleteListener(OrganizerSignUp.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(OrganizerSignUp.this, "Account Created Successfully!", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(OrganizerSignUp.this, OrganizerLogin.class);
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                            FirebaseDatabase.getInstance().getReference("Users")
                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                    .setValue(currentOrganizer);
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                            Toast.makeText(OrganizerSignUp.this, "Authentication failed." + task.getException(),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                        }
                    }
                }
        });
    }
}