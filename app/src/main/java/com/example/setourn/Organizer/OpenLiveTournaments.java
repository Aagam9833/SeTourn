package com.example.setourn.Organizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.setourn.Players.Players;
import com.example.setourn.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class OpenLiveTournaments extends AppCompatActivity {

    long TimeLeft;
    String passID;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference docRef;
    CollectionReference pastRef;
    int noOfParticipants;
    static String Username;
    static String LoginType;

    public static final String TAG = "WATCH OUT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_live_tournaments);

        passID = getIntent().getStringExtra("TournamentID");
        docRef = db.collection("Tournaments").document(passID);
        pastRef = db.collection("Past Tournaments");

        final TextView typeText = findViewById(R.id.type_live);
        final TextView organizerText = findViewById(R.id.organizer_live);
        final TextView participantsText = findViewById(R.id.participants_live);
        final TextView statusText = findViewById(R.id.status_live);
        final LinearLayout liveLayout = findViewById(R.id.live_linear_layout);
        final TextView timeText = findViewById(R.id.timer_live);
        final Button registerBtn = findViewById(R.id.register_button);
        final TextView participantsView = findViewById(R.id.participants_view);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Players players;
                players = snapshot.getValue(Players.class);
                LoginType = players.getType();
                Username = players.getUsername();
                if(LoginType.equals("Organizer")){
                    registerBtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });



        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                final LiveTournaments liveTournaments = documentSnapshot.toObject(LiveTournaments.class);
                typeText.setText(liveTournaments.getTournamentType());
                organizerText.setText(liveTournaments.getOrganizerUsername());
                participantsText.setText(liveTournaments.getTournamentParticipants());
                statusText.setText(liveTournaments.getStatus() + ": ");
                if(liveTournaments.getTournamentGame().equals("Cs")){
                    liveLayout.setBackgroundResource(R.drawable.part_cs_live);
                }else{
                    liveLayout.setBackgroundResource(R.drawable.part_dota_live);
                }


                Calendar calendar = Calendar.getInstance();
                long timeMilli = calendar.getTimeInMillis();
                long timeStored = liveTournaments.getTimeMilli();
                long TimeElapsed = timeMilli - timeStored;
                long TIMER;
                String OpenTill = liveTournaments.getOpenTill();
                if (OpenTill.equals("1 day")) {
                    TIMER = 86400000;
                } else if (OpenTill.equals("2 day")) {
                    TIMER = 86400000 * 2;
                } else {
                    TIMER = 86400000 * 7;
                }
                TimeLeft = TIMER - TimeElapsed;

                if(liveTournaments.getStatus().equals("Registration")) {
                    new CountDownTimer(TimeLeft, 1000) {
                        @Override
                        public void onTick(long l) {
                            TimeLeft = l;
                            int hr = (int) ((TimeLeft / 1000) / 3600);
                            int min = (int) ((TimeLeft / 1000) % 3600) / 60;
                            int sec = (int) (TimeLeft / 1000) % 60;
                            String TimeLeftFormat = String.format(Locale.getDefault(), "%02d:%02d:%02d", hr, min, sec);
                            timeText.setText(TimeLeftFormat);
                        }

                        @Override
                        public void onFinish() {
                            docRef.update("status", "Live");
                            docRef.update("timeMilli",liveTournaments.getTimeMilli()+86400000);
                        }
                    }.start();
                }
                if(liveTournaments.getStatus().equals("Live")) {
                    TimeLeft = 86400000 - TimeElapsed;
                    new CountDownTimer(TimeLeft, 1000) {
                        @Override
                        public void onTick(long l) {
                            TimeLeft = l;
                            int hr = (int) ((TimeLeft / 1000) / 3600);
                            int min = (int) ((TimeLeft / 1000) % 3600) / 60;
                            int sec = (int) (TimeLeft / 1000) % 60;
                            String TimeLeftFormat = String.format(Locale.getDefault(), "%02d:%02d:%02d", hr, min, sec);
                            timeText.setText(TimeLeftFormat);
                            registerBtn.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFinish() {
                            docRef.update("status", "Ended");
                            timeText.setVisibility(View.GONE);
                            docRef.delete();
                            pastRef.document(passID).set(liveTournaments);
                        }
                    }.start();
                }
                noOfParticipants = Integer.parseInt(liveTournaments.getTournamentParticipants());
                registerBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        List<String> names = liveTournaments.getParticipantsName();
                        if(names == null){
                            docRef.update("participantsName", FieldValue.arrayUnion(Username));
                        }else if(names.size() == noOfParticipants) {
                            Toast.makeText(OpenLiveTournaments.this, "Tournament is full, cannot register", Toast.LENGTH_SHORT).show();
                        }else{
                            Boolean isThere = false;
                                for (int i = 0; i < names.size(); i++) {
                                    isThere = names.get(i).equals(Username);
                                }
                            if(isThere){
                                Toast.makeText(OpenLiveTournaments.this, "You have already registered for this tournament", Toast.LENGTH_SHORT).show();
                            }else{
                                docRef.update("participantsName", FieldValue.arrayUnion(Username));
                                Toast.makeText(OpenLiveTournaments.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                            }
                            }
                        }
                });
                StringBuilder participantsName = new StringBuilder();
                List<String> names = liveTournaments.getParticipantsName();
                if(names != null) {
                    for (String temp : names) {
                        participantsName.append("\n").append(temp);
                    }
                }
                participantsView.setText(participantsName);
            }
        });

    }
}