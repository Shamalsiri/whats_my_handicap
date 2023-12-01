package com.siriwardana.whatsmyhandicap.fragments;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.siriwardana.whatsmyhandicap.R;
import com.siriwardana.whatsmyhandicap.helpers.PasswordVisibilityToggleHelper;
import com.siriwardana.whatsmyhandicap.helpers.UserRegistrationHelper;

public class LoginFragment extends Fragment {

    private PasswordVisibilityToggleHelper passwordVisibilityToggleHelper;

    private UserRegistrationHelper userRegistrationHelper;
    private EditText emailET, passwordET;
    private TextView errorTV;

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

        view.findViewById(R.id.btn_login).setOnClickListener(v -> {

            boolean canLogin = validateLogin(emailET, passwordET);
            if (!userRegistrationHelper.validateEmail(emailET.getText().toString().trim())) {
                errorTV.setText("Invalid email");
                errorTV.setVisibility(View.VISIBLE);
                canLogin = false;
            } else if (!validateLogin(emailET, passwordET)) {
                errorTV.setText("Username and Password didn't match our records");
                errorTV.setVisibility(View.VISIBLE);
                canLogin = false;
            } else {
                errorTV.setText("");
                errorTV.setVisibility(View.INVISIBLE);
            }

            ((onLoginButtonClickListener) requireActivity()).onLoginButtonClicked(canLogin);
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

    private boolean validateEmail(CharSequence email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean validateLogin(EditText emailEt, EditText passwordET) {
        String email = emailEt.getText().toString();
        String pass  = passwordET.getText().toString();

        //TODO: Validate against credentials DB Table;
        if(email.length() > 5 && pass.length() > 5) {
            Log.d("SSIRI", "Login Email: " + email);
            Log.d("SSIRI", "Login Password: " + pass);
            return true;
        }
        return false;
    }

    public interface onLoginButtonClickListener {
        void onLoginButtonClicked(boolean loginAllowed);
        void onRegisterButtonClicked();
    }
}
