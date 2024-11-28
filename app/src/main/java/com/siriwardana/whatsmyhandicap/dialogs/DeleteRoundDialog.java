package com.siriwardana.whatsmyhandicap.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.siriwardana.whatsmyhandicap.R;
import com.siriwardana.whatsmyhandicap.database.Round;
import com.siriwardana.whatsmyhandicap.helpers.DataStorageHelper;

public class DeleteRoundDialog extends Dialog {
    private final Context context;
    private final Round round;
    private final DataStorageHelper dataStorageHelper;

    public DeleteRoundDialog(@NonNull Context context, Round round) {
        super(context);
        this.context = context;
        this.round = round;
        dataStorageHelper = new DataStorageHelper(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_delete_round);

        TextView courseName = findViewById(R.id.tv_delete_course_name);
        TextView playedOn = findViewById(R.id.tv_delete_round_played_on);

        String name = round.getClubName() + ": " + round.getCourseName();

        ImageView closeIV = findViewById(R.id.ib_close);
        Button btnExit = findViewById(R.id.btn_delete_exit);
        Button btnDelete = findViewById(R.id.btn_delete_dialog_delete);

        courseName.setText(name);
        playedOn.setText("00-00-0000 @ 00:00");

        closeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataStorageHelper.deleteRound(round);
                dismiss();
            }
        });
    }


}
