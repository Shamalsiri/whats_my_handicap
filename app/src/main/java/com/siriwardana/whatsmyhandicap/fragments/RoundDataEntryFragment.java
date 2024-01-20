package com.siriwardana.whatsmyhandicap.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
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
import java.util.Objects;

public class RoundDataEntryFragment extends Fragment {

    private EditText parET, distanceET;
    private TextView holeNumTV, strokeCountTV, roundScoreTV, errorMsg1TV, errorMsg2TV;
    private int roundId, userId, currentHole, numHolesThisRound, numStrokes, roundScore;
    private ImageView minusIV, plusIV;
    private Button prevBTN, nextBTN;

    private DatabaseSingleton dbSingleton;

    public RoundDataEntryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_round_data_entry, container, false);
        dbSingleton = DatabaseSingleton.getDBInstance(getContext().getApplicationContext());

        parET = view.findViewById(R.id.et_par);
        distanceET = view.findViewById(R.id.et_hole_distance);

        holeNumTV = view.findViewById(R.id.tv_hole_num);
        strokeCountTV = view.findViewById(R.id.tv_stroke_count);
        roundScoreTV = view.findViewById(R.id.tv_round_score);
        errorMsg1TV = view.findViewById(R.id.tv_error_msg1);
        errorMsg2TV = view.findViewById(R.id.tv_error_msg2);

        minusIV = view.findViewById(R.id.iv_minus);
        plusIV = view.findViewById(R.id.iv_plus);

        prevBTN = view.findViewById(R.id.btn_prev);
        nextBTN = view.findViewById(R.id.btn_next);

        final String MODE_NEW_ROUND = "new_round"; // default case
        final String MODE_SINGLE_HOLE = "single_hole";
        final String MODE_EDIT_ROUND = "edit_round";

        String mode = "";
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            numHolesThisRound = bundle.getInt("numHoles");
            roundId = bundle.getInt("roundId");
            userId = bundle.getInt("userId");
            mode = bundle.getString("mode");
        }

        switch (mode) {
            case MODE_SINGLE_HOLE:
                Log.d("SSIRI", "Open Round Data Fragment in Mode: Single Hole");
                break;

            case MODE_EDIT_ROUND:
                Log.d("SSIRI", "Open Round Data Fragment in Mode: Edit Round");
                break;
            default:
                Log.d("SSIRI", "Open Round Data Fragment in Mode: New Round");
                startNewRound();
                break;
        }

        prevBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (prevBTN.getText().toString().equalsIgnoreCase("Exit")) {
                    Log.d("SSIRI", "Exit Button Clicked");
                    ((onRoundDataEntryButtonClickListener) requireActivity())
                            .onPrevButtonClicked(true);
                } else {
                    Log.d("SSIRI", "Prev Hole Button Clicked");
                    if (currentHole > 1) {
                        currentHole--;
                        loadHoleFromDB(currentHole);
                    }
                }
            }
        });

        nextBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String btnText = nextBTN.getText().toString();
                if (btnText.equalsIgnoreCase("Next Hole")) {
                    Log.d("SSIRI", "Next Hole Button Clicked");
                    if (validateData()) {
                        saveHoleData(currentHole);
                        currentHole++;
                        loadHole(currentHole);

                    }
                } else if (btnText.equalsIgnoreCase("Save Round")) {
                    Log.d("SSIRI", "Save Round Hole Button Clicked");
                    if (validateData()) {
                        saveHoleData(currentHole);
                    }

                    List<Hole> holeList = dbSingleton.HoleDao().getHolesByRound(roundId);
                    int numHolesInRound = holeList.size();
                    if (numHolesInRound % 9 == 0) {
                        //Todo: Open confirm score fragment
                        Log.d("SSIRI", "Opening confirm score fragment");
                    }

                    ((onRoundDataEntryButtonClickListener) requireActivity())
                            .onPrevButtonClicked(true);

                }
            }
        });


        plusIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("SSIRI", "Add Stroke Button clicked");
                numStrokes++;
                strokeCountTV.setText(String.valueOf(numStrokes));

            }
        });

        minusIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (numStrokes > 0) {
                    Log.d("SSIRI", "Minus Stroke Button clicked");
                    numStrokes--;
                    strokeCountTV.setText(String.valueOf(numStrokes));
                }
            }
        });


        return view;
    }

    /**
     * save hole data
     *
     * @param holeNum
     */
    private void saveHoleData(int holeNum) {
        if (holeDataExist(holeNum)) {
            Hole hole = dbSingleton.HoleDao().getHoleByRound(roundId, holeNum);
            updateHoleData(hole);
        } else {
            saveHoleData(new Hole());
        }
    }

    /**
     * Load new hole
     */
    private void loadNewHole() {
        setNewHoleUI();
    }

    /**
     * load hole
     *
     * @param holeNum
     */
    private void loadHole(int holeNum) {
        if (holeDataExist(holeNum)) {
            loadHoleFromDB(holeNum);

        } else {
            loadNewHole();
        }
    }

    /**
     * Check if hole data exist in the db
     *
     * @param holeNum
     * @return
     */
    private boolean holeDataExist(int holeNum) {
        Hole hole;
        hole = dbSingleton.HoleDao().getHoleByRound(roundId, holeNum);

        return hole != null ? true : false;
    }

    /**
     * Setup the first hole for a new round
     */
    private void startNewRound() {
        currentHole = 1;
        numStrokes = 0;
        roundScore = 0;
        setHoleSpecificBtnUI();

        roundScoreTV.setText("E");
        strokeCountTV.setText("0");
    }

    /**
     * Setup the button UI changes needed for each hole
     * update the hole number on UI
     */
    private void setHoleSpecificBtnUI() {
        if (currentHole == 1) {
            prevBTN.setText(R.string.exit);
            nextBTN.setText(R.string.next_hole);
        } else if (currentHole == 2) {
            prevBTN.setText(R.string.prev_hole);
        } else if (currentHole == 9 && numHolesThisRound == 9) {
            nextBTN.setText(R.string.save_round);
        } else if (currentHole == 18) {
            nextBTN.setText(R.string.save_round);
        }
    }

    /**
     * Setup the UI for a new hole
     * Reset par, distance and stroke count
     */
    private void setNewHoleUI() {
        setHoleSpecificBtnUI();
        holeNumTV.setText(String.valueOf(currentHole));
        numStrokes = 0;
        parET.setText(null);
        distanceET.setText(null);
        strokeCountTV.setText(String.valueOf(numStrokes));

        calculateRoundScore(currentHole - 1);
        if (roundScore == 0) {
            roundScoreTV.setText("E");
        } else {
            roundScoreTV.setText(String.valueOf(roundScore));
        }
    }

    /**
     * Load hole data from the db
     *
     * @param holeNum
     */
    private void loadHoleFromDB(int holeNum) {
        setNewHoleUI();
        Hole hole;
        hole = dbSingleton.HoleDao().getHoleByRound(roundId, holeNum);
        numStrokes = hole.getStrokeCount();

        parET.setText(String.valueOf(hole.getPar()));
        distanceET.setText(String.valueOf(hole.getDistance()));
        strokeCountTV.setText(String.valueOf(numStrokes));
        if (holeNum == 1) {
            roundScoreTV.setText("E");
        } else {
            calculateRoundScore(holeNum - 1);
        }
    }

    /**
     * Validate that the all necessary data is entered by the user
     * Show error messages if needed.
     *
     * @return
     */
    private boolean validateData() {
        boolean validated = true;

        if (parET.getText().toString().length() == 0) {
            errorMsg1TV.setVisibility(View.VISIBLE);
            errorMsg1TV.setText(R.string.enter_par_and_distance);
            validated = false;
        } else {
            int par = Integer.parseInt(parET.getText().toString().trim());
            if (par < 3 || par > 5) {
                errorMsg1TV.setVisibility(View.VISIBLE);
                errorMsg1TV.setText(R.string.invalid_par_value);
                validated = false;
            }
        }

        int strokeCount = Integer.parseInt(strokeCountTV.getText().toString().trim());
        if (strokeCount < 1) {
            errorMsg2TV.setVisibility(View.VISIBLE);
            errorMsg2TV.setText(R.string.enter_the_stroke_count);
            validated = false;
        }

        if (validated) {
            errorMsg1TV.setVisibility(View.GONE);
            errorMsg2TV.setVisibility(View.GONE);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Save hole data to the database
     *
     * @param hole
     */
    private void saveHoleData(Hole hole) {
        hole = createHoleObject(hole);

        Log.d("SSIRI", "Saving values for hole: " + hole.getHoleNumber());
        dbSingleton.HoleDao().insert(hole);
    }

    /**
     * Update the existing hole record
     *
     * @param hole
     */
    private void updateHoleData(Hole hole) {
        hole = createHoleObject(hole);

        Log.d("SSIRI", "Updating values for hole: " + hole.getHoleNumber());
        dbSingleton.HoleDao().update(hole);

        calculateRoundScore(currentHole);
    }

    /**
     * Setup a hole with all data in current fragment
     *
     * @param hole
     * @return
     */
    private Hole createHoleObject(Hole hole) {
        int par = Integer.parseInt(parET.getText().toString().trim());
        int distance = distanceET.getText().toString().length() > 0 ?
                Integer.parseInt(distanceET.getText().toString().trim()) :
                0;

        int holeScore = numStrokes - par;

        hole.setRoundId(roundId);
        hole.setUserId(userId);
        hole.setHoleNumber(currentHole);
        hole.setPar(par);
        hole.setDistance(distance);
        hole.setStrokeCount(numStrokes);
        hole.setHoleScore(holeScore);

        return hole;
    }

    /**
     * Calculate the roundScore so far.
     *
     * @param holeNum
     */
    private void calculateRoundScore(int holeNum) {
        int score = 0;
        Hole hole;
        if (holeNum != 1 || holeDataExist(1)) {
            for (int i = 1; i <= holeNum; i++) {
                hole = dbSingleton.HoleDao().getHoleByRound(roundId, i);
                score = score + hole.getHoleScore();
            }
        }

        roundScore = score;
    }


    public interface onRoundDataEntryButtonClickListener {
        void onPrevButtonClicked(boolean isExit);

        void onNextButtonClicked();
    }

}