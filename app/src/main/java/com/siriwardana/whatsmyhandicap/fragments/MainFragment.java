package com.siriwardana.whatsmyhandicap.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.siriwardana.whatsmyhandicap.R;

public class MainFragment extends Fragment {

    private Button newRoundBTN, prevScoresBTN;
    private TextView handicapTV, handicapNoteTV;
    private ImageButton settingsIB, logoutIB;
    private int userId;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        newRoundBTN = (Button) view.findViewById(R.id.btn_new_round);
        prevScoresBTN = (Button) view.findViewById(R.id.btn_prev_scores);

        handicapTV = (TextView) view.findViewById(R.id.tv_handicap);
        handicapNoteTV = (TextView) view.findViewById(R.id.tv_handicap_note);

        settingsIB = (ImageButton) view.findViewById(R.id.ib_settings);
        logoutIB = (ImageButton) view.findViewById(R.id.ib_logout);

        newRoundBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMainFragmentButtonClickListener listener =
                        (onMainFragmentButtonClickListener) requireActivity();
                listener.onNewRoundButtonClicked();
            }
        });

        // Lambda form of the button click listener (compare to button click above)
        prevScoresBTN.setOnClickListener(v ->
                ((onMainFragmentButtonClickListener) requireActivity())
                        .onPrevScoreButtonClicked(userId)
        );

        settingsIB.setOnClickListener(v ->
                ((onMainFragmentButtonClickListener) requireActivity()).onSettingsButtonClicked());

        logoutIB.setOnClickListener(v ->
                ((onMainFragmentButtonClickListener) requireActivity()).onLogoutButtonClicked());

        return view;
    }

    public interface onMainFragmentButtonClickListener {
        void onNewRoundButtonClicked();
        void onPrevScoreButtonClicked(int userId);
        void onSettingsButtonClicked();
        void onLogoutButtonClicked();
    }
}