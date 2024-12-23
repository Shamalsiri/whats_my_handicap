package com.siriwardana.whatsmyhandicap.helpers;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.siriwardana.whatsmyhandicap.R;
import com.siriwardana.whatsmyhandicap.database.Hole;
import com.siriwardana.whatsmyhandicap.database.Round;
import com.siriwardana.whatsmyhandicap.dialogs.DeleteRoundDialog;
import com.siriwardana.whatsmyhandicap.dialogs.EditHoleDialog;
import com.siriwardana.whatsmyhandicap.fragments.PreviousScoresFragment;

import java.util.List;

public class RoundDataAdapter extends RecyclerView.Adapter<RoundDataViewHolder> {

    Context context;
    List<RoundData> roundDataList;
    PreviousScoresFragment fragment;

    public RoundDataAdapter(Context context, List<RoundData> roundDataList, PreviousScoresFragment fragment) {
        this.context = context;
        this.roundDataList = roundDataList;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public RoundDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RoundDataViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.round_data_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RoundDataViewHolder holder, int position) {
        Round round = roundDataList.get(position).getRound();
        List<Hole> holes = roundDataList.get(position).getHoles();

        int roundId = round.getRoundId();
        String location = round.getClubName() + " \n" + round.getCourseName() + " Course";

        holder.holeDataRV.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false));
        holder.holeDataRV.setAdapter(new HoleDataAdapter(context, holes));

        holder.courseNameTV.setText(location);
        updateHoleTotal(holes, holder);

        holder.dateTV.setText(round.getDate());

        holder.editRoundIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("SSIRI", "Edit round at " + location + " with roundId " + roundId +  " Clicked");
                EditHoleDialog editRoundDialog = new EditHoleDialog(context, round);
                editRoundDialog.show();

                editRoundDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        ReloadScoresCallback reloadPreviousScoreUICallback = fragment;
                        reloadPreviousScoreUICallback.reloadPreviousScoreUI();

                    }
                });
            }
        });

        holder.deleteRoundIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("SSIRI", "Delete round at " + location + " with roundId " + roundId +  " Clicked");
                DeleteRoundDialog deleteRoundDialog = new DeleteRoundDialog(context, round);
                deleteRoundDialog.show();

                deleteRoundDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        ReloadScoresCallback reloadPreviousScoreUICallback = fragment;
                        reloadPreviousScoreUICallback.reloadPreviousScoreUI();

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return roundDataList.size();
    }

    private void updateHoleTotal(List<Hole> holes, @NonNull RoundDataViewHolder holder) {
        Hole hole;
        boolean distanceNotEntered = false;
        int totalPar = 0;
        int totalScore = 0;
        int totalDistance = 0;

        for (int i = 0; i < holes.size(); i++) {
            hole = holes.get(i);

            if (hole.getDistance() == 0) {
                distanceNotEntered = true;
            }
            totalScore = totalScore + hole.getHoleScore();
            totalPar = totalPar + hole.getPar();
            totalDistance = totalDistance + hole.getDistance();
        }

        holder.totalScoreTV.setText(String.valueOf(totalScore));
        holder.totalParTV.setText(String.valueOf(totalPar));
        holder.totalDistanceTV.setText(distanceNotEntered ? "-" : String.valueOf(totalDistance));

    }
}
