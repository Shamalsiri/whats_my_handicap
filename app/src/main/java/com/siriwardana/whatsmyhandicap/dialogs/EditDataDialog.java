package com.siriwardana.whatsmyhandicap.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.siriwardana.whatsmyhandicap.R;
import com.siriwardana.whatsmyhandicap.database.DatabaseSingleton;

public class EditDataDialog extends Dialog {

    TextView titleTV, currentValTV;
    EditText newValET;
    Button saveBTN, exitBTN;
    ImageButton closeIB;
    DatabaseSingleton databaseSingleton;
    EditModeEnums mode;
    public EditDataDialog(@NonNull Context context, EditModeEnums mode) {
        super(context);
        databaseSingleton = DatabaseSingleton.getDBInstance(context);
        this.mode = mode;
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
                break;
            case EDIT_COURSE_NAME:
                updateTitle("Course Name");
                break;
            case EDIT_DISTANCE:
                updateTitle("Distance");
                break;
            case EDIT_PAR:
                updateTitle("Par");
                break;
            case EDIT_SCORE:
                updateTitle("Stroke Count");
                break;
        }

        saveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
    }

    private void saveData() {

    }

    private void updateTitle(String title) {
        titleTV.setText(title);
    }
}
