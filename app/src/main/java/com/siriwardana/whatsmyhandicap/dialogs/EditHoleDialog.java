package com.siriwardana.whatsmyhandicap.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.siriwardana.whatsmyhandicap.R;
import com.siriwardana.whatsmyhandicap.database.DatabaseSingleton;
import com.siriwardana.whatsmyhandicap.database.Hole;
import com.siriwardana.whatsmyhandicap.database.Round;

import java.util.ArrayList;
import java.util.List;

public class EditHoleDialog extends Dialog implements View.OnClickListener {

    ImageView closeIV;
    TextView clubNameTV, courseNameTV, distanceTV, parTV, scoreTV;
    Button editClubNameBTN, editCourseNameBTN, editDistanceBTN, editParBTN, editScoreBTN;
    Spinner selectHoleSP;
    int roundId, holeId;
    Round round;
    Hole hole;
    Context context;
    DatabaseSingleton databaseSingleton;

    public EditHoleDialog(@NonNull Context context, Round round) {
        super(context);
        this.context = context;
        this.round = round;
        databaseSingleton = DatabaseSingleton.getDBInstance(context);
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

        editClubNameBTN = findViewById(R.id.btn_edit_club_name);
        editClubNameBTN.setOnClickListener(this);
        editCourseNameBTN = findViewById(R.id.btn_edit_course_name);
        editCourseNameBTN.setOnClickListener(this);
        editDistanceBTN = findViewById(R.id.btn_edit_distance);
        editDistanceBTN.setOnClickListener(this);
        editParBTN = findViewById(R.id.btn_edit_par);
        editParBTN.setOnClickListener(this);
        editScoreBTN = findViewById(R.id.btn_edit_score);
        editScoreBTN.setOnClickListener(this);

        closeIV = findViewById(R.id.iv_close);
        selectHoleSP = findViewById(R.id.sp_select_hole);

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
                Log.d("SSIRI", "Selected: " + i);
                updateDataByHole(i + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    @Override
    public void onClick(View v) {
        EditDataDialog editDataDialog;
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

        editDataDialog = new EditDataDialog(context, mode, hole, round);
        editDataDialog.show();

    }

    public void updateDataByHole(int holeNum) {
        hole = databaseSingleton.HoleDao().getHoleByRound(round.getRoundId(), holeNum);
        int score = hole.getHoleScore();
        distanceTV.setText(String.valueOf(hole.getDistance()));
        parTV.setText(String.valueOf(hole.getPar()));
        scoreTV.setText(score == 0 ? "E" : String.valueOf(score));

    }

    public void updateRoundData() {
        clubNameTV.setText(round.getClubName());
        courseNameTV.setText(round.getCourseName());

        updateDataByHole(1);
    }
}
