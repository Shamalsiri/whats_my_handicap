package com.siriwardana.whatsmyhandicap.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.siriwardana.whatsmyhandicap.R;

public class RegisterFragment extends Fragment {

    private  PasswordVisibilityToggleHelper passwordVisibilityToggleHelper;

    private EditText fNameET, lNameET, dobET,
            emailET, emailConfirmET, passwordET, passwordConfirmET;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register, container, false);

        view.findViewById(R.id.btn_back).setOnClickListener(v -> {
            ((OnRegisterButtonClickListener) requireActivity()).onBackButtonPressed();

        });

        view.findViewById(R.id.btn_register).setOnClickListener(v -> {
            ((OnRegisterButtonClickListener) requireActivity()).onRegisterNewUser();
        });

        fNameET = (EditText) view.findViewById(R.id.et_first_name);
        lNameET = (EditText) view.findViewById(R.id.et_last_name);
        dobET = (EditText) view.findViewById(R.id.et_dob);
        emailET = (EditText) view.findViewById(R.id.et_email);
        emailConfirmET = (EditText) view.findViewById(R.id.et_email_confirm);
        passwordET = (EditText) view.findViewById(R.id.et_passwd);
        passwordConfirmET = (EditText) view.findViewById(R.id.et_passwd_confirm);

        // Password visibility toggle logic
        passwordVisibilityToggleHelper = new PasswordVisibilityToggleHelper();
        passwordET.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_password_visibility_off,0);
        passwordET.setOnTouchListener(passwordVisibilityToggleHelper.getPasswordTouchListener(passwordET));

        // Password visibility toggle logic
        passwordConfirmET.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_password_visibility_off,0);
        passwordConfirmET.setOnTouchListener(passwordVisibilityToggleHelper.getPasswordTouchListener(passwordConfirmET));

        return view;
    }

    public interface OnRegisterButtonClickListener {
        void onRegisterNewUser();
        void onBackButtonPressed();
    }
}