package com.tripplanner.util;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.tripplanner.databinding.FragmentAddTripBinding;

public class ValidationUtil {
    private ValidationUtil() {
    }

    public static boolean tripValidation(FragmentAddTripBinding fragmentAddTripBinding) {

        return checkName(fragmentAddTripBinding.tripToolBar.tripName)
                && checkPlaces(fragmentAddTripBinding.placeView.fromEt)
                && checkPlaces(fragmentAddTripBinding.placeView.toEt)
                && checkDate(fragmentAddTripBinding.dateView.datePicker)
                && checkDate(fragmentAddTripBinding.dateView.datePicker);


    }

    private static boolean checkName(TextInputEditText tripName) {
        String name = tripName.getText().toString();
        boolean hasName = !name.isEmpty();
        if (!hasName) {
            tripName.setError("Please enter trip name");
            return false;
        }
        return true;
    }

    private static boolean checkPlaces(AutoCompleteTextView autoComplete) {
        String from = autoComplete.getText().toString();
        if (TextUtils.isEmpty(from)) {
            autoComplete.setError("Please enter this field");
            return false;
        }
        return true;
    }

    private static boolean checkDate(TextInputEditText textView) {
        String time = textView.getText().toString();
        if (TextUtils.isEmpty(time)) {
            textView.setError("Please enter date and time ");
            return false;
        }
        Log.d("check", "checkDate: ");
        return true;
    }
}
