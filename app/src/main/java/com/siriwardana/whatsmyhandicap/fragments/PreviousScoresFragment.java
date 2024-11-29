package com.siriwardana.whatsmyhandicap.fragments;

import static java.lang.Integer.parseInt;

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
import com.siriwardana.whatsmyhandicap.helpers.HoleDataAdapter;
import com.siriwardana.whatsmyhandicap.helpers.ReloadScoresCallback;
import com.siriwardana.whatsmyhandicap.helpers.RoundData;
import com.siriwardana.whatsmyhandicap.helpers.RoundDataAdapter;

import java.util.ArrayList;
import java.util.List;

public class PreviousScoresFragment extends Fragment implements ReloadScoresCallback {
    private final String TAG = PreviousScoresFragment.class.getName();
    private int userId;
    private TextView bestTotalDistanceTV, bestTotalParTV,
            bestTotalScore, bestCourseName, bestDate;
    private Button exitBTN;
    private DatabaseSingleton databaseSingleton;
    private View view;
    private RecyclerView bestRoundRecyclerView, allScoresRecyclerView;
    private Context context;

    /**
     * Constructor
     */
    public PreviousScoresFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_previous_scores, container, false);
        Bundle bundle = getArguments();
        this.userId = bundle.getInt("userId");

        bestRoundRecyclerView = view.findViewById(R.id.rv_hole_data);
        allScoresRecyclerView = view.findViewById(R.id.rv_all_rounds);
        bestTotalDistanceTV = view.findViewById(R.id.tv_round_distance);
        bestTotalParTV = view.findViewById(R.id.tv_round_par);
        bestTotalScore = view.findViewById(R.id.tv_round_total_score);
        bestCourseName = view.findViewById(R.id.tv_best_course_name);
        bestDate = view.findViewById(R.id.tv_best_round_date);
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
                Log.d(TAG, "onCreate: Exit button clicked");
                requireActivity().onBackPressed();
            }
        });

        return view;
    }

    /**
     * Update the Recycler View with all Round data for the logged in user
     *
     * @param context
     */
    private void updateAllRoundRecyclerView(Context context) {
        Log.d(TAG, "updateAllRoundRecyclerView: Updating Recycler View with Round Data");
        List<RoundData> roundDataList = generateAllRoundData();
        allScoresRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        allScoresRecyclerView.setAdapter(new RoundDataAdapter(context, roundDataList, this));
    }

    /**
     * Update the Best Round Data
     *
     * @param context
     */
    private void updateBestRoundRecyclerView(Context context) {
        Log.d(TAG, "updateBestRoundRecyclerView: Updating best round data");
        int roundId = databaseSingleton.RoundDao().getBestRoundIdByUser(userId);
        List<Hole> holes = databaseSingleton.HoleDao().getHolesByRound(roundId);
        updateBestRoundTotal(holes, roundId);
        bestRoundRecyclerView.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false));
        bestRoundRecyclerView.setAdapter(new HoleDataAdapter(context, holes));
    }

    /**
     * Generate all the round data for the user
     *
     * @return
     */
    private List<RoundData> generateAllRoundData() {
        Log.d(TAG, "generateAllRoundData: Generating all Round Data for the Logged in User");
        List<RoundData> roundDataList = new ArrayList<RoundData>();
        List<Round> roundList = databaseSingleton.RoundDao().getRoundsByUser(userId);
        List<Hole> holeList;
        for (int i = 0; i < roundList.size(); i++) {
            Round round = roundList.get(i);
            holeList = databaseSingleton.HoleDao().getHolesByRound(round.getRoundId());

            if (!holeList.isEmpty()) {
                roundDataList.add(new RoundData(holeList, round));
            }
        }

        return roundDataList;
    }

    /**
     * Update the Best Round total data including:
     * Total Par
     * Total Score
     * Total Distance
     * Round Club and Course Name
     *
     * @param bestRound
     * @param roundId
     */
    public void updateBestRoundTotal(List<Hole> bestRound, int roundId) {
        Hole hole;
        Round round;

        round = databaseSingleton.RoundDao().getRoundById(roundId);
        int size = bestRound.size();
        if (size == 0) {
            Log.d(TAG, "updateBestRoundTotal: No Round Data in the DB yet");
            bestTotalScore.setText("");
            bestTotalParTV.setText("");
            bestTotalDistanceTV.setText("");
            bestCourseName.setText("");
            bestDate.setText("");
            return;
        }
        boolean distanceNotEntered = false;
        int totalPar = 0;
        int totalScore = 0;
        int totalDistance = 0;

        for (int i = 0; i < size; i++) {
            hole = bestRound.get(i);

            if (hole.getDistance() == 0) {
                Log.d(TAG, "updateBestRoundTotal: Distance NOT Entered for hole: " + (i + 1));
                distanceNotEntered = true;
            }
            totalScore = totalScore + hole.getHoleScore();
            totalPar = totalPar + hole.getPar();
            totalDistance = totalDistance + hole.getDistance();
        }

        String courseName = round.getClubName() + " \n" + round.getCourseName() + " Course";
        bestTotalScore.setText(String.valueOf(totalScore));
        bestTotalParTV.setText(String.valueOf(totalPar));
        bestTotalDistanceTV.setText(distanceNotEntered ? "-" : String.valueOf(totalDistance));
        bestCourseName.setText(courseName);
        bestDate.setText(round.getDate());
    }

    /**
     * Implementation of the Callback
     */
    @Override
    public void reloadPreviousScoreUI() {
        Log.d(TAG, "reloadPreviousScoreUI: Callback executed");
        updateAllRoundRecyclerView(context);
        updateBestRoundRecyclerView(context);
    }
}
