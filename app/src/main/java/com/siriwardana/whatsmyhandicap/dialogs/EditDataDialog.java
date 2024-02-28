package com.siriwardana.whatsmyhandicap.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.siriwardana.whatsmyhandicap.R;
import com.siriwardana.whatsmyhandicap.database.DatabaseSingleton;
import com.siriwardana.whatsmyhandicap.database.Hole;
import com.siriwardana.whatsmyhandicap.database.Round;
import com.siriwardana.whatsmyhandicap.helpers.DataStorageHelper;

public class EditDataDialog extends Dialog {

    TextView titleTV, currentValTV;
    EditText newValET;
    Button saveBTN, exitBTN;
    ImageButton closeIB;
    DatabaseSingleton databaseSingleton;
    DataStorageHelper dataStorageHelper;
    EditModeEnums mode;
    Hole hole;
    Round round;

    public EditDataDialog(@NonNull Context context, EditModeEnums mode, Hole hole, Round round) {
        super(context);
        databaseSingleton = DatabaseSingleton.getDBInstance(context);
        dataStorageHelper = new DataStorageHelper(context);
        this.mode = mode;
        this.hole = hole;
        this.round = round;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit_data);

        titleTV = findViewById(R.id.tv_edit_title);
        currentValTV = findViewById(R.id.tv_current_value);

        newValET = findViewById(R.id.et_new_value);

        saveBTN = findViewById(R.id.btn_edit_save);
        exitBTN = findViewById(R.id.btn_edit_exit);

        closeIB = findViewById(R.id.ib_close);

        switch (mode) {
            case EDIT_CLUB_NAME:
                updateTitle("Club Name");
                newValET.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                currentValTV.setText(round.getClubName());
                setEditTextWidth();
                break;
            case EDIT_COURSE_NAME:
                updateTitle("Course Name");
                newValET.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                currentValTV.setText(round.getCourseName());
                setEditTextWidth();
                break;
            case EDIT_DISTANCE:
                updateTitle("Distance");
                newValET.setInputType(InputType.TYPE_CLASS_NUMBER);
                currentValTV.setText(String.valueOf(hole.getDistance()));
                break;
            case EDIT_PAR:
                updateTitle("Par");
                newValET.setInputType(InputType.TYPE_CLASS_NUMBER);
                currentValTV.setText(String.valueOf(hole.getPar()));
                break;
            case EDIT_SCORE:
                updateTitle("Stroke Count");
                newValET.setInputType(InputType.TYPE_CLASS_NUMBER);
                currentValTV.setText(String.valueOf(hole.getStrokeCount()));
                break;
        }

        saveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newValue = newValET.getText().toString().trim();

                if (!TextUtils.isEmpty(newValue)) {
                    saveData();
                    dismiss();
                } else {
                    showToast("Enter a value to save or click exit to close");
                }
            }
        });

        exitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exit();
            }
        });

        closeIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exit();
            }
        });
    }

    public void exit() {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create(); //Read Update
        alertDialog.setTitle("Confirm Exit");
        alertDialog.setMessage("You are exiting without saving any changes");


        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Exit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // here you can add functions
                alertDialog.dismiss();
                dismiss();
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // here you can add functions
                alertDialog.dismiss();
            }
        });


        alertDialog.show();
    }

    private void setEditTextWidth() {
        int width = currentValTV.getLayoutParams().width;
        ViewGroup.LayoutParams layoutParams = newValET.getLayoutParams();
        layoutParams.width = width;
        newValET.setLayoutParams(layoutParams);
    }

    private void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void saveData() {
        switch (mode) {
            case EDIT_CLUB_NAME:
                saveClubName();
                break;
            case EDIT_COURSE_NAME:
                saveCourseName();
                break;
            case EDIT_DISTANCE:
                saveDistance();
                break;
            case EDIT_PAR:
                savePar();
                break;
            case EDIT_SCORE:
                saveStrokeCount();
                break;
        }
    }

    private void updateTitle(String title) {
        titleTV.setText(title);
    }

    private void saveClubName() {
        String value = newValET.getText().toString().trim();
        round.setClubName(value);

        dataStorageHelper.updateRound(round);

    }

    private void saveCourseName() {
        String value = newValET.getText().toString().trim();
        round.setCourseName(value);

        dataStorageHelper.updateRound(round);
    }

    private void saveDistance() {
        String value = newValET.getText().toString().trim();
        hole.setDistance(Integer.parseInt(value));

        dataStorageHelper.updateHole(hole);
    }

    private void savePar() {
        String value = newValET.getText().toString().trim();
        hole.setPar(Integer.parseInt(value));
        hole.setHoleScore(hole.getStrokeCount() - hole.getPar());

        dataStorageHelper.updateHole(hole);
        dataStorageHelper.updateRoundScore(round.getRoundId());
    }

    private void saveStrokeCount() {
        String value = newValET.getText().toString().trim();
        int strokeCount = Integer.parseInt(value);

        hole.setStrokeCount(strokeCount);
        hole.setHoleScore(strokeCount - hole.getPar());

        dataStorageHelper.updateHole(hole);
        dataStorageHelper.updateRoundScore(round.getRoundId());
    }
}
