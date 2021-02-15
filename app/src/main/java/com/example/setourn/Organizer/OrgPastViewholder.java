package com.example.setourn.Organizer;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.setourn.R;

public class OrgPastViewholder extends RecyclerView.ViewHolder {

    TextView PTypeTextView,PParticipantsTextView;
    ImageView PGameImageView;

    public OrgPastViewholder(@NonNull View itemView) {
        super(itemView);

        PTypeTextView = itemView.findViewById(R.id.past_tournament_type);
        PParticipantsTextView = itemView.findViewById(R.id.past_tournament_participants);
        PGameImageView = itemView.findViewById(R.id.past_image_view);

    }
}
