package com.kaiman.sports.main.workshops.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.core.presentation.activity.BaseRequestActivity;
import com.core.presentation.contract.BaseView;
import com.core.util.PlaceholderRequestViewHelper;
import com.kaiman.sports.Injection;
import com.kaiman.sports.R;
import com.kaiman.sports.databinding.ActivityWorkshopDetailBinding;
import com.kaiman.sports.main.profile.EditUserActivity;
import com.kaiman.sports.main.workshops.contract.WorkshopDetail;
import com.kaiman.sports.main.workshops.model.WorkshopViewModel;

/**
 * Created by jhonnybarrios on 3/28/18
 */

public class WorkshopDetailActivity extends BaseRequestActivity<ActivityWorkshopDetailBinding> implements WorkshopDetail.View {
    public static final String WORKSHOP_ID = "workshop_id";

    WorkshopDetail.Presenter presenter;

    @Override
    protected BaseView.Request provideRequestViewHelper() {
        return new PlaceholderRequestViewHelper(binder.content);
    }

    @Override
    protected void initView() {
        showUpButton(true);
        binder.collapsingToolbar.setTitle("...");
        binder.appBarLayout.setExpanded(false);
        binder.buttonSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSubscribeConfirmationDialog();
            }
        });
        binder.textHowToGetThere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onHowToGetThereClicked();
            }
        });
        presenter.setWorkshopId(getIntent().getExtras().getString(WORKSHOP_ID));
        presenter.initialize(this);
    }

    @Override
    public void openAddress(String address) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://maps.google.co.in/maps?q=" + address));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void showSubscribeConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.confirmation);
        builder.setMessage(R.string.confirmation_subscribe_message);

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                presenter.subscribeToWorkshop();
                dialog.dismiss();
            } });


        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            } });
        builder.show();
    }

    @Override
    protected void injectDependencies() {
        presenter = Injection.provideWorkshopDetailPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_workshop_detail;
    }

    @Override
    public void onRetryLoadingClicked() {
        presenter.onRetryClicked();
    }

    @Override
    public void setupDetails(WorkshopViewModel result) {
        hideProgress();
        binder.collapsingToolbar.setTitle(result.title);
        Glide.with(this)
                .load(result.photoUrl)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.workshop_placeholder)
                        .error(R.drawable.workshop_placeholder))
                .into(binder.imagePhoto);

        binder.appBarLayout.setExpanded(true);

        if (result.availability.equals("0")) {
            binder.buttonNoAvailability.setVisibility(View.VISIBLE);
            binder.buttonSubscribe.setVisibility(View.GONE);
        }
        setTextOrHide(binder.textAvailability, result.availability, R.string.n_available_spaces);
        setTextOrHide(binder.textInstructorName, result.instructorName, R.string.instructor_name);
        setTextOrHide(binder.textEnclosure, result.enclosure, R.string.enclosure_s);
        setTextOrHide(binder.textSchedule, result.schedules, R.string.time_interval);
        setTextOrHide(binder.textAddress, result.address, R.string.address_s);
        if (result.address != null && !result.address.isEmpty()) {
            binder.textHowToGetThere.setVisibility(View.VISIBLE);
        }

        if (result.description != null) {
            binder.textDescription.setText(result.description);
        }
    }

    private void setTextOrHide(TextView textView, String text, @StringRes int prefix) {
        if (text != null) {
            textView.setText(getString(prefix, text));
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showSubscribeButton() {
        if (binder.buttonNoAvailability.getVisibility() == View.GONE) {
            binder.buttonSubscribe.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideSubscribeButton() {
        binder.buttonSubscribe.setVisibility(View.GONE);
    }

    @Override
    public void showConnectionErrorDialog() {
        showMessage(R.string.ups, getString(R.string.error_connection_message));
    }

    @Override
    public void showSuccessSubscriptionMessage() {
        showMessage(R.string.great, getString(R.string.successfull_subscription));
    }

    @Override
    public void showAlreadySubscribed() {
        binder.buttonNoAvailability.setVisibility(View.GONE);
        binder.textAlreadySubscribed.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMustCompletePersonalDataMessage() {
        Toast.makeText(this, R.string.you_must_complete_your_personal_data_first, Toast.LENGTH_LONG).show();
    }

    @Override
    public void openEditUser() {
        startActivity(EditUserActivity.class);
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }
}