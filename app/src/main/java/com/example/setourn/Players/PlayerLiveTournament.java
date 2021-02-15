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
import com.example.setourn.Organizer.OpenLiveTournaments;
import com.example.setourn.Organizer.OrganizerLiveTournament;
import com.example.setourn.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class PlayerLiveTournament extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference liveTourRef = db.collection("Tournaments");
    private RecyclerView recyclerView;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String USERNAME;

    FirestoreRecyclerOptions<LiveTournaments> options;
    FirestoreRecyclerAdapter<LiveTournaments, PlayerViewHolder> adapter;

    public PlayerLiveTournament() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_live_tournament, container, false);

        recyclerView = view.findViewById(R.id.recyclerView_player);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        
        ReadData();

        return view;
    }

    private void ReadData() {
        options = new FirestoreRecyclerOptions.Builder<LiveTournaments>().setQuery(liveTourRef, LiveTournaments.class).build();
        adapter = new FirestoreRecyclerAdapter<LiveTournaments, PlayerViewHolder>(options) {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            protected void onBindViewHolder(@NonNull final PlayerViewHolder holder, int position, @NonNull final LiveTournaments model) {
                holder.TypeTextView.setText("Tournament Type: " + model.getTournamentType());
                holder.ParticipantsTextView.setText("Participants: " + model.getTournamentParticipants());
                holder.Status.setText(model.getStatus());
                if (model.getTournamentGame().equals("Cs")) {
                    holder.GameImageView.setImageResource(R.drawable.cs_list_live);
                    holder.GameImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String passID = model.getTournamentID();
                            Intent intent = new Intent(PlayerLiveTournament.this.getActivity(), OpenLiveTournaments.class);
                            intent.putExtra("TournamentID", passID);
                            startActivity(intent);
                        }
                    });
                } else {
                    holder.GameImageView.setImageResource(R.drawable.live_dota_wall);
                    holder.GameImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String passID = model.getTournamentID();
                            Intent intent = new Intent(PlayerLiveTournament.this.getActivity(), OpenLiveTournaments.class);
                            intent.putExtra("TournamentID", passID);
                            startActivity(intent);
                        }
                    });
                }

            }
            @NonNull
            @Override
            public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_live_recycler_view, parent, false);
                return new PlayerViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}