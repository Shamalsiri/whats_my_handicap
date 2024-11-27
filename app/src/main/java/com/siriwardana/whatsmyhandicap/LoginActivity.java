package com.siriwardana.whatsmyhandicap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.siriwardana.whatsmyhandicap.fragments.LoginFragment;
import com.siriwardana.whatsmyhandicap.fragments.RegisterFragment;

public class LoginActivity extends AppCompatActivity implements
        LoginFragment.onLoginButtonClickListener, RegisterFragment.OnRegisterButtonClickListener {

    private final String TAG = LoginActivity.class.getName();
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.d(TAG, "onCreate");
        loadFragment(new LoginFragment());

    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.login_fragment_container);

        if (!(currentFragment instanceof LoginFragment)) {
            super.onBackPressed();
        } else {
            // todo: show exit dialog
        }
    }

    /**
     * Load fragment
     *
     * @param fragment
     */
    private void loadFragment(Fragment fragment) {
        Log.d(TAG, "loadFragment: Loading Fragment: " + fragment.toString());
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.login_fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /**
     * Login/ Load MainActivity if able to Login
     *
     * @param canLogin
     * @param userId
     */
    @Override
    public void onLoginFragmentLoginButtonClicked(boolean canLogin, int userId) {
        Log.d(TAG, "onLoginFragmentLoginButtonClicked: Login Button Clicked");

        if (canLogin) {
            Log.d(TAG, "onLoginFragmentLoginButtonClicked: Logging in");
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
            finish();
        }
    }

    /**
     * Open Register Fragment
     */
    @Override
    public void onLoginFragmentRegisterButtonClicked() {
        Log.d(TAG, "onLoginFragmentRegisterButtonClicked: Register Button Clicked");
        loadFragment(new RegisterFragment());
    }

    /**
     * Register New User when register button is clicked
     *
     * @param registered
     */
    @Override
    public void onRegisterFragmentRegisterButtonClicked(boolean registered) {
        Log.d(TAG, "onRegisterFragmentRegisterButtonClicked: Register Button Clicked");
        // close keyboard
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm =
                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        if (registered) {
            Toast.makeText(LoginActivity.this, "User successfully Registered",
                    Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onRegisterFragmentRegisterButtonClicked: " +
                    "User successfully Registered");

            getSupportFragmentManager().popBackStack();
        }
    }

    /**
     * pop Back Stack to the login activity & fragment
     */
    @Override
    public void onRegisterFragmentBackButtonClicked() {
        Log.d(TAG, "onRegisterFragmentBackButtonClicked: Back Button Clicked");
        getSupportFragmentManager().popBackStack();

    }

}