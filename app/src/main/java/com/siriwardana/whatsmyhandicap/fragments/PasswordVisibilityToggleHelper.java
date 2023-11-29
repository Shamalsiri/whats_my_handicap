package com.siriwardana.whatsmyhandicap.fragments;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.content.res.AppCompatResources;

import com.siriwardana.whatsmyhandicap.R;

public class PasswordVisibilityToggleHelper {
    private boolean isPasswordVisible = false;

    //Default constructor
    public PasswordVisibilityToggleHelper() {

    }

    private void togglePasswordVisibility(EditText passwordET) {
        if (!isPasswordVisible) {
            passwordET.setTransformationMethod(null);
            isPasswordVisible = true;

            setDrawable(passwordET, R.drawable.ic_password_visibility_off);
        } else {
            passwordET.setTransformationMethod(new PasswordTransformationMethod());
            isPasswordVisible = false;

            setDrawable(passwordET, R.drawable.ic_password_visibility);
        }

        //Move the cursor to the end of the ET
        passwordET.setSelection(passwordET.getText().length());
    }

    private void setDrawable(EditText passwordET, int drawableRes) {
        Drawable drawable = AppCompatResources.getDrawable(passwordET.getContext(), drawableRes);
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            passwordET.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawable, null);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public View.OnTouchListener getPasswordTouchListener(final EditText passET) {
        return (v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (passET.getRight() - passET.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    togglePasswordVisibility(passET);
                    return true;
                }
            }
            return false;
        };
    }
}
