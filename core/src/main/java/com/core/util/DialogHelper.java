package com.core.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import com.core.R;

/**
 * Created by jhonnybarrios on 10/23/17
 */

public class DialogHelper {
    protected Context context;
    protected AlertDialog dialog;

    public DialogHelper(Context context) {
        this.context = context;
    }

    public void attachContext(Context context) {
        this.context = context;
    }

    public void showMessageDialog(int idString) {
        showMessageDialog(context.getString(idString));
    }

    public void showMessageDialog(String text) {
        showMessageDialog(text,R.string.default_title);
    }

    public void showErrorDialog(int idString) {
        showErrorDialog(context.getString(idString));
    }

    public void showErrorDialog(String text) {
        showMessageDialog(text, R.string.ups);
    }

    public void showMessageDialog(String text, @StringRes int title) {
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
            dialog = null;
        }
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            dialog = builder.setCancelable(false)
                    .setTitle(title)
                    .setMessage(text)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).create();
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}