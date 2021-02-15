package com.example.setourn.Players;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.setourn.Organizer.LiveTournaments;
import com.example.setourn.Organizer.MyViewholder;
import com.example.setourn.Organizer.OpenPastTournaments;
import com.example.setourn.Organizer.OrganizerPastTournament;
import com.example.setourn.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class PlayerPastTournament extends Fragment {

    public static final String TAG = "WATCH OUT";

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference pastTourRef = db.collection("Past Tournaments");
    private RecyclerView recyclerView;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String USERNAME;

    FirestoreRecyclerOptions<LiveTournaments> options;
    FirestoreRecyclerAdapter<LiveTournaments, PlayerViewHolder> adapter;

    public PlayerPastTournament() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_player_past_tournament, container, false);

        recyclerView = view.findViewById(R.id.past_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        String userID = user.getUid();
        FirebaseDatabase.getInstance().getReference().child("Users").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                USERNAME = (String) snapshot.child("username").getValue();
                ReadData(USERNAME);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
    private void ReadData(String USERNAME) {

        Query query = pastTourRef.whereArrayContains("participantsName", USERNAME);
        options = new FirestoreRecyclerOptions.Builder<LiveTournaments>().setQuery(query, LiveTournaments.class).build();
        adapter = new FirestoreRecyclerAdapter<LiveTournaments, PlayerViewHolder>(options) {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            protected void onBindViewHolder(@NonNull final PlayerViewHolder holder, int position, @NonNull final LiveTournaments model) {
                holder.PTypeTextView.setText("Tournament Type: " + model.getTournamentType());
                holder.PParticipantsTextView.setText("Participants: " + model.getTournamentParticipants());
                if (model.getTournamentGame().equals("Cs")) {
                    holder.PGameImageView.setImageResource(R.drawable.cs_list_live);
                    holder.PGameImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String passID = model.getTournamentID();
                            Intent intent = new Intent(PlayerPastTournament.this.getActivity(), OpenPastTournaments.class);
                            intent.putExtra("TournamentID", passID);
                            startActivity(intent);
                        }
                    });
                } else {
                    holder.PGameImageView.setImageResource(R.drawable.live_dota_wall);
                    holder.PGameImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String passID = model.getTournamentID();
                            Intent intent = new Intent(PlayerPastTournament.this.getActivity(), OpenPastTournaments.class);
                            intent.putExtra("TournamentID", passID);
                            startActivity(intent);
                        }
                    });
                }

            }

            @NonNull
            @Override
            public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.past_recycler_view, parent, false);
                return new PlayerViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}