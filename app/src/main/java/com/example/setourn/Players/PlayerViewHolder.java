package com.example.setourn.Players;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.setourn.R;

public class PlayerViewHolder extends RecyclerView.ViewHolder {
    TextView TypeTextView, ParticipantsTextView, Status;
    ImageView GameImageView;

    TextView PTypeTextView, PParticipantsTextView;
    ImageView PGameImageView;

    public PlayerViewHolder(@NonNull View itemView) {
        super(itemView);

        TypeTextView = itemView.findViewById(R.id.player_live_tournament_type);
        ParticipantsTextView = itemView.findViewById(R.id.player_live_tournament_participants);
        GameImageView = itemView.findViewById(R.id.player_live_image_view);
        Status = itemView.findViewById(R.id.player_live_status);

        PTypeTextView = itemView.findViewById(R.id.past_tournament_type);
        PParticipantsTextView = itemView.findViewById(R.id.past_tournament_participants);
        PGameImageView = itemView.findViewById(R.id.past_image_view);
    }
}
