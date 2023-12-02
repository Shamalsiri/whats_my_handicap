package com.siriwardana.whatsmyhandicap.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.siriwardana.whatsmyhandicap.R;
import com.siriwardana.whatsmyhandicap.database.DatabaseSingleton;
import com.siriwardana.whatsmyhandicap.database.Round;

public class NewRoundFragment extends Fragment {

    private Button backBTN, startRoundBTN;
    private EditText clubNameET, courseNameET;
    private TextView clubNameLabelTV, numHolesLabelTV;
    private RadioGroup holesRG;
    private RadioButton checkedRadioButton;
    private String clubName, courseName;
    private int numHoles, userId, roundId;
    private DatabaseSingleton dbSingleton;

    public NewRoundFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_round, container, false);
        dbSingleton = DatabaseSingleton.getDBInstance(getContext().getApplicationContext());
        Bundle bundle = getArguments();
        if(bundle != null) {
            userId = bundle.getInt("userId");
        }

        backBTN = (Button) view.findViewById(R.id.btn_nr_back);
        startRoundBTN = (Button) view.findViewById(R.id.btn_nr_start_round);

        clubNameET = (EditText) view.findViewById(R.id.et_club_name);
        courseNameET = (EditText) view.findViewById(R.id.et_course_name);

        clubNameLabelTV = (TextView) view.findViewById(R.id.tv_club_name_label);
        numHolesLabelTV = (TextView) view.findViewById(R.id.tv_num_holes_label);

        holesRG = (RadioGroup) view.findViewById(R.id.rg_num_holes);

        backBTN.setOnClickListener(v->
                        ((onNewRoundButtonClickListener)requireActivity()).onBackButtonClicked()
                );

        startRoundBTN.setOnClickListener(v -> {
            clubNameLabelTV.setVisibility(View.GONE);
            numHolesLabelTV.setVisibility(View.GONE);

            clubName = clubNameET.getText().toString().trim();
            courseName = courseNameET.getText().toString().trim();

            boolean canStart;
            canStart = validateNewRoundForm(view);

            if(canStart) {
                Round newRound = new Round();
                newRound.setUserId(123);
                newRound.setClubName(clubName);
                newRound.setCourseName(courseName);
                newRound.setNumHoles(numHoles);
                dbSingleton.RoundDao().insert(newRound);
                roundId = dbSingleton.RoundDao().getLatestRoundId();
            }

            ((onNewRoundButtonClickListener) requireActivity()).onStartButtonClicked(canStart, roundId);
        });
        return view;
    }

    private boolean validateNewRoundForm(View v) {

        boolean canStart = true;
        if(clubName.length() < 2) {
            clubNameLabelTV.setVisibility(View.VISIBLE);
            canStart = false;
        }

        int checkedRB = holesRG.getCheckedRadioButtonId();
        if (checkedRB != -1 ){
            checkedRadioButton = (RadioButton) v.findViewById(checkedRB);
            if(checkedRadioButton.getText().toString().equalsIgnoreCase("9 Holes")) {
                numHoles = 9;
            } else {
                numHoles = 18;
            }
        } else {
            numHolesLabelTV.setVisibility(View.VISIBLE);
            canStart = false;
        }

        return canStart;
    }

    public interface onNewRoundButtonClickListener {
        void onBackButtonClicked();
        void onStartButtonClicked(boolean canStart, int roundId);
    }
}