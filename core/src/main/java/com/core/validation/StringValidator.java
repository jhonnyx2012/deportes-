package com.core.validation;

/**
 * Created by jhonnybarrios on 10/23/17
 */

public abstract class StringValidator implements Validator {
    public boolean isEmpty(String value) {
        return value!=null&&value.trim().isEmpty();
    }
}