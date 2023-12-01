package com.siriwardana.whatsmyhandicap.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.siriwardana.whatsmyhandicap.R;
import com.siriwardana.whatsmyhandicap.helpers.PasswordVisibilityToggleHelper;
import com.siriwardana.whatsmyhandicap.helpers.UserRegistrationHelper;

import java.time.LocalDate;

public class RegisterFragment extends Fragment {

    private PasswordVisibilityToggleHelper passwordVisibilityToggleHelper;
    private UserRegistrationHelper userRegistrationHelper;
    private TextView errorMsgTV;

    private EditText fNameET, lNameET, dobET,
            emailET, emailConfirmET, passwordET, passwordConfirmET;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register, container, false);

        userRegistrationHelper = new UserRegistrationHelper();

        errorMsgTV = (TextView) view.findViewById(R.id.tv_error_msg);
        errorMsgTV.setVisibility(View.INVISIBLE);

        fNameET = (EditText) view.findViewById(R.id.et_first_name);
        lNameET = (EditText) view.findViewById(R.id.et_last_name);
        dobET = (EditText) view.findViewById(R.id.et_dob);
        emailET = (EditText) view.findViewById(R.id.et_email);
        emailConfirmET = (EditText) view.findViewById(R.id.et_email_confirm);
        passwordET = (EditText) view.findViewById(R.id.et_passwd);
        passwordConfirmET = (EditText) view.findViewById(R.id.et_passwd_confirm);


        // Password visibility toggle logic
        passwordVisibilityToggleHelper = new PasswordVisibilityToggleHelper();
        passwordET.setCompoundDrawablesWithIntrinsicBounds(0,0,
                R.drawable.ic_password_visibility_off,0);
        passwordET.setOnTouchListener(
                passwordVisibilityToggleHelper.getPasswordTouchListener(passwordET));

        // Password visibility toggle logic
        passwordConfirmET.setCompoundDrawablesWithIntrinsicBounds(0,0
                ,R.drawable.ic_password_visibility_off,0);
        passwordConfirmET.setOnTouchListener(
                passwordVisibilityToggleHelper.getPasswordTouchListener(passwordConfirmET));

        view.findViewById(R.id.btn_back).setOnClickListener(v -> {
            ((OnRegisterButtonClickListener) requireActivity()).onBackButtonPressed();

        });

        view.findViewById(R.id.btn_register).setOnClickListener(v -> {
            boolean canRegister = validateRegisterFormInput() &&
                    validateNewUser(emailET.getText().toString().trim());

            if (canRegister) {
                // place holder
                errorMsgTV.setText("Can Register");
                errorMsgTV.setVisibility(View.VISIBLE);
            }
            ((OnRegisterButtonClickListener) requireActivity()).onRegisterNewUser(canRegister);
        });

        return view;
    }

    public boolean validateNewUser(String email) {
        //Todo: validate that the email isn't on the db already.
        return true;
    }

    public boolean validateRegisterFormInput() {
        String fName = fNameET.getText().toString().trim();
        String lName = lNameET.getText().toString().trim();
        String dob = dobET.getText().toString().trim();
        String email = emailET.getText().toString().trim();
        String confirmEmail = emailConfirmET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();
        String confirmPassword = passwordConfirmET.getText().toString().trim();

        errorMsgTV.setVisibility(View.INVISIBLE);

        // Check to see if the form is filled
        String formValidationMsg = userRegistrationHelper.validateRegisterForm(fName, lName, dob,
                email, confirmEmail, password, confirmPassword);
        if (!formValidationMsg.equalsIgnoreCase("CAN REGISTER")) {
            showErrorMsg(formValidationMsg);
            return false;
        }

        // Check for a valid date
        if(!userRegistrationHelper.isValidDate(dob)) {
            showErrorMsg("Invalid dob. Enter the date in format mm-dd-yyyy");
            return false;
        }

        // Check for a valid email
        if(!userRegistrationHelper.validateEmail(email)) {
            showErrorMsg("Invalid email");
            return false;
        }
        if(!userRegistrationHelper.validateEmail(confirmEmail)) {
            showErrorMsg("Invalid confirm email");
            return false;
        }

        // Check if emails match
        if(!userRegistrationHelper.validateStringMatch(email, confirmEmail)) {
            showErrorMsg("Emails do not match");
            return false;
        }

        // Check if password match
        if(!userRegistrationHelper.validateStringMatch(password, confirmPassword)) {
            showErrorMsg("Passwords do not match");
            return false;
        }

        return true;
    }

    public void showErrorMsg(String msg) {
        errorMsgTV.setText(msg);
        errorMsgTV.setVisibility(View.VISIBLE);
    }

    public interface OnRegisterButtonClickListener {
        void onRegisterNewUser(boolean canRegister);
        void onBackButtonPressed();
    }
}