package com.core.validation;

import android.support.annotation.NonNull;
import java.util.regex.Pattern;

/**
 * Created by jhonnybarrios on 10/23/17.
 */

public class EmailValidator extends StringValidator {
    private Pattern pattern = Pattern.compile("^.+@.+\\..+$");
    @Override
    public boolean isValid(@NonNull String value) {
        return pattern.matcher(value).matches();
    }
}
