package com.kaiman.sports.main.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.core.data.remote.SchedulerProvider;
import com.core.presentation.contract.BaseView;
import com.core.presentation.fragment.BaseRequestFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.UploadTask;
import com.kaiman.sports.Injection;
import com.kaiman.sports.R;
import com.kaiman.sports.data.entity.User;
import com.kaiman.sports.data.local.api.ApiAuthLocalDataSource;
import com.kaiman.sports.data.remote.DisposableCompletableObserver;
import com.kaiman.sports.data.remote.DisposableObserver;
import com.kaiman.sports.data.repository.user.UserDataSource;
import com.kaiman.sports.databinding.FragmentProfileBinding;
import com.kaiman.sports.intro.StartActivity;
import com.kaiman.sports.utils.Utils;

import java.io.File;
import java.util.List;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

public class ProfileFragment extends BaseRequestFragment<FragmentProfileBinding> {

    private UserDataSource userRepository;
    private ApiAuthLocalDataSource apiPreferences;
    private SchedulerProvider schedulerProvider;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_profile;
    }

    @Override
    protected void initView() {
        setupUser();
        binder.buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutConfirmationDialog();
            }
        });
        binder.imageEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(EditUserActivity.class);
            }
        });

        binder.imagePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyImage.handleActivityResult(requestCode, resultCode, data, getActivity(), new DefaultCallback() {
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
            public void onCanceled(EasyImage.ImageSource source, int type) {}
        });
    }

    private void onPhotosReturned(List<File> imagesFiles) {
        Utils.uploadImage(this, imagesFiles.get(0), new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                postImageUploaded(taskSnapshot.getDownloadUrl().toString());
            }
        });
    }

    private void postImageUploaded(String urlPhoto) {
        userRepository.editUserPhoto(urlPhoto).subscribe(new DisposableObserver<User>(this) {
            @Override
            public void onNext(User user) {
                setupUser();
            }
        });
    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.confirmation);
        builder.setMessage(R.string.confirmation_logout_message);

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                doLogout();
                dialog.dismiss();
            } });


        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            } });
        builder.show();
    }

    private void doLogout() {
        userRepository.deleteThisDevice()
                .observeOn(schedulerProvider.observer())
                .subscribeOn(schedulerProvider.subscriber())
                .subscribe(new DisposableCompletableObserver(ProfileFragment.this) {
                    @Override
                    public void onComplete() {
                        finishLogout();
                    }
                });
    }

    private void finishLogout() {
        userRepository.logout();
        apiPreferences.clear();
        finishActivity();
        startActivity(StartActivity.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        setupUser();
    }

    @Override
    protected void injectDependencies() {
        schedulerProvider = Injection.provideSchedulerProvider();
        userRepository = Injection.provideUserRepository();
        apiPreferences = Injection.provideApiAuthPreferences();
    }

    private void setupUser() {
        if (getActivity() == null) {
            return;
        }
        User user = userRepository.getLoggedUser();
        if (Utils.nonNullOrEmpty(user.first_name)) {
            binder.textPhotoTitle.setText(user.getFullName());
        } else {
            String userName = user.email.split("@")[0];
            binder.textPhotoTitle.setText(userName);
        }

        binder.textEmail.setText(user.email);

        Glide.with(getActivity())
                .load(user.image)
                .apply(new RequestOptions()
                        .placeholder(getVectorDrawable(R.drawable.profile_photo_placeholder))
                        .error(getVectorDrawable(R.drawable.profile_photo_placeholder)))
                .into(binder.imagePhoto);
        if (user.first_name != null) {
            binder.textName.setText(user.getFullName());
        }

        if (user.rut != null) {
            binder.textRut.setText(user.rut);
        }

        if (user.address != null) {
            binder.textAddress.setText(user.address);
        }
    }

    @Override
    protected BaseView.Request provideRequestViewHelper() {
        return Injection.provideIntroRequestViewHelper(binder.root);
    }
}