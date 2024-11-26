package com.siriwardana.whatsmyhandicap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends AppCompatActivity {

    Button newRoundBtn, prevScoreBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        newRoundBtn = (Button) findViewById(R.id.btn_new_round);
        prevScoreBtn = (Button) findViewById(R.id.btn_previous_scores);


        newRoundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, NewRoundSetupActivity.class);
                MainMenuActivity.this.startActivity(intent);
            }
        });

        prevScoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, PrevScoresActivity.class);
                MainMenuActivity.this.startActivity(intent);
            }
        });
    }
}