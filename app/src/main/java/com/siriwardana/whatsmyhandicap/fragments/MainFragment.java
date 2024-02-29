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

public class MainFragment extends Fragment {

    private final String TAG = MainFragment.class.getName();
    private Button newRoundBTN, prevScoresBTN;
    private TextView handicapTV, handicapNoteTV;
    private ImageButton settingsIB, logoutIB;
    private int userId;

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
     *
     * @return
     */
    private double calculateHandicap() {
        Log.d(TAG, "calculateHandicap: Calculating User Handicap");
        // Todo: Calculate Handicap
        return 0.0;
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