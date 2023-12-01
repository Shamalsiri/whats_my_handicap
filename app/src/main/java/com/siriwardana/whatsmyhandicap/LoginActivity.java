package com.siriwardana.whatsmyhandicap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.siriwardana.whatsmyhandicap.fragments.LoginFragment;
import com.siriwardana.whatsmyhandicap.fragments.RegisterFragment;

public class LoginActivity extends AppCompatActivity implements LoginFragment.onLoginButtonClickListener, RegisterFragment.OnRegisterButtonClickListener {
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
    public void onLoginButtonClicked(boolean canLogin, int uID) {
        Log.d("SSiri", "Login Button Clicked");
        Log.d("SSiri", "Can Login: " + canLogin);

        if (canLogin) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("uID", uID);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onRegisterButtonClicked() {
        Log.d("SSiri", "Register Button Clicked");
        loadFragment(new RegisterFragment());
    }

    @Override
    public void onRegisterNewUser(boolean registered) {
        // close keyboard
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        Log.d("SSiri", "New User Register");
        Log.d("SSIRI", "registered: " + registered);
        if(registered) {
            Toast.makeText(LoginActivity.this, "User successfully Registered", Toast.LENGTH_SHORT)
                    .show();

            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onBackButtonPressed() {
        Log.d("SSiri", "Back Button Clicked");
        getSupportFragmentManager().popBackStack();

    }

}