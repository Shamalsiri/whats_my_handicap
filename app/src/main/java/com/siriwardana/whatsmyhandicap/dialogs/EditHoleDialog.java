package com.siriwardana.whatsmyhandicap.dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.siriwardana.whatsmyhandicap.R;
import com.siriwardana.whatsmyhandicap.database.Hole;
import com.siriwardana.whatsmyhandicap.database.Round;
import com.siriwardana.whatsmyhandicap.helpers.DataStorageHelper;

import java.util.ArrayList;
import java.util.List;

public class EditHoleDialog extends Dialog implements View.OnClickListener {

    private final String TAG = EditRoundDataDialog.class.getName();
    private TextView clubNameTV, courseNameTV, distanceTV, parTV, scoreTV;
    private final Round round;
    private Hole hole;
    private int currentHoleNumber;
    private final Context context;
    private final DataStorageHelper dataStorageHelper;

    /**
     * Constructor
     *
     * @param context
     * @param round
     */
    public EditHoleDialog(@NonNull Context context, Round round) {
        super(context);
        this.context = context;
        this.round = round;
        dataStorageHelper = new DataStorageHelper(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit_round);

        clubNameTV = findViewById(R.id.tv_club_name);
        courseNameTV = findViewById(R.id.tv_course_name);
        distanceTV = findViewById(R.id.tv_edit_distance);
        parTV = findViewById(R.id.tv_edit_par);
        scoreTV = findViewById(R.id.tv_edit_score);

        ImageButton editClubNameBTN = findViewById(R.id.btn_edit_club_name);
        editClubNameBTN.setOnClickListener(this);
        ImageButton editCourseNameBTN = findViewById(R.id.btn_edit_course_name);
        editCourseNameBTN.setOnClickListener(this);
        ImageButton editDistanceBTN = findViewById(R.id.btn_edit_distance);
        editDistanceBTN.setOnClickListener(this);
        ImageButton editParBTN = findViewById(R.id.btn_edit_par);
        editParBTN.setOnClickListener(this);
        ImageButton editScoreBTN = findViewById(R.id.btn_edit_score);
        editScoreBTN.setOnClickListener(this);

        ImageView closeIV = findViewById(R.id.iv_close);
        Spinner selectHoleSP = findViewById(R.id.sp_select_hole);

        int numHoles = round.getNumHoles();
        List<String> spinnerValues = new ArrayList<>();
        for (int i = 0; i < numHoles; i++) {
            spinnerValues.add("Hole " + (i + 1));
        }

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, spinnerValues);
        // Specify the layout to use when the list of choices appears
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        selectHoleSP.setAdapter(dataAdapter);

        updateRoundData();

        closeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        selectHoleSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "onItemSelected: Selected hole: " + i);
                currentHoleNumber = i + 1;
                updateUIByHole(currentHoleNumber);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    /**
     * Set the mode enum and opens edit dialog
     *
     * @param v
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        EditRoundDataDialog editDataDialog;
        EditModeEnums mode = null;

        switch (v.getId()) {
            case R.id.btn_edit_club_name:
                mode = EditModeEnums.EDIT_CLUB_NAME;
                break;
            case R.id.btn_edit_course_name:
                mode = EditModeEnums.EDIT_COURSE_NAME;
                break;
            case R.id.btn_edit_distance:
                mode = EditModeEnums.EDIT_DISTANCE;
                break;
            case R.id.btn_edit_par:
                mode = EditModeEnums.EDIT_PAR;
                break;
            case R.id.btn_edit_score:
                mode = EditModeEnums.EDIT_SCORE;
                break;
        }

        Log.d(TAG, "onClick: Showing Edit Data Dialog");
        editDataDialog = new EditRoundDataDialog(context, mode, hole, round);
        editDataDialog.show();

        editDataDialog.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                updateUIByHole(currentHoleNumber);
            }
        });


    }

    /**
     * Update Dialog UI by Hole
     *
     * @param holeNum
     */
    public void updateUIByHole(int holeNum) {
        Log.d(TAG, "updateUIByHole: Current Hole: " + holeNum);
        hole = dataStorageHelper.getHoleByRound(round.getRoundId(), holeNum);
        int score = hole.getHoleScore();
        distanceTV.setText(String.valueOf(hole.getDistance()));
        parTV.setText(String.valueOf(hole.getPar()));
        scoreTV.setText(score == 0 ? "E" : String.valueOf(score));

    }

    /**
     * Update Round Data
     */
    public void updateRoundData() {
        Log.d(TAG, "updateRoundData: Updating Dialog UI");
        clubNameTV.setText(round.getClubName());
        courseNameTV.setText(round.getCourseName());

        updateUIByHole(currentHoleNumber + 1);
    }
}
