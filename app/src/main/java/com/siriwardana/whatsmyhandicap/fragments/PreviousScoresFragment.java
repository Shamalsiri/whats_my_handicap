package com.siriwardana.whatsmyhandicap.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.siriwardana.whatsmyhandicap.R;
import com.siriwardana.whatsmyhandicap.database.DatabaseSingleton;
import com.siriwardana.whatsmyhandicap.database.Hole;
import com.siriwardana.whatsmyhandicap.database.Round;
import com.siriwardana.whatsmyhandicap.helpers.ReloadPreviousScoreUICallback;
import com.siriwardana.whatsmyhandicap.helpers.HoleDataAdapter;
import com.siriwardana.whatsmyhandicap.helpers.RoundData;
import com.siriwardana.whatsmyhandicap.helpers.RoundDataAdapter;

import java.util.ArrayList;
import java.util.List;

public class PreviousScoresFragment extends Fragment implements ReloadPreviousScoreUICallback {
    private int userId;
    private TextView bestTotalDistanceTV, bestTotalParTV, bestTotalScore, bestCourseName;
    private Button exitBTN;
    private DatabaseSingleton databaseSingleton;
    private View view;
    private RecyclerView bestHoleRecyclerView, allScoresRecyclerView;
    private Context context;


    public PreviousScoresFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_previous_scores, container, false);
        Bundle bundle = getArguments();
        this.userId = bundle.getInt("userId");

        bestHoleRecyclerView = view.findViewById(R.id.rv_hole_data);
        allScoresRecyclerView = view.findViewById(R.id.rv_all_rounds);
        bestTotalDistanceTV = view.findViewById(R.id.tv_round_distance);
        bestTotalParTV = view.findViewById(R.id.tv_round_par);
        bestTotalScore = view.findViewById(R.id.tv_round_total_score);
        bestCourseName = view.findViewById(R.id.tv_best_course_name);
        exitBTN = view.findViewById(R.id.btn_exit_home);

        context = getContext();
        databaseSingleton = DatabaseSingleton.getDBInstance(context);

        //best round data
        updateBestRoundRecyclerView(context);

        //all round data
        updateAllRoundRecyclerView(context);

        exitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().onBackPressed();
            }
        });

        return view;
    }

    private void updateAllRoundRecyclerView(Context context) {
        List<RoundData> roundDataList = generateAllRoundData();
        allScoresRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        allScoresRecyclerView.setAdapter(new RoundDataAdapter(context, roundDataList, this));
    }

    private void updateBestRoundRecyclerView(Context context) {
        int roundId = databaseSingleton.RoundDao().getBestRoundIdByUser(userId);
        List<Hole> holes = databaseSingleton.HoleDao().getHolesByRound(roundId);
        updateBestHoleTotal(holes, roundId);
        bestHoleRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        bestHoleRecyclerView.setAdapter(new HoleDataAdapter(context, holes));
    }

    private List<RoundData> generateAllRoundData() {
        List<RoundData> roundDataList = new ArrayList<RoundData>();
        List<Round> roundList = databaseSingleton.RoundDao().getRoundsByUser(userId);
        List<Hole>  holeList;
        for(int i = 0; i < roundList.size(); i++) {
            Round round = roundList.get(i);
            holeList = databaseSingleton.HoleDao().getHolesByRound(round.getRoundId());

            if (holeList.size() > 0) {
                roundDataList.add(new RoundData(holeList, round));
            }
        }

        return roundDataList;
    }

    public void updateBestHoleTotal(List<Hole> bestRound, int roundId) {
        Hole hole;
        Round round;

        round = databaseSingleton.RoundDao().getRoundById(roundId);
        int size = bestRound.size();
        if(size == 0) {
            Log.d("SSIRI", "No Data in the db yet");
            return;
        }
        boolean distanceNotEntered = false;
        int totalPar = 0;
        int totalScore = 0;
        int totalDistance = 0;

        for (int i = 0; i < size; i++) {
            hole = bestRound.get(i);

            if(hole.getDistance() == 0) { distanceNotEntered = true; }
            totalScore = totalScore + hole.getHoleScore();
            totalPar = totalPar + hole.getPar();
            totalDistance = totalDistance + hole.getDistance();
        }

        String courseName = round.getClubName() + " | " + round.getCourseName() + " Course";
        bestTotalScore.setText(String.valueOf(totalScore));
        bestTotalParTV.setText(String.valueOf(totalPar));
        bestTotalDistanceTV.setText(distanceNotEntered ? "-" : String.valueOf(totalDistance));
        bestCourseName.setText(courseName);



    }

    @Override
    public void reloadUI() {
        updateAllRoundRecyclerView(context);
        updateBestRoundRecyclerView(context);
    }
}
