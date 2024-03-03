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
import com.siriwardana.whatsmyhandicap.database.Converters;
import com.siriwardana.whatsmyhandicap.database.DatabaseSingleton;
import com.siriwardana.whatsmyhandicap.database.User;
import com.siriwardana.whatsmyhandicap.helpers.PasswordVisibilityToggleHelper;
import com.siriwardana.whatsmyhandicap.helpers.UserRegistrationHelper;

import java.util.Date;

public class RegisterFragment extends Fragment {

    private final String TAG = RegisterFragment.class.getName();
    private PasswordVisibilityToggleHelper passwordVisibilityToggleHelper;
    private UserRegistrationHelper userRegistrationHelper;
    private TextView errorMsgTV;
    private EditText fNameET, lNameET, dobET,
            emailET, emailConfirmET, passwordET, passwordConfirmET;
    private DatabaseSingleton dbSingleton;
    private String fName, lName, dob, email, confirmEmail, password, confirmPassword;

    /**
     * Constructor
     */
    public RegisterFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register, container, false);
        userRegistrationHelper = new UserRegistrationHelper();
        dbSingleton = DatabaseSingleton.getDBInstance(getContext());

        errorMsgTV = view.findViewById(R.id.tv_error_msg);
        errorMsgTV.setVisibility(View.INVISIBLE);

        fNameET = view.findViewById(R.id.et_first_name);
        lNameET = view.findViewById(R.id.et_last_name);
        dobET = view.findViewById(R.id.et_dob);
        emailET = view.findViewById(R.id.et_email);
        emailConfirmET = view.findViewById(R.id.et_email_confirm);
        passwordET = view.findViewById(R.id.et_passwd);
        passwordConfirmET = view.findViewById(R.id.et_passwd_confirm);


        // Password visibility toggle logic
        passwordVisibilityToggleHelper = new PasswordVisibilityToggleHelper();
        passwordET.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                R.drawable.ic_password_visibility_off, 0);
        passwordET.setOnTouchListener(
                passwordVisibilityToggleHelper.getPasswordTouchListener(passwordET));

        // Password visibility toggle logic
        passwordConfirmET.setCompoundDrawablesWithIntrinsicBounds(0, 0
                , R.drawable.ic_password_visibility_off, 0);
        passwordConfirmET.setOnTouchListener(
                passwordVisibilityToggleHelper.getPasswordTouchListener(passwordConfirmET));

        view.findViewById(R.id.btn_back).setOnClickListener(v -> {
            ((OnRegisterButtonClickListener) requireActivity()).onRegisterFragmentBackButtonClicked();

        });

        view.findViewById(R.id.btn_register).setOnClickListener(v -> {
            boolean canRegister = validateRegisterFormInput() &&
                    validateNewUser(emailET.getText().toString().trim());

            if (canRegister) {
                registerNewUser();
            }
            ((OnRegisterButtonClickListener) requireActivity()).onRegisterFragmentRegisterButtonClicked(canRegister);
        });

        return view;
    }

    /**
     * Validate the new user email isn't on the database
     *
     * @param email
     * @return
     */
    public boolean validateNewUser(String email) {
        Log.d(TAG, "validateNewUser: Validating New User");
        if (dbSingleton.UserDao().getUserCountByEmail(email) == 0) {
            return true;
        }
        showErrorMsg(getString(R.string.email_is_already_registered));
        Log.d(TAG, getString(R.string.email_is_already_registered));
        return false;
    }

    /**
     * Validate that the inputs from the register form are valid values
     * @return
     */
    public boolean validateRegisterFormInput() {
        Log.d(TAG, "validateRegisterFormInput: Validating Register Form Input");
        fName = fNameET.getText().toString().trim();
        lName = lNameET.getText().toString().trim();
        dob = dobET.getText().toString().trim();
        email = emailET.getText().toString().trim();
        confirmEmail = emailConfirmET.getText().toString().trim();
        password = passwordET.getText().toString().trim();
        confirmPassword = passwordConfirmET.getText().toString().trim();

        errorMsgTV.setVisibility(View.INVISIBLE);

        // Check to see if the form is filled
        String formValidationMsg = userRegistrationHelper.validateRegisterForm(fName, lName, dob,
                email, confirmEmail, password, confirmPassword);
        if (!formValidationMsg.equalsIgnoreCase("CAN REGISTER")) {
            showErrorMsg(formValidationMsg);
            return false;
        }

        // Check for a valid date
        if (!userRegistrationHelper.isValidDate(dob)) {
            Log.d(TAG, "validateRegisterFormInput: Validating Date of Birth Failed");
            showErrorMsg(getString(R.string.invalid_dob));
            return false;
        }

        // Check for a valid email
        if (!userRegistrationHelper.validateEmail(email)) {
            Log.d(TAG, "validateRegisterFormInput: Validating Email Failed");
            showErrorMsg(getString(R.string.invalid_email));
            return false;
        }
        if (!userRegistrationHelper.validateEmail(confirmEmail)) {
            Log.d(TAG, "validateRegisterFormInput: Validating Confirm Email Failed");
            showErrorMsg(getString(R.string.invalid_confirm_email));
            return false;
        }

        // Check if emails match
        if (!userRegistrationHelper.validateStringMatch(email, confirmEmail)) {
            Log.d(TAG, "validateRegisterFormInput: Email and Confirm Email didn't match");
            showErrorMsg(getString(R.string.emails_do_not_match));
            return false;
        }

        // Check if password match
        if (!userRegistrationHelper.validateStringMatch(password, confirmPassword)) {
            Log.d(TAG, "validateRegisterFormInput: Password and Confirm Password didn't match");
            showErrorMsg(getString(R.string.passwords_do_not_match));
            return false;
        }

        return true;
    }

    /**
     * Registers New User
     */
    public void registerNewUser() {
        Log.d(TAG, "registerNewUser: Registering New User");
        Date birthday = userRegistrationHelper.str2Date(dob);
        long bDay;
        if (birthday != null) {
            bDay = Converters.dateToTimestamp(birthday);
        } else {
            bDay = Long.parseLong(null);
        }

        User newUser = new User();
        newUser.setFirstName(fName);
        newUser.setLastName(lName);
        newUser.setDob(bDay);
        newUser.setEmail(email);
        newUser.setPassword(password);

        Log.d(TAG, "registerNewUser: Inserting New User to the database");
        dbSingleton.UserDao().insert(newUser);
    }

    /**
     * Showing Error Message
     *
     * @param msg
     */
    public void showErrorMsg(String msg) {
        Log.d(TAG, "showErrorMsg: Showing Error Message");
        errorMsgTV.setText(msg);
        errorMsgTV.setVisibility(View.VISIBLE);
    }

    public interface OnRegisterButtonClickListener {
        void onRegisterFragmentRegisterButtonClicked(boolean canRegister);

        void onRegisterFragmentBackButtonClicked();
    }
}