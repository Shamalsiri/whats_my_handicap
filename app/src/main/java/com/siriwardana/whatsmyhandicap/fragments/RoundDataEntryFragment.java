package com.siriwardana.whatsmyhandicap.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.siriwardana.whatsmyhandicap.R;
import com.siriwardana.whatsmyhandicap.database.DatabaseSingleton;
import com.siriwardana.whatsmyhandicap.database.Hole;

import java.util.List;

public class RoundDataEntryFragment extends Fragment {

    private DatabaseSingleton dbSingleton;
    private int userId, roundId, holeNum, numStrokes, totalScore, totalPar, roundScore;

    EditText parET, distanceET;
    TextView holeNumTV, strokeCountTV, totalScoreTV, errorMsg1TV, errorMsg2TV;
    ImageView minusIV, plusIV;
    Button prevBTN, nextBTN;

    final private String MODE_NEW_ROUND = "new_round";
    final private String MODE_SINGLE_HOLE = "single_hole";
    final private String MODE_EDIT_ROUND = "edit_round";

    public RoundDataEntryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_round_data_entry, container, false);
        dbSingleton = DatabaseSingleton.getDBInstance(getContext().getApplicationContext());
        Bundle bundle = getArguments();
        if (bundle != null) {
            userId = bundle.getInt("userId");
            roundId = bundle.getInt("roundId");
        }

        parET = (EditText) view.findViewById(R.id.et_par);
        distanceET = (EditText) view.findViewById(R.id.et_distance_hole);

        holeNumTV = (TextView) view.findViewById(R.id.tv_hole_num);
        strokeCountTV = (TextView) view.findViewById(R.id.tv_stroke_count);
        totalScoreTV = (TextView) view.findViewById(R.id.tv_total_score);
        errorMsg1TV = (TextView) view.findViewById(R.id.tv_error_msg1);
        errorMsg2TV = (TextView) view.findViewById(R.id.tv_error_msg2);

        minusIV = (ImageView) view.findViewById(R.id.iv_minus);
        plusIV = (ImageView) view.findViewById(R.id.iv_plus);
        prevBTN = (Button) view.findViewById(R.id.btn_prev);
        nextBTN = (Button) view.findViewById(R.id.btn_next);

        String tempMode = "new_round";

        switch (tempMode) {
            case MODE_NEW_ROUND:
                Log.d("SSIRI", "New Round");
                startNewRound();
                break;

            case MODE_SINGLE_HOLE:
                Log.d("SSIRI", "Single Hole");
                break;

            case MODE_EDIT_ROUND:
                Log.d("SSIRI", "Edit Round");
        }

        prevBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (prevBTN.getText().toString().equalsIgnoreCase("Exit")) {
                    ((onRoundDataEntryButtonClickListener) requireActivity())
                            .onPrevButtonClicked(true);
                } else {
                    holeNum--;
                    loadHole(holeNum);
                }
            }
        });

        nextBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nextBTN.getText().toString().equalsIgnoreCase("Next Hole")) {
                    if (validateData()) {
                        Log.d("SSIRI", "Validated");
                        saveHoleData();
                        holeNum++;
                        loadNextHole();

                    }
                }
            }
        });

        plusIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numStrokes++;
                strokeCountTV.setText(String.valueOf(numStrokes));

            }
        });


        minusIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (numStrokes > 0) {
                    numStrokes--;
                    strokeCountTV.setText(String.valueOf(numStrokes));
                }
            }
        });


        return view;
    }

    private void loadHole(int holeNum) {
        Hole hole = (Hole) dbSingleton.HoleDao().getHoleByRound(roundId, holeNum);

        holeNumTV.setText(Integer.toString(holeNum));
        Log.d("SSIRI", "hole par: " + hole.getPar());

    }

    private boolean validateData() {
        boolean validated = true;

        if (parET.getText().toString().length() == 0) {
            errorMsg1TV.setVisibility(View.VISIBLE);
            errorMsg1TV.setText("Enter Par and Distance");
            validated = false;
        } else {
            errorMsg1TV.setVisibility(View.GONE);
            int par = Integer.valueOf(parET.getText().toString().trim());
            if (par < 3 || par > 5) {
                errorMsg1TV.setVisibility(View.VISIBLE);
                errorMsg1TV.setText("Invalid par value");
                validated = false;
            }

        }


        int strokeCount = Integer.valueOf(strokeCountTV.getText().toString().trim());
        if (strokeCount < 1) {
            errorMsg2TV.setVisibility(View.VISIBLE);
            errorMsg2TV.setText("Enter the Stroke Count");
            validated = false;
        } else {
            errorMsg2TV.setVisibility(View.GONE);
        }


        if (validated) {
            errorMsg1TV.setVisibility(View.GONE);
            errorMsg2TV.setVisibility(View.GONE);
            return true;
        } else {
            return false;
        }
    }

    private void saveHoleData() {
        int par = Integer.valueOf(parET.getText().toString().trim());
        int distance = distanceET.getText().toString().length() > 0 ?
                Integer.valueOf(distanceET.getText().toString().trim()) :
                0;
        totalScore = totalScore + numStrokes;
        totalPar = totalPar + par;
        roundScore = totalScore - totalPar;

        Hole hole = new Hole();
        hole.setRoundId(roundId);
        hole.setUserId(userId);
        hole.setHoleNumber(holeNum);
        hole.setPar(par);
        hole.setDistance(distance);
        hole.setStrokeCount(numStrokes);
        hole.setScore(roundScore);
        dbSingleton.HoleDao().insert(hole);



    }

    private void loadNextHole() {
        if (holeNum == 2) {
            prevBTN.setText("Prev Hole");
            loadNewHole();
        } else if (holeNum == 18) {
            nextBTN.setText("Save Round");
            loadNewHole();
        } else {
            loadNewHole();
        }
    }

    private void loadNewHole() {
        holeNumTV.setText(Integer.toString(holeNum));
        parET.setText("");
        distanceET.setText("");
        strokeCountTV.setText("0");
        if (totalScore == 0) {
            totalScoreTV.setText("E");
        } else {
            totalScoreTV.setText(Integer.toString(totalScore));
        }
    }

    private void loadHole(int holeNum, int par, @Nullable int distance, int strokeCount, int score) {
        holeNumTV.setText(Integer.toString(holeNum));
        parET.setText(Integer.toString(par));
        distanceET.setText(Integer.toString(distance));
        strokeCountTV.setText(Integer.toString(strokeCount));
        totalScoreTV.setText(Integer.toString(score));
    }

    private void startNewRound() {
        holeNum = 1;
        numStrokes = 0;
        totalScore = 0;
        totalPar = 0;
        totalScoreTV.setText("E");
        strokeCountTV.setText("0");
        holeNumTV.setText(Integer.toString(holeNum));
        prevBTN.setText("Exit");
        nextBTN.setText("Next Hole");

    }

    public interface onRoundDataEntryButtonClickListener {
        void onPrevButtonClicked(boolean isExit);

        void onNextButtonClicked();
    }


}