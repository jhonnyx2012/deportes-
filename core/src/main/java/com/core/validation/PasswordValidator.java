package com.core.validation;

/**
 * Created by jhonnybarrios on 10/23/17.
 */

public class PasswordValidator extends StringValidator{
    private int MIN_CHARACTERS = 4;

    public PasswordValidator(int minCharacters) {
        this.MIN_CHARACTERS = minCharacters;
    }

    @Override
    public boolean isValid(String value) {
        return !isEmpty(value) && value.length() >= MIN_CHARACTERS;
    }
}