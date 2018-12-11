package com.core.presentation.contract;

/**
 * Created by jhonnybarrios on 10/23/17.
 */

public interface IProgressView {
    void showProgress(boolean show);
    void showMessage(String message);
}