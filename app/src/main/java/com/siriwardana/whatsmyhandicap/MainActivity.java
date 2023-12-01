package com.siriwardana.whatsmyhandicap;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.siriwardana.whatsmyhandicap.fragments.LoginFragment;
import com.siriwardana.whatsmyhandicap.fragments.RegisterFragment;

public class MainActivity extends AppCompatActivity implements LoginFragment.onLoginButtonClickListener, RegisterFragment.OnRegisterButtonClickListener {
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadFragment(new LoginFragment());

    }

    private void loadFragment(Fragment fragment) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onLoginButtonClicked(boolean canLogin) {
        Log.d("SSiri", "Login Button Clicked");
        Log.d("SSiri", "Can Login: " + canLogin);

        if (canLogin) {
            // TODO: Open next Activity
        }
    }

    @Override
    public void onRegisterButtonClicked() {
        Log.d("SSiri", "Register Button Clicked");
        loadFragment(new RegisterFragment());
    }

    @Override
    public void onRegisterNewUser(boolean canRegister) {
        Log.d("SSiri", "New User Register");
    }

    @Override
    public void onBackButtonPressed() {
        Log.d("SSiri", "Back Button Clicked");
        getSupportFragmentManager().popBackStack();

    }

}