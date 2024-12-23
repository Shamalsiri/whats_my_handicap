package com.siriwardana.whatsmyhandicap.helpers;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.siriwardana.whatsmyhandicap.R;

public class RoundDataViewHolder extends RecyclerView.ViewHolder {

    TextView courseNameTV, dateTV, totalDistanceTV, totalParTV, totalScoreTV;
    ImageButton editRoundIB;
    ImageButton deleteRoundIB;
    RecyclerView holeDataRV;

    public RoundDataViewHolder(@NonNull View itemView) {
        super(itemView);

        courseNameTV = itemView.findViewById(R.id.tv_round_course_name);
        dateTV = itemView.findViewById(R.id.tv_round_date);
        totalDistanceTV = itemView.findViewById(R.id.tv_round_distance);
        totalParTV = itemView.findViewById(R.id.tv_round_par);
        totalScoreTV = itemView.findViewById(R.id.tv_round_total_score);

        editRoundIB = itemView.findViewById(R.id.ib_edit_round);
        deleteRoundIB = itemView.findViewById(R.id.ib_delete_round);

        holeDataRV = itemView.findViewById(R.id.rv_round_holes_data);
    }
}
