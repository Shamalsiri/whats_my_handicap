package com.siriwardana.whatsmyhandicap.fragments;

import android.os.Bundle;
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

    private PasswordVisibilityToggleHelper passwordVisibilityToggleHelper;
    private UserRegistrationHelper userRegistrationHelper;
    private DatabaseSingleton dbSingleton;
    private EditText emailET, passwordET;
    private TextView errorTV;
    private int uID;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        emailET = (EditText) view.findViewById(R.id.et_email_address);
        passwordET = (EditText) view.findViewById(R.id.et_password);

        errorTV = (TextView) view.findViewById(R.id.tv_error);

        //Needed to validate the email
        userRegistrationHelper = new UserRegistrationHelper();
        //Needed to validate login
        dbSingleton = DatabaseSingleton.getDBInstance(getContext().getApplicationContext());

        view.findViewById(R.id.btn_login).setOnClickListener(v -> {
            String email = emailET.getText().toString().trim();
            String password = passwordET.getText().toString().trim();

            errorTV.setVisibility(View.INVISIBLE);

            boolean canLogin;
            if (!userRegistrationHelper.validateEmail(email)) {
                showErrorMsg("Invalid email format");
                canLogin = false;
            } else if (!validateLoginCredentials(email, password)) {
                showErrorMsg("Username and Password didn't match our records");
                canLogin = false;
            } else {
                errorTV.setText("");
                errorTV.setVisibility(View.INVISIBLE);
                canLogin = true;
            }

            ((onLoginButtonClickListener) requireActivity()).onLoginButtonClicked(canLogin, uID);
        });

        view.findViewById(R.id.btn_register).setOnClickListener(v -> {
            ((onLoginButtonClickListener) requireActivity()).onRegisterButtonClicked();
        });

        // Password visibility toggle logic
        passwordVisibilityToggleHelper = new PasswordVisibilityToggleHelper();
        passwordET.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_password_visibility_off,0);
        passwordET.setOnTouchListener(passwordVisibilityToggleHelper.getPasswordTouchListener(passwordET));


        return view;
    }

    public void showErrorMsg(String msg) {
        errorTV.setText(msg);
        errorTV.setVisibility(View.VISIBLE);
    }

    private boolean validateLoginCredentials(String email, String password) {
        User user;
        user = dbSingleton.UserDao().getUserByEmail(email);

        if(user != null && user.getPassword().equals(password)) {
            uID = user.getUID();
            return true;
        }
        return false;
    }

    public interface onLoginButtonClickListener {
        void onLoginButtonClicked(boolean loginAllowed, int uID);
        void onRegisterButtonClicked();
    }
}
