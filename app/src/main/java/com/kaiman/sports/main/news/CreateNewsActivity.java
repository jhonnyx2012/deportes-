package com.kaiman.sports.main.news;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.core.presentation.activity.BaseRequestActivity;
import com.core.presentation.contract.BaseView;
import com.core.util.AndroidUtils;
import com.kaiman.sports.Injection;
import com.kaiman.sports.R;
import com.kaiman.sports.databinding.ActivityCreatePublicationBinding;
import com.kaiman.sports.main.news.contract.CreateNews;
import com.kaiman.sports.utils.Utils;

import java.io.File;
import java.util.List;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

/**
 * Created by jhonnybarrios on 4/1/18
 */

public class CreateNewsActivity extends BaseRequestActivity<ActivityCreatePublicationBinding> implements CreateNews.View{
    private CreateNews.Presenter presenter;

    @Override
    protected BaseView.Request provideRequestViewHelper() {
        return Injection.provideIntroRequestViewHelper(binder.root);
    }

    @Override
    public void showMessage(int title, String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void initView() {
        showUpButton(true);
        setTitle(R.string.title_create_publication);
        presenter.initialize(this);
        binder.imagePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });
        openCamera();
    }

    private void openCamera() {
        Utils.openCameraWithPermissions(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (Utils.cameraPermissionAreGranted(grantResults)) {
            openCamera();
        } else {
            showMessage(R.string.ups, getString(R.string.declined_permissions));
        }
    }

    @Override
    protected int getMenuId() {
        return R.menu.create_publication;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.post:
                presenter.attemptPost(getDescription());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void injectDependencies() {
        presenter = Injection.provideCreateNewsPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_create_publication;
    }

    public String getDescription() {
        return binder.inputDescription.getText().toString().trim();
    }

    @Override
    public void clearErrorFromFields() {
        AndroidUtils.hideSoftKeyboard(binder.inputDescription);
        binder.inputLayoutDescription.setError(null);
    }

    @Override
    public void showEmptyDescriptionError() {
        binder.inputLayoutDescription.setError(getString(R.string.empty_field_error));
    }

    @Override
    public void showSuccessCreationMessage() {
        Toast.makeText(this, getString(R.string.successfull_publication_creation), Toast.LENGTH_LONG).show();
    }

    @Override
    public void goBack() {
        onBackPressed();
    }

    @Override
    public void showImage(File file) {
        Glide.with(this)
                .load(file)
                .into(binder.imagePhoto);
    }

    @Override
    public void showNoImageTakedError() {
        showMessage(R.string.ups, getString(R.string.no_image_taked_error));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                showMessage(R.string.ups, getString(R.string.generic_error_message));
            }

            @Override
            public void onImagesPicked(@NonNull List<File> imagesFiles, EasyImage.ImageSource source, int type) {
                //Handle the images
                onPhotosReturned(imagesFiles);
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) { }
        });
    }

    private void onPhotosReturned(List<File> imagesFiles) {
        presenter.setImageToUpload(imagesFiles.get(0));
    }
}
