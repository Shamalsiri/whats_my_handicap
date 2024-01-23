package com.siriwardana.whatsmyhandicap.helpers;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.siriwardana.whatsmyhandicap.R;

public class HoleDataViewHolder extends RecyclerView.ViewHolder {

    TextView holeTV, distanceTV, parTV, scoreTV;
    public HoleDataViewHolder(@NonNull View itemView) {
        super(itemView);

        holeTV = itemView.findViewById(R.id.tv_hole);
        distanceTV = itemView.findViewById(R.id.tv_distance);
        parTV = itemView.findViewById(R.id.tv_par);
        scoreTV = itemView.findViewById(R.id.tv_score);
    }
}
