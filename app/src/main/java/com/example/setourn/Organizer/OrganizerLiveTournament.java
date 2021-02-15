package com.example.setourn.Organizer;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class OrganizerLiveTournament extends Fragment {

    public OrganizerLiveTournament() {
        // Required empty public constructor
    }

    public static final String TAG = "WATCH OUT: ";

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference liveTourRef = db.collection("Tournaments");
    private RecyclerView recyclerView;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String USERNAME;

    FirestoreRecyclerOptions<LiveTournaments> options;
    FirestoreRecyclerAdapter<LiveTournaments,MyViewholder> adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_organizer_live_tournament, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
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

            Query query = liveTourRef.whereEqualTo("organizerUsername", USERNAME);
            options = new FirestoreRecyclerOptions.Builder<LiveTournaments>().setQuery(query, LiveTournaments.class).build();
            adapter = new FirestoreRecyclerAdapter<LiveTournaments, MyViewholder>(options) {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                protected void onBindViewHolder(@NonNull final MyViewholder holder, int position, @NonNull final LiveTournaments model) {
                        holder.TypeTextView.setText("Tournament Type: " + model.getTournamentType());
                        holder.ParticipantsTextView.setText("Participants: " + model.getTournamentParticipants());
                        holder.Status.setText(model.getStatus());

                        if (model.getTournamentGame().equals("Cs")) {
                            holder.GameImageView.setImageResource(R.drawable.cs_list_live);
                            holder.GameImageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String passID = model.getTournamentID();
                                    Intent intent = new Intent(OrganizerLiveTournament.this.getActivity(), OpenLiveTournaments.class);
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
                                    Intent intent = new Intent(OrganizerLiveTournament.this.getActivity(), OpenLiveTournaments.class);
                                    intent.putExtra("TournamentID", passID);
                                    startActivity(intent);
                                }
                            });
                        }

                    }
                @NonNull
                @Override
                public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.live_recycler_view, parent, false);
                    return new MyViewholder(view);
                }
            };
            adapter.startListening();
            recyclerView.setAdapter(adapter);
        }

}
