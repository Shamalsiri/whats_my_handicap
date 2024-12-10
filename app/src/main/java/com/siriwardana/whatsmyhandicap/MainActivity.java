package com.siriwardana.whatsmyhandicap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.siriwardana.whatsmyhandicap.fragments.MainFragment;
import com.siriwardana.whatsmyhandicap.fragments.NewRoundFragment;
import com.siriwardana.whatsmyhandicap.fragments.PreviousScoresFragment;
import com.siriwardana.whatsmyhandicap.fragments.RoundDataEntryFragment;

public class MainActivity extends AppCompatActivity
        implements MainFragment.onMainFragmentButtonClickListener,
        NewRoundFragment.onNewRoundButtonClickListener,
        RoundDataEntryFragment.onRoundDataEntryButtonClickListener {

    private final String TAG = MainActivity.class.getName();
    private FragmentTransaction fragmentTransaction;
    private int userId, roundId;
    private boolean canLogout = false;
    private MainFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hideSystemBars();

        Intent intent = getIntent();
        if (intent != null) {
            userId = intent.getIntExtra("userId", 0);
            Log.d(TAG, "onCreate: User Logged in with id: " + userId);
        }

        Log.d(TAG, "onCreate: Loading Main Fragment");
        Bundle bundle = new Bundle();
        bundle.putInt("userId", userId);
        MainFragment mainFragment = new MainFragment();
        mainFragment.setArguments(bundle);
        loadFragment(mainFragment);
    }

    private void hideSystemBars() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
    }


    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.main_fragment_container);

        if (!(currentFragment instanceof MainFragment)) {
            super.onBackPressed();
        } else {
            // todo: show logout dialog
        }
    }

    /**
     * Load Fragment
     *
     * @param fragment
     */
    public void loadFragment(Fragment fragment) {
        Log.d(TAG, "loadFragment: Loading Fragment: " + fragment.toString());
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    /**
     * opens settings fragment when button is clicked
     */
    @Override
    public void onMainFragmentSettingsButtonClicked() {
        Log.d(TAG, "onMainFragmentSettingsButtonClicked: Settings button clicked");
    }

    /**
     * Logout the user when the button is clicked
     */
    @Override
    public void onMainFragmentLogoutButtonClicked() {
        canLogout = false;
        LogoutDialog();
    }

    private void LogoutDialog() {

        Log.d(TAG, "exit: Showing Exit Alert Dialog");
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create(); //Read Update
        alertDialog.setTitle("Confirm Logout");
        alertDialog.setMessage("Are you sure you want to Logout?");

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Logout", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // here you can add functions
                alertDialog.dismiss();
                Log.d(TAG, "onMainFragmentLogoutButtonClicked: Logout button clicked");
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
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
        negButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.wmh_green));;
        Button posButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        posButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.wmh_red));;
    }

    /**
     * Loads the New Round Fragment when the button is clicked
     */
    @Override
    public void onMainFragmentNewRoundButtonClicked() {
        Log.d(TAG, "onMainFragmentNewRoundButtonClicked: New Round Button Clicked");
        Bundle bundle = new Bundle();
        bundle.putInt("userId", userId);
        NewRoundFragment newRoundFragment = new NewRoundFragment();
        newRoundFragment.setArguments(bundle);
        loadFragment(newRoundFragment);
    }

    /**
     * Loads the Previous Scores Fragment when the button is clicked
     *
     * @param userId
     */
    @Override
    public void onMainFragmentPreviousScoreButtonClicked(int userId) {
        Log.d(TAG, "onMainFragmentPreviousScoreButtonClicked: Previous Score Button Clicked");
        Bundle bundle = new Bundle();
        bundle.putInt("userId", userId);
        PreviousScoresFragment previousScoresFragment = new PreviousScoresFragment();
        previousScoresFragment.setArguments(bundle);
        loadFragment(previousScoresFragment);

    }

    /**
     * Pop Fragment back stack to the main activity/fragment
     */
    @Override
    public void onNewRoundFragmentBackButtonClicked() {
        Log.d(TAG, "onNewRoundFragmentBackButtonClicked: Back Button Clicked");
        getSupportFragmentManager().popBackStack();
    }

    /**
     * Opens a new round data entry fragment with the new round data
     *
     * @param canStart
     * @param roundId
     * @param numHoles
     */
    @Override
    public void onNewRoundFragmentStartButtonClicked(boolean canStart, int roundId, int numHoles) {
        Log.d(TAG, "onNewRoundFragmentStartButtonClicked: Start Button Clicked");

        Log.d(TAG, "onNewRoundFragmentStartButtonClicked: Can start new round: " + canStart);
        if (canStart) {
            this.roundId = roundId;

            Bundle bundle = new Bundle();
            bundle.putInt("userId", userId);
            bundle.putInt("roundId", roundId);
            bundle.putInt("numHoles", numHoles);
            bundle.putString("mode", "new_round");
            RoundDataEntryFragment roundDataEntryFragment = new RoundDataEntryFragment();
            roundDataEntryFragment.setArguments(bundle);
            loadFragment(roundDataEntryFragment);

        }
    }

    /**
     * Pop Fragment Back stack if isExit is true
     *
     * @param isExit
     */
    @Override
    public void onNewRoundDataEntryPreviousButtonClicked(boolean isExit) {
        Log.d(TAG, "onNewRoundDataEntryPreviousButtonClicked: Previous Button Clicked");

        Log.d(TAG, "onNewRoundDataEntryPreviousButtonClicked: isExit: " + isExit);
        if (isExit) {
            getSupportFragmentManager().popBackStack();
        }
    }

    /**
     * Logs when the next button is clicked
     * Most of the work here is handled by the NewRoundDataEntry Fragment
     */
    @Override
    public void onNextButtonClicked() {
        Log.d(TAG, "onNextButtonClicked: Next Button Clicked");
    }
}