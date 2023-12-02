package com.siriwardana.whatsmyhandicap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.siriwardana.whatsmyhandicap.fragments.MainFragment;
import com.siriwardana.whatsmyhandicap.fragments.NewRoundFragment;
import com.siriwardana.whatsmyhandicap.fragments.RoundDataEntryFragment;

public class MainActivity extends AppCompatActivity
        implements MainFragment.onMainFragmentButtonClickListener,
        NewRoundFragment.onNewRoundButtonClickListener {

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private int userId, roundId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        if(intent != null) {
            userId = intent.getIntExtra("userId", 0);
        }


        loadFragment(new MainFragment());
    }

    public void loadFragment(Fragment fragment){
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    @Override
    public void onSettingsButtonClicked() {

    }

    @Override
    public void onLogoutButtonClicked() {

    }

    @Override
    public void onNewRoundButtonClicked() {
        Bundle bundle = new Bundle();
        bundle.putInt("userId", userId);
        NewRoundFragment nRFragment = new NewRoundFragment();
        nRFragment.setArguments(bundle);
        loadFragment(nRFragment);
    }

    @Override
    public void onPrevScoreButtonClicked(int userId) {

    }

    @Override
    public void onBackButtonClicked() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onStartButtonClicked(boolean canStart, int roundId) {
        Log.d("SSIRI", "Can Start: " + canStart);
        if(canStart) {
            this.roundId = roundId;

            Bundle bundle = new Bundle();
            bundle.putInt("userId", userId);
            bundle.putInt("roundId", roundId);
            RoundDataEntryFragment rdeFragment = new RoundDataEntryFragment();
            rdeFragment.setArguments(bundle);
            loadFragment(rdeFragment);

        }
    }
}