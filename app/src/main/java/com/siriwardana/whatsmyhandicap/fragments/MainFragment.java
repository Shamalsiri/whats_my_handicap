package com.siriwardana.whatsmyhandicap.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.siriwardana.whatsmyhandicap.R;
import com.siriwardana.whatsmyhandicap.database.DatabaseSingleton;
import com.siriwardana.whatsmyhandicap.database.Hole;
import com.siriwardana.whatsmyhandicap.database.Round;

import java.util.List;

public class MainFragment extends Fragment {

    private final String TAG = MainFragment.class.getName();
    private Button newRoundBTN, prevScoresBTN;
    private TextView handicapTV, handicapNoteTV;
    private ImageButton settingsIB, logoutIB;
    private int userId;
    private String handicap;

    /**
     * Constructor
     */
    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        Bundle bundle = getArguments();
        assert bundle != null;
        this.userId = bundle.getInt("userId");

        newRoundBTN = view.findViewById(R.id.btn_new_round);
        prevScoresBTN = view.findViewById(R.id.btn_prev_scores);

        handicapTV = view.findViewById(R.id.tv_handicap);
        handicapNoteTV = view.findViewById(R.id.tv_handicap_note);

        settingsIB = view.findViewById(R.id.ib_settings);
        logoutIB = view.findViewById(R.id.ib_logout);
        calculateHandicap();

        newRoundBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMainFragmentButtonClickListener listener =
                        (onMainFragmentButtonClickListener) requireActivity();
                listener.onMainFragmentNewRoundButtonClicked();
            }
        });

        // Lambda form of the button click listener (compare to button click above)
        prevScoresBTN.setOnClickListener(v ->
                ((onMainFragmentButtonClickListener) requireActivity())
                        .onMainFragmentPreviousScoreButtonClicked(userId)
        );

        settingsIB.setOnClickListener(v ->
                ((onMainFragmentButtonClickListener) requireActivity()).onMainFragmentSettingsButtonClicked());

        logoutIB.setOnClickListener(v ->
                ((onMainFragmentButtonClickListener) requireActivity()).onMainFragmentLogoutButtonClicked());

        return view;
    }

    /**
     * Calculate the user Handicap;
     */
    private void calculateHandicap() {
        Log.d(TAG, "calculateHandicap: Calculating User Handicap");
        DatabaseSingleton databaseSingleton = DatabaseSingleton.getDBInstance(getContext());
        List<Round> bestRounds = databaseSingleton.RoundDao().getBest8RoundsByUserID(userId);
        Round round;
        Hole hole;
        int roundId;
        int totalPar;
        int totalScore;
        int size;

        if (bestRounds.size() != 8) {
            Log.w(TAG, "The list must contain exactly 8 rounds.");
            return;
        }

        // Calculate score differentials for each round
        double[] differentials = new double[8];
        for (int i = 0; i < bestRounds.size(); i++) {

            totalPar = 0;
            totalScore = 0;

            roundId = bestRounds.get(i).getRoundId();
            round = databaseSingleton.RoundDao().getRoundById(roundId);

            size = round.getNumHoles();
            for (int j = 0; j < size; j++) {
                hole = databaseSingleton.HoleDao().getHoleByRound(roundId, j + 1 );

                if (hole == null) {
                    databaseSingleton.RoundDao().delete(round);
                    databaseSingleton.HoleDao().deleteHolesByRoundID(roundId);
                    calculateHandicap();
                    return;
                }

                totalScore = totalScore + hole.getHoleScore();
                totalPar = totalPar + hole.getPar();
            }

            if (totalPar != 0 ) {
                // Adjust for 9-hole rounds by doubling scores and par
                if (size == 9) {
                    totalScore *= 2;
                    totalPar *= 2;
                } else if (size != 18) {
                    Log.e(TAG,"Rounds must be either 9 or 18 holes.");
                }

                if (i % 2 == 0) {
                    totalScore = totalScore - 1;
                }

                double adjustedGrossScore = totalScore + totalPar;
                double courseRating = totalPar;
                double slopeRating = round.getSlopeRating();

                // Formula: (Adjusted Gross Score - Course Rating) / Slope Rating * 113
                differentials[i] = ((adjustedGrossScore - courseRating) / slopeRating) * 113;
            }
        }

        // Average the best 2 differentials (lowest values)
        double bestDifferentialsSum = differentials[0] + differentials[1] +
                differentials[2] + differentials[3] + differentials[4] + differentials[5] +
                differentials[6] + differentials[7];
        double averageBestDifferentials = bestDifferentialsSum / 8;

        // Calculate handicap index
        double handicapIndex = averageBestDifferentials * 0.96;

        handicap = String.valueOf(Math.round(handicapIndex * 100.0));
        handicap = handicap.substring(0, 2) + "." + handicap.substring(2);

        handicapTV.setText(handicap);
        handicapNoteTV.setVisibility(View.GONE);
    }

    /**
     * Store User Handicap in the Database
     */
    private void storeUserHandicap() {
        Log.d(TAG, "storeUserHandicap: Storing User Handicap in Database");
        // Todo: Store User Handicap in the DB
    }

    public interface onMainFragmentButtonClickListener {
        void onMainFragmentNewRoundButtonClicked();

        void onMainFragmentPreviousScoreButtonClicked(int userId);

        void onMainFragmentSettingsButtonClicked();

        void onMainFragmentLogoutButtonClicked();
    }
}