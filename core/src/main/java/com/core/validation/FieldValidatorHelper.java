package com.core.validation;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputLayout;

/**
 * Created by jhonnybarrios on 10/23/17.
 */

public class FieldValidatorHelper {
    private final Resources resources;

    public FieldValidatorHelper(Context context) {
        resources= context.getResources();
    }

    public void clearErrorFromFields(TextInputLayout... fields) {
        for (TextInputLayout field : fields) {
            field.setErrorEnabled(false);
            field.setError(null);
        }
    }

    public void setErrorToField(TextInputLayout textInputLayout, @StringRes int error) {
        setErrorToField(textInputLayout, resources.getString(error));
    }

    public void setErrorToField(TextInputLayout textInputLayout,String error) {
        textInputLayout.setErrorEnabled(true);
        textInputLayout.setError(error);
        textInputLayout.requestFocus();
    }
}
