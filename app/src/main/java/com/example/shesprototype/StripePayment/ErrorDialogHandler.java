package com.example.shesprototype.StripePayment;


import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.example.shesprototype.R;

/**
 * A convenience class to handle displaying error dialogs.
 */
public class ErrorDialogHandler {

    FragmentManager mFragmentManager;

    public ErrorDialogHandler(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
    }

    public void showError(String errorMessage) {
        DialogFragment fragment = ErrorDialogFragment.newInstance(
                R.string.validationErrors, errorMessage);
        fragment.show(mFragmentManager, "error");
    }
}
