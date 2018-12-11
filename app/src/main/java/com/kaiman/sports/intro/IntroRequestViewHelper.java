package com.kaiman.sports.intro;

import android.view.View;
import android.view.ViewGroup;
import com.core.R;
import com.core.util.DialogHelper;
import com.core.util.PlaceholderRequestViewHelper;

/**
 * Created by jhonnybarrios on 10/23/17
 */

public class IntroRequestViewHelper extends PlaceholderRequestViewHelper {

    private final DialogHelper dialogHelper;

    public IntroRequestViewHelper(ViewGroup parent) {
        super(parent);
        dialogHelper = new DialogHelper(parent.getContext());
    }

    @Override
    public void showConnectionError(View.OnClickListener onClickListener) {
        hideProgress();
        dialogHelper.showErrorDialog(R.string.error_connection_message);
    }

    @Override
    public void showRequestError(String message) {
        hideProgress();
        dialogHelper.showErrorDialog(message);
    }

    @Override
    public void showRequestError(int imageRes, String description, View.OnClickListener onClickListener) {
        showRequestError(description);
    }

    @Override
    public void showMessage(int title, String message) {
        dialogHelper.showMessageDialog(message, title);
    }
}