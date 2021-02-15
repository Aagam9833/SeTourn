package com.example.setourn.Organizer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.setourn.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DotaActivity extends AppCompatActivity {

    private static final String TAG = "DotaActivity";

    private RadioGroup TypeRadioGroup;
    private RadioGroup ParticipantsRadioGroup;
    private RadioGroup OpenTillRadioGroup;
    private RadioButton TypeRadioButton;
    private RadioButton ParticipantsRadioButton;
    private RadioButton OpenTillRadioButton;
    String username;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference ref = db.collection("Tournaments");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dota);

        final String uid = user.getUid();
        TypeRadioGroup = findViewById(R.id.dota_tournament_type_radio);
        ParticipantsRadioGroup = findViewById(R.id.dota_participants_radio);
        OpenTillRadioGroup = findViewById(R.id.dota_open_till);
        Button setTournamentButton = findViewById(R.id.set_tournament_submit);

        FirebaseDatabase.getInstance().getReference().child("Users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                username = (String) snapshot.child("username").getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        setTournamentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int TypeSelected = TypeRadioGroup.getCheckedRadioButtonId();
                int ParticipantsSelected = ParticipantsRadioGroup.getCheckedRadioButtonId();
                int OpenTillSelected = OpenTillRadioGroup.getCheckedRadioButtonId();

                if(TypeSelected == -1 || ParticipantsSelected == -1 || OpenTillSelected == -1){
                    Toast.makeText(DotaActivity.this, "Please Select All Options", Toast.LENGTH_SHORT).show();
                }else{
                    TypeRadioButton = findViewById(TypeSelected);
                    ParticipantsRadioButton = findViewById(ParticipantsSelected);
                    OpenTillRadioButton = findViewById(OpenTillSelected);

                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                    String date = simpleDateFormat.format(calendar.getTime());
                    simpleDateFormat = new SimpleDateFormat("kk:mm:ss");
                    String time = simpleDateFormat.format(calendar.getTime());

                    Calendar calendarMilli = Calendar.getInstance();
                    long timeMilli = calendarMilli.getTimeInMillis();

                    LiveTournaments liveTournaments = new LiveTournaments(TypeRadioButton.getText().toString(),ParticipantsRadioButton.getText().toString(),username,"Dota",date,time,OpenTillRadioButton.getText().toString(),null,"Registration",null,timeMilli,null);

                    ref.add(liveTournaments).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            String docRef = documentReference.getId();
                            db.collection("Tournaments").document(docRef).update("tournamentID",docRef);
                            Toast.makeText(DotaActivity.this,"Tournament Live Now!",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(DotaActivity.this, OrganizerHomePage.class);
                            startActivity(intent);
                        }
                    });

                }
            }
        });

    }
}