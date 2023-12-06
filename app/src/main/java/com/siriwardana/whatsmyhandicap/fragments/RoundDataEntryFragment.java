package com.siriwardana.whatsmyhandicap.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.siriwardana.whatsmyhandicap.R;

public class RoundDataEntryFragment extends Fragment {

    private EditText parET, distanceET;
    private TextView holeNumTV, holeScoreTV, totalScoreTV;
    private ImageButton addStrokeIB, minusStrokeIB;
    private Button prevHoleBTN, nextHoleBTN;
    private int numHoles, roundId, userId, currentHole = 1;


    public RoundDataEntryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_round_data_entry, container, false);

        parET = (EditText)  view.findViewById(R.id.et_par);
        distanceET = (EditText) view.findViewById(R.id.et_distance);

        holeNumTV = (TextView) view.findViewById(R.id.tv_hole_num);
        holeScoreTV = (TextView) view.findViewById(R.id.tv_hole_score);
        totalScoreTV = (TextView) view.findViewById(R.id.tv_total_score);

        minusStrokeIB = (ImageButton) view.findViewById(R.id.ib_minus_stroke);
        addStrokeIB = (ImageButton) view.findViewById(R.id.ib_plus_stroke);

        prevHoleBTN = (Button) view.findViewById(R.id.btn_prev_hole);
        nextHoleBTN = (Button) view.findViewById(R.id.btn_next_hole);

        Bundle test = this.getArguments();
        if(test != null) {
            numHoles = test.getInt("numHoles");
            roundId = test.getInt("roundId");
            userId = test.getInt("userId");

        }

        loadHoleData();

        nextHoleBTN.setOnClickListener(v ->
                //Todo: Validate all data is entered
                //Todo: Store the hole data in the db
                //Todo: update the fragment with new data.

                ((onRoundDataEntryFragmentButtonClickListener) requireActivity())
                        .onNextHoleButtonClicked()
        );





        return view;
    }

    private void loadHoleData() {
        if (currentHole == 1) {
            //Todo: code for the first hole
            totalScoreTV.setText("0");
        } else if (currentHole == numHoles) {
            //Todo: code for the last hole
        }

        holeNumTV.setText(Integer.toString(currentHole));
        holeScoreTV.setText("0");

    }

    public interface onRoundDataEntryFragmentButtonClickListener {
        void onNextHoleButtonClicked();
        void onPrevHoleButtonClicked();
        void onMinusStrokeButtonClicked();
        void onPlusStrokeButtonClicked();
    }

}