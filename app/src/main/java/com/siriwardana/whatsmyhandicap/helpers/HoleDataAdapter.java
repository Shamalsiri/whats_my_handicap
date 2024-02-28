package com.siriwardana.whatsmyhandicap.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.siriwardana.whatsmyhandicap.R;
import com.siriwardana.whatsmyhandicap.database.Hole;

import java.util.List;

public class HoleDataAdapter extends RecyclerView.Adapter<HoleDataViewHolder> {

    Context context;
    List<Hole> holes;

    public HoleDataAdapter(Context context, List<Hole> holes) {
        this.context = context;
        this.holes = holes;
    }

    @NonNull
    @Override
    public HoleDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HoleDataViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.hole_data_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HoleDataViewHolder holder, int position) {

        int distance = holes.get(position).getDistance();
        int par = holes.get(position).getPar();
        int score = holes.get(position).getHoleScore();

        holder.holeTV.setText(String.valueOf(holes.get(position).getHoleNumber()));
        holder.distanceTV.setText(distance > 0 ? String.valueOf(distance) : "-");
        holder.parTV.setText(String.valueOf(par));
        holder.scoreTV.setText(String.valueOf(par + score));
    }

    @Override
    public int getItemCount() {
        return holes.size();
    }
}
