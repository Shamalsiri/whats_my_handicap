package com.siriwardana.whatsmyhandicap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class NewRoundSetupActivity extends AppCompatActivity {

    private EditText locationEt, courseNameEt;
    private RadioGroup holesRg;
    private RadioButton nineRb, eighteenRb, twentySevenRb, thirtySixRb;
    private Button backBtn, startBtn;

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
                    //store vals
                    //show popup - enter course info now, or as you go
                    //start round
                }
            }
        });

    }

    private boolean validateInputs() {
        //validate edit texts
        //validate radiobuttons
        return false;
    }

    private void setupButtons() {
        backBtn = (Button) findViewById(R.id.btn_back);
        startBtn = (Button) findViewById(R.id.btn_start);
    }

    private void setupRadioButtons() {

        //radio buttons
        nineRb = (RadioButton) findViewById(R.id.rb_nine);
        eighteenRb = (RadioButton) findViewById(R.id.rb_eighteen);
        twentySevenRb = (RadioButton) findViewById(R.id.rb_twenty_seven);
        thirtySixRb = (RadioButton) findViewById(R.id.rb_thirty_six);

        //radio group
        holesRg = (RadioGroup) findViewById(R.id.rg_holes);
    }

    private void setupEditText() {
        locationEt = (EditText) findViewById(R.id.et_location);
        courseNameEt = (EditText) findViewById(R.id.et_course_name);
    }
}