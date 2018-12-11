package com.core.util;

import android.support.annotation.StringRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.core.R;
import com.core.presentation.contract.BaseView;

/**
 * Created by jhonnybarrios on 10/23/17
 */

public class PlaceholderRequestViewHelper implements BaseView.Request {
    private static final int NOT_SETTED = -1;
    private final ViewGroup parent;
    private final int contentIndex;

    public PlaceholderRequestViewHelper(ViewGroup parent) {
        this.parent = parent;
        contentIndex = parent.getChildCount();
    }

    public void showProgress() {
        clearAddedViews();
        View.inflate(parent.getContext(), R.layout.view_progress, parent);
    }

    private void clearAddedViews() {
        if (contentIndex != NOT_SETTED) {
            int count = parent.getChildCount() - contentIndex;
            parent.removeViews(contentIndex, count);
        }
    }

    @Override
    public void hideProgress() {
        clearAddedViews();
    }

    @Override
    public void showConnectionError(View.OnClickListener onClickListener) {
        showRequestError(R.drawable.ic_cloud_error, parent.getResources().getString(R.string.error_connection_message), onClickListener);
    }

    @Override
    public void showRequestError(int imageRes, String description, View.OnClickListener onClickListener) {
        clearAddedViews();
        View view = View.inflate(parent.getContext(), R.layout.layout_placeholder, parent);
        TextView textDescription = view.findViewById(R.id.textDescription);
        ImageView imageIcon = view.findViewById(R.id.imageIcon);
        textDescription.setText(description);
        imageIcon.setImageResource(imageRes);

        if (onClickListener != null) {
            view.findViewById(R.id.buttonAction).setOnClickListener(onClickListener);
        } else {
            view.findViewById(R.id.buttonAction).setVisibility(View.GONE);
        }
    }

    @Override
    public void showRequestError(String message) {
        clearAddedViews();
        Toast.makeText(parent.getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showMessage(@StringRes int title, String message) {
        clearAddedViews();
        Toast.makeText(parent.getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showPlaceholder(int imageRes, int textRes, View.OnClickListener onClickListener) {
        clearAddedViews();
        View view = View.inflate(parent.getContext(), R.layout.layout_placeholder, parent);
        TextView textDescription = view.findViewById(R.id.textDescription);
        ImageView imageIcon = view.findViewById(R.id.imageIcon);
        textDescription.setText(textRes);
        imageIcon.setImageResource(imageRes);

        if (onClickListener != null) {
            view.findViewById(R.id.buttonAction).setOnClickListener(onClickListener);
        } else {
            view.findViewById(R.id.buttonAction).setVisibility(View.GONE);
        }
    }

    @Override
    public void onRetryLoadingClicked() {
        throw new UnsupportedOperationException();
    }
}