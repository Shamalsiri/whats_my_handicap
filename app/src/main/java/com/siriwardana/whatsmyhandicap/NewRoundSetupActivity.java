package com.siriwardana.whatsmyhandicap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class NewRoundSetupActivity extends AppCompatActivity {

    private EditText locationEt, courseNameEt;
    private RadioGroup holesRg;
    private RadioButton selectedRb;
    private Button backBtn, startBtn;
    private String location, courseName;
    private int holes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_round_setup);

        setupEditText();
        setupRadioButtons();
        setupButtons();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateInputs()) {
                    //store values
                    storeRoundData();

                    // todo: show popup - enter course info now, or as you go

                    //start round
                    Intent intent = new Intent(NewRoundSetupActivity.this, RoundDataEntryActivity.class);
                    intent.putExtra("location", location);
                    intent.putExtra("courseName", courseName);
                    intent.putExtra("holes", holes);
                    startActivity(intent);
                }
            }
        });

    }

    private void storeRoundData() {
        location = locationEt.getText().toString().trim();
        Log.d("Siri", "Location entered: " + location);

        courseName = courseNameEt.getText().toString().trim();
        Log.d("Siri", "Course name entered: " + courseName);

        selectedRb = (RadioButton) findViewById(holesRg.getCheckedRadioButtonId());
        holes = Integer.valueOf(selectedRb.getText().toString());
        Log.d("Siri", "Number of holes selected: " + holes);

        //todo: Store the data in the db;
    }

    private boolean validateInputs() {
        Toast toast = new Toast(NewRoundSetupActivity.this);

        //validate locations
        if(locationEt.getText().length() < 3) {
            Log.d("Siri", "Club location validation failed");

            toast.setText("Enter Club Name");
            toast.show();
            locationEt.setHintTextColor(Color.RED);
            locationEt.setHint("Required");
            return false;
        }

        //validate course
        if (courseNameEt.getText().length() < 1) {
            Log.d("Siri", "Course name empty validation failed");

            toast.setText("Enter Course Name");
            toast.show();
            courseNameEt.setHintTextColor(Color.RED);
            courseNameEt.setHint("Required");
            return false;
        }

        //validate radio buttons
        if (holesRg.getCheckedRadioButtonId() == -1) {
            Log.d("Siri", "Number of holes for round validation failed");

            // no radio buttons are checked
            toast.setText("Select Number of Holes");
            toast.show();
            return false;
        }

        Log.d("Siri", "New Round setup successfully started");

        return true;
    }

    private void setupButtons() {
        backBtn = (Button) findViewById(R.id.btn_back);
        startBtn = (Button) findViewById(R.id.btn_start);
    }

    private void setupRadioButtons() {

        // radio buttons

        // radio groups
        holesRg = (RadioGroup) findViewById(R.id.rg_holes);
    }

    private void setupEditText() {
        locationEt = (EditText) findViewById(R.id.et_location);
        courseNameEt = (EditText) findViewById(R.id.et_course_name);
    }
}