package com.siriwardana.whatsmyhandicap.helpers;

import android.util.Log;
import android.util.Patterns;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class UserRegistrationHelper {

    public UserRegistrationHelper() {

    }

    public boolean validateEmail(CharSequence email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean isValidDate(String inputDate) {
        SimpleDateFormat sdf  = new SimpleDateFormat("MM-dd-yyyy");
        sdf.setLenient(false);

        Date date;
        try {
            date = sdf.parse(inputDate);
            Log.d("SSIRI", "Valid date: " + date);
            return true;
        } catch (ParseException e) {
            Log.d("SSIRI", "invalid date");
            return false;
        }
    }

    public Date str2Date(String inputDate) {
        SimpleDateFormat sdf  = new SimpleDateFormat("MM-dd-yyyy");
        sdf.setLenient(false);

        Date date;
        try {
            date = sdf.parse(inputDate);
            return date;
        } catch (ParseException e) {
            return null;
        }
    }

    public boolean validateStringMatch(String email, String str2) {
        return email.equals(str2);
    }

    public String validateRegisterForm(String fName, String lName, String dob, String email,
                                       String confirmEmail, String password, String confirmPassword) {

        if (fName.length() == 0) {
            return "Invalid First Name";
        } else if (lName.length() == 0) {
            return "Invalid Last Name";
        } else if (dob.length() < 10) {
            return "Invalid DOB. Please enter the dob in the format dd-MM-yyyy";
        } else if (email.length() < 3) {
            return "Invalid Email Address";
        } else if (confirmEmail.length() < 3) {
            return "Invalid Confirm Email Address";
        } else if (password.length() < 5) {
            return "Invalid Password. Password must be at least 5 characters";
        } else if (confirmPassword.length() < 5) {
            return "Invalid Confirm Password. Password must be at least 5 characters";
        } else {
            return "CAN REGISTER";
        }

    }


}
