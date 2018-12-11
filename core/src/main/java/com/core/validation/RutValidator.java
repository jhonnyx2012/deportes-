package com.core.validation;

import android.support.annotation.NonNull;
import java.util.regex.Pattern;

/**
 * Created by jhonnybarrios on 10/23/17
 */

public class RutValidator extends StringValidator {
    private Pattern pattern = Pattern.compile("\\b\\d{1,8}-[K|k|0-9]");
    @Override
    public boolean isValid(@NonNull String value) {
        return pattern.matcher(value).matches();
    }
}