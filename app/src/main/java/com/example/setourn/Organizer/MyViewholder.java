package com.example.setourn.Organizer;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.setourn.R;

public class MyViewholder extends RecyclerView.ViewHolder {
    TextView TypeTextView,ParticipantsTextView,Status;
    ImageView GameImageView;

    public MyViewholder(@NonNull View itemView) {
        super(itemView);

        TypeTextView = itemView.findViewById(R.id.live_tournament_type);
        ParticipantsTextView = itemView.findViewById(R.id.live_tournament_participants);
        GameImageView = itemView.findViewById(R.id.live_image_view);
        Status = itemView.findViewById(R.id.live_status);

    }
}
