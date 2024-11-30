package com.siriwardana.whatsmyhandicap.fragments;

import static java.lang.Integer.parseInt;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import java.time.format.DateTimeFormatter;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.siriwardana.whatsmyhandicap.R;
import com.siriwardana.whatsmyhandicap.database.DatabaseSingleton;
import com.siriwardana.whatsmyhandicap.database.Round;

import java.time.LocalDateTime;

public class NewRoundFragment extends Fragment {

    private final String TAG = NewRoundFragment.class.getName();
    private Button backBTN, startRoundBTN;
    private EditText clubNameET, courseNameET;
    private TextView clubNameLabelTV, numHolesLabelTV, selectTeeWarningTV;
    private RadioGroup holesRG;
    private RadioButton checkedRadioButton;
    private String clubName, courseName;
    private Spinner slopeRatingSP;
    private int numHoles, userId, roundId, teeSelected, slopeRating;
    private DatabaseSingleton dbSingleton;

    /**
     * Constructor
     */
    public NewRoundFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String[] courses = {"one", "two", "three"};
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_round, container, false);
        dbSingleton = DatabaseSingleton.getDBInstance(getContext());
        Bundle bundle = getArguments();
        if (bundle != null) {
            userId = bundle.getInt("userId");
        }

        backBTN = view.findViewById(R.id.btn_nr_back);
        startRoundBTN = view.findViewById(R.id.btn_nr_start_round);

        clubNameET = view.findViewById(R.id.et_club_name);
        courseNameET = view.findViewById(R.id.et_course_name);

        clubNameLabelTV = view.findViewById(R.id.tv_club_name_label);
        numHolesLabelTV = view.findViewById(R.id.tv_num_holes_label);
        selectTeeWarningTV = view.findViewById(R.id.tv_select_tee_req_label);

        holesRG = view.findViewById(R.id.rg_num_holes);

        slopeRatingSP = view.findViewById(R.id.sp_slope_rating);

        backBTN.setOnClickListener(v ->
                ((onNewRoundButtonClickListener) requireActivity()).onNewRoundFragmentBackButtonClicked()
        );

        startRoundBTN.setOnClickListener(v -> {

            clubNameLabelTV.setVisibility(View.GONE);
            numHolesLabelTV.setVisibility(View.GONE);

            clubName = clubNameET.getText().toString().trim();
            courseName = courseNameET.getText().toString().trim();

            boolean canStart;
            canStart = validateNewRoundForm(view);

            Log.d(TAG, "onCreateView: Can Start a New Round: " + canStart);
            if (canStart) {
                LocalDateTime current = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy | HH:mm a");
                String formattedDateTime = current.format(formatter);
                String[] date = formattedDateTime.split("\\|");
                String time = date[1].trim();
                int hour = parseInt(date[1].trim().substring(0,2));
                if ( hour > 12) {
                    hour = hour - 12;
                    time = hour + time.substring(2);
                    date[1] = time;
                }

                String timeDate = String.join(" | ", date[0], date[1]);



                Round newRound = new Round();
                newRound.setUserId(userId);
                newRound.setClubName(clubName);
                newRound.setCourseName(courseName);
                newRound.setNumHoles(numHoles);
                newRound.setDate(timeDate);
                newRound.setSlopeRating(slopeRating);
                Log.d(TAG, "onCreateView: Inserting Round data to the Round table");
                dbSingleton.RoundDao().insert(newRound);
                roundId = dbSingleton.RoundDao().getLatestRoundId();
                Log.d(TAG, "onCreateView: Round Created; RoundId: " + roundId);
            }

            ((onNewRoundButtonClickListener) requireActivity()).onNewRoundFragmentStartButtonClicked(canStart,
                    roundId, numHoles);
        });

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.slope_rating,    // The array resource
                android.R.layout.simple_spinner_item // Default layout for spinner items
        );
        // Specify the layout to use when the list of choices appears
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        slopeRatingSP.setAdapter(dataAdapter);

        slopeRatingSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                teeSelected = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }

    /**
     * Validate that the club name is more than 2 characters
     * Determine if the round is 9 holes or 18 holes
     *
     * @param v
     * @return
     */
    private boolean validateNewRoundForm(View v) {

        boolean canStart = true;
        if (clubName.length() < 2) {
            Log.d(TAG, "validateNewRoundForm: Club Name is invalid");
            clubNameLabelTV.setVisibility(View.VISIBLE);
            canStart = false;
        }

        int checkedRB = holesRG.getCheckedRadioButtonId();
        if (checkedRB != -1) {
            checkedRadioButton = v.findViewById(checkedRB);
            if (checkedRadioButton.getText().toString().equalsIgnoreCase("9 Holes")) {
                numHoles = 9;
                Log.d(TAG, "validateNewRoundForm: 9 holes selected");
            } else {
                numHoles = 18;
                Log.d(TAG, "validateNewRoundForm: 18 holes selected");
            }
        } else {
            Log.d(TAG, "validateNewRoundForm: Number of holes not selected");
            numHolesLabelTV.setVisibility(View.VISIBLE);
            canStart = false;
        }

        if (teeSelected == 0 ) {
            selectTeeWarningTV.setVisibility(View.VISIBLE);
            canStart = false;
        } else {
            canStart = true;
            switch (teeSelected) {
                case 1:
                    slopeRating = 95;
                    break;
                case 2:
                    slopeRating = 105;
                    break;
                case 3:
                    slopeRating = 115;
                    break;
                case 4:
                    slopeRating = 125;
                    break;
                case 5:
                    slopeRating = 135;
                    break;
                default:
                    slopeRating = -1;
            }
        }
        return canStart;
    }

    public interface onNewRoundButtonClickListener {
        void onNewRoundFragmentBackButtonClicked();

        void onNewRoundFragmentStartButtonClicked(boolean canStart, int roundId, int numHoles);
    }
}