package com.example.setourn.Organizer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.setourn.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class OpenPastTournaments extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String TAG = "WATCH OUT";

    String passID;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference pastRef;
    static String Winner;
    static List<String> names;
    static boolean isWinner = false;
    static String item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_past_tournaments);

        passID = getIntent().getStringExtra("TournamentID");
        pastRef = db.collection("Past Tournaments").document(passID);

        final TextView typeText = findViewById(R.id.type_past);
        final TextView organizerText = findViewById(R.id.organizer_past);
        final TextView participantsText = findViewById(R.id.participants_past);
        final LinearLayout liveLayout = findViewById(R.id.org_past_linear_layout);
        final TextView participantsView = findViewById(R.id.participants_view);
        final TextView winnerView = findViewById(R.id.winner_display);
        final Button declareWinner = findViewById(R.id.declare_winner);
        final Spinner winnerSpinner = findViewById(R.id.winner_spinner);
        final Button submitBtn = findViewById(R.id.submit_winner);

        winnerSpinner.setOnItemSelectedListener(this);

        pastRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                final LiveTournaments liveTournaments = documentSnapshot.toObject(LiveTournaments.class);
                typeText.setText(liveTournaments.getTournamentType());
                organizerText.setText(liveTournaments.getOrganizerUsername());
                participantsText.setText(liveTournaments.getTournamentParticipants());
                if(liveTournaments.getTournamentGame().equals("Cs")){
                    liveLayout.setBackgroundResource(R.drawable.part_cs_live);
                }else{
                    liveLayout.setBackgroundResource(R.drawable.part_dota_live);
                }
                StringBuilder participantsName = new StringBuilder();
                names = liveTournaments.getParticipantsName();
                if(names != null) {
                    for (String temp : names) {
                        participantsName.append("\n").append(temp);
                    }
                }
                participantsView.setText(participantsName);

                Winner = liveTournaments.getWinner();
                if(Winner == null){
                    isWinner = false;
                }else{
                    isWinner = true;
                    winnerView.setText(Winner);
                    declareWinner.setVisibility(View.GONE);
                }


            }
        });
        if(!isWinner){
            declareWinner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(OpenPastTournaments.this, android.R.layout.simple_spinner_item, names);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    winnerSpinner.setAdapter(dataAdapter);
                    winnerSpinner.setVisibility(View.VISIBLE);
                    declareWinner.setVisibility(View.GONE);
                    submitBtn.setVisibility(View.VISIBLE);
                }
            });
        }else {
            declareWinner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(OpenPastTournaments.this, "Winner Already Declared", Toast.LENGTH_SHORT).show();
                    declareWinner.setVisibility(View.GONE);
                    winnerSpinner.setVisibility(View.VISIBLE);
                }
            });
        }
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pastRef.update("winner",item);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        item = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}