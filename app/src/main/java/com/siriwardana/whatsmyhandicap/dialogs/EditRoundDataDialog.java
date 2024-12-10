package com.siriwardana.whatsmyhandicap.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.siriwardana.whatsmyhandicap.R;
import com.siriwardana.whatsmyhandicap.database.Hole;
import com.siriwardana.whatsmyhandicap.database.Round;
import com.siriwardana.whatsmyhandicap.helpers.DataStorageHelper;

public class EditRoundDataDialog extends Dialog {

    private final String TAG = EditRoundDataDialog.class.getName();
    private TextView titleTV, currentValTV;
    private EditText newValET;
    private final DataStorageHelper dataStorageHelper;
    private final EditModeEnums mode;
    Hole hole;
    Round round;
    Context context;

    /**
     * Constructor
     *
     * @param context
     * @param mode
     * @param hole
     * @param round
     */
    public EditRoundDataDialog(@NonNull Context context, EditModeEnums mode, Hole hole, Round round) {
        super(context);
        dataStorageHelper = new DataStorageHelper(context);
        this.context = context;
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

        newValET = findViewById(R.id.tv_delete_round_played_on);

        Button saveBTN = findViewById(R.id.btn_edit_save);
        Button exitBTN = findViewById(R.id.btn_delete_exit);

        ImageButton closeIB = findViewById(R.id.ib_close);

        switch (mode) {
            case EDIT_CLUB_NAME:
                updateTitle(context.getString(R.string.club_name));
                newValET.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                currentValTV.setText(round.getClubName());
                setEditTextWidth();
                break;
            case EDIT_COURSE_NAME:
                updateTitle(context.getString(R.string.course_name));
                newValET.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                currentValTV.setText(round.getCourseName());
                setEditTextWidth();
                break;
            case EDIT_DISTANCE:
                updateTitle(context.getString(R.string.distance));
                newValET.setInputType(InputType.TYPE_CLASS_NUMBER);
                currentValTV.setText(String.valueOf(hole.getDistance()));
                break;
            case EDIT_PAR:
                updateTitle(context.getString(R.string.par));
                newValET.setInputType(InputType.TYPE_CLASS_NUMBER);
                currentValTV.setText(String.valueOf(hole.getPar()));
                break;
            case EDIT_SCORE:
                updateTitle(context.getString(R.string.stroke_count));
                newValET.setInputType(InputType.TYPE_CLASS_NUMBER);
                currentValTV.setText(String.valueOf(hole.getStrokeCount()));
                break;
            default:
                Log.d(TAG, "onCreate: Unexpected value: " + mode);
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

    /**
     * Exiting dialog
     */
    public void exit() {
        Log.d(TAG, "exit: Showing Exit Alert Dialog");
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

        Button negButton = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        negButton.setTextColor(ContextCompat.getColor(getContext(), R.color.wmh_orange));;
        Button posButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        posButton.setTextColor(ContextCompat.getColor(getContext(), R.color.wmh_red));;

    }

    /**
     * Setting EditText Width
     */
    private void setEditTextWidth() {
        Log.d(TAG, "setEditTextWidth: Setting Edit Text Width");
        int width = currentValTV.getLayoutParams().width;
        ViewGroup.LayoutParams layoutParams = newValET.getLayoutParams();
        layoutParams.width = width;
        newValET.setLayoutParams(layoutParams);
    }

    /**
     * Show Toast Message
     *
     * @param msg
     */
    private void showToast(String msg) {
        Log.d(TAG, "showToast: Showing Toast Message");
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Saving Data
     */
    private void saveData() {
        Log.d(TAG, "saveData: Saving Data");
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

    /**
     * Update Dialog Title
     *
     * @param title
     */
    private void updateTitle(String title) {
        Log.d(TAG, "updateTitle: Updating Dialog Title: " + title);
        titleTV.setText(title);
    }

    /**
     * Save Club Name
     */
    private void saveClubName() {
        Log.d(TAG, "saveClubName: Saving Club Name");
        String value = newValET.getText().toString().trim();
        round.setClubName(value);

        dataStorageHelper.updateRound(round);

    }

    /**
     * Save Course Name
     */
    private void saveCourseName() {
        Log.d(TAG, "saveCourseName: Saving Course Name");
        String value = newValET.getText().toString().trim();
        round.setCourseName(value);

        dataStorageHelper.updateRound(round);
    }

    /**
     * Save Distance
     */
    private void saveDistance() {
        Log.d(TAG, "saveDistance: Saving Distance");
        String value = newValET.getText().toString().trim();
        hole.setDistance(Integer.parseInt(value));

        dataStorageHelper.updateHole(hole);
    }

    /**
     * Save Par
     */
    private void savePar() {
        Log.d(TAG, "savePar: Saving Par");
        String value = newValET.getText().toString().trim();
        hole.setPar(Integer.parseInt(value));
        hole.setHoleScore(hole.getStrokeCount() - hole.getPar());

        dataStorageHelper.updateHole(hole);
        dataStorageHelper.updateRoundScore(round.getRoundId());
    }

    /**
     * Save Stroke Count
     */
    private void saveStrokeCount() {
        Log.d(TAG, "saveStrokeCount: Saving Stroke Count");
        String value = newValET.getText().toString().trim();
        int strokeCount = Integer.parseInt(value);

        hole.setStrokeCount(strokeCount);
        hole.setHoleScore(strokeCount - hole.getPar());

        dataStorageHelper.updateHole(hole);
        dataStorageHelper.updateRoundScore(round.getRoundId());
    }
}
