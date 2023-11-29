package com.siriwardana.whatsmyhandicap.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.siriwardana.whatsmyhandicap.R;

public class LoginFragment extends Fragment {

    private PasswordVisibilityToggleHelper passwordVisibilityToggleHelper;

    private EditText emailET, passwordET;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        emailET = (EditText) view.findViewById(R.id.et_email_address);
        passwordET = (EditText) view.findViewById(R.id.et_password);

        view.findViewById(R.id.btn_login).setOnClickListener(v -> {
            ((onLoginButtonClickListener) requireActivity()).onLoginButtonClicked();
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

    public interface onLoginButtonClickListener {
        void onLoginButtonClicked();
        void onRegisterButtonClicked();
    }
}
