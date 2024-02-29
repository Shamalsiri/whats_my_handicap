package com.siriwardana.whatsmyhandicap.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.siriwardana.whatsmyhandicap.R;
import com.siriwardana.whatsmyhandicap.database.DatabaseSingleton;
import com.siriwardana.whatsmyhandicap.database.User;
import com.siriwardana.whatsmyhandicap.helpers.PasswordVisibilityToggleHelper;
import com.siriwardana.whatsmyhandicap.helpers.UserRegistrationHelper;

public class LoginFragment extends Fragment {

    private final String TAG = LoginFragment.class.getName();
    private PasswordVisibilityToggleHelper passwordVisibilityToggleHelper;
    private UserRegistrationHelper userRegistrationHelper;
    private DatabaseSingleton dbSingleton;
    private EditText emailET, passwordET;
    private TextView errorTV;
    private int userId;

    /**
     * Constructor
     */
    public LoginFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        emailET = view.findViewById(R.id.et_email_address);
        passwordET = view.findViewById(R.id.et_password);

        errorTV = view.findViewById(R.id.tv_error);

        //Needed to validate the email
        userRegistrationHelper = new UserRegistrationHelper();
        //Needed to validate login
        dbSingleton = DatabaseSingleton.getDBInstance(getContext());

        view.findViewById(R.id.btn_login).setOnClickListener(v -> {
            String email = emailET.getText().toString().trim();
            String password = passwordET.getText().toString().trim();

            errorTV.setVisibility(View.INVISIBLE);

            boolean canLogin;
            if (!userRegistrationHelper.validateEmail(email)) {
                Log.d(TAG, "onCreateView: Invalid Email");
                showErrorMsg(getString(R.string.invalid_email));
                canLogin = false;
            } else if (!validateLoginCredentials(email, password)) {
                Log.d(TAG, "onCreateView: Invalid Username and Password");
                showErrorMsg(getString(R.string.username_and_password_didn_t_match_our_records));
                canLogin = false;
            } else {
                errorTV.setText("");
                errorTV.setVisibility(View.INVISIBLE);
                canLogin = true;
            }

            ((onLoginButtonClickListener) requireActivity())
                    .onLoginFragmentLoginButtonClicked(canLogin, userId);
        });

        view.findViewById(R.id.btn_register).setOnClickListener(v -> {

            ((onLoginButtonClickListener) requireActivity()).onLoginFragmentRegisterButtonClicked();
        });

        // Password visibility toggle logic
        passwordVisibilityToggleHelper = new PasswordVisibilityToggleHelper();

        Log.d(TAG, "onCreateView: Hiding Password");
        passwordET.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                R.drawable.ic_password_visibility_off, 0);
        passwordET.setOnTouchListener(passwordVisibilityToggleHelper
                .getPasswordTouchListener(passwordET));


        return view;
    }

    /**
     * Update the error TextView
     * Set TextView visible
     *
     * @param msg
     */
    public void showErrorMsg(String msg) {
        Log.d(TAG, "showErrorMsg: Error Message: " + msg);
        errorTV.setText(msg);
        Log.d(TAG, "showErrorMsg: Showing Error Message");
        errorTV.setVisibility(View.VISIBLE);
    }

    /**
     * Validate if the username and password match the database records
     *
     * @param email
     * @param password
     * @return
     */
    private boolean validateLoginCredentials(String email, String password) {
        User user;
        Log.d(TAG, "validateLoginCredentials: Getting User by Email");
        user = dbSingleton.UserDao().getUserByEmail(email);

        if (user != null && user.getPassword().equals(password)) {
            userId = user.getUserId();
            Log.d(TAG, "validateLoginCredentials: Email and Password Matches DB Records");
            return true;
        }
        return false;
    }

    public interface onLoginButtonClickListener {
        void onLoginFragmentLoginButtonClicked(boolean loginAllowed, int uID);

        void onLoginFragmentRegisterButtonClicked();
    }
}
