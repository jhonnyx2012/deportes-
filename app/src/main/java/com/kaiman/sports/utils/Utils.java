package com.kaiman.sports.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.core.presentation.contract.BaseView;
import com.core.presentation.fragment.BaseFragment;
import com.core.util.AndroidUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kaiman.sports.BuildConfig;
import com.kaiman.sports.Injection;
import com.kaiman.sports.R;
import com.kaiman.sports.data.remote.DisposableObserver;
import com.kaiman.sports.data.repository.app.AppDataSource;
import com.kaiman.sports.main.TabsActivity;

import org.joda.time.DateTime;
import java.io.File;
import java.io.IOException;
import id.zelory.compressor.Compressor;
import pl.aprilapps.easyphotopicker.EasyImage;

/**
 * Created by jhonnybarrios on 3/12/18
 */

public class Utils {
    public static boolean nonNullOrEmpty(String text) {
        return text != null && !text.isEmpty() && !text.trim().isEmpty();
    }

    public static boolean nonNull(Object o) {
        return o != null;
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    private static final String POST_PATH = "posts";

    public static void uploadImage(final BaseView.Request view, File imageToUpload, final OnSuccessListener<UploadTask.TaskSnapshot> successListener) {
        try {
            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setContentType("image/jpg")
                    .build();
            StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
            Compressor compressor = Injection.provideCompressor();
            File imageCompressed = compressor.compressToFile(imageToUpload);
            Uri file = Uri.fromFile(imageCompressed);
            String fileName = new DateTime().getMillis()+"";
            StorageReference photoRef = mStorageRef.child(POST_PATH).child(fileName);

            photoRef.putFile(file, metadata)
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            if (taskSnapshot.getBytesTransferred() == 0) {
                                return;
                            }
                            long progressPercent = taskSnapshot.getTotalByteCount() / taskSnapshot.getBytesTransferred();
                            Log.d("uploadProgress", "progress = "+progressPercent);
                        }
                    })
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.d("uploadProgress", "upload finished total file size = "+(taskSnapshot.getTotalByteCount() / 1000)+" KB");
                            successListener.onSuccess(taskSnapshot);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            view.showRequestError(exception.getMessage());
                            exception.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            view.showRequestError(e.getMessage());
            e.printStackTrace();
        }
    }

    private static final int TYPE_IMAGE = 9854;
    private static final int REQUEST_CODE_PERMISSIONS = 23552;

    public static void openCameraWithPermissions(BaseFragment fragment) {
        openCameraWithPermissions(fragment.getActivity(), fragment);
    }

    public static void openCameraWithPermissions(Activity activity) {
        openCameraWithPermissions(activity, activity);
    }

    private static void openCameraWithPermissions(Activity activity, Object target) {
        String[] cameraPermissions = new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        int writePermissionCheck = ContextCompat.checkSelfPermission(activity,cameraPermissions[1]);
        int cameraPermissionCheck = ContextCompat.checkSelfPermission(activity, cameraPermissions[0]);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && (cameraPermissionCheck != PackageManager.PERMISSION_GRANTED
                || writePermissionCheck != PackageManager.PERMISSION_GRANTED)) {
            if (target instanceof Activity) {
                ActivityCompat.requestPermissions((Activity) target, cameraPermissions, REQUEST_CODE_PERMISSIONS);
            } else {
                ((BaseFragment)target).requestPermissions(cameraPermissions, REQUEST_CODE_PERMISSIONS);
            }

        } else {
            if (target instanceof Activity) {
                EasyImage.openChooserWithGallery((Activity) target,activity.getString(R.string.take_from), TYPE_IMAGE);
            } else {
                EasyImage.openChooserWithGallery((BaseFragment) target,activity.getString(R.string.take_from), TYPE_IMAGE);
            }
        }
    }

    public static boolean cameraPermissionAreGranted(int[] grantResults) {
        boolean success = true;
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                success = false;
                break;
            }
        }
        return success;
    }

    public static void checkAppVersion(final Context context) {
        AppDataSource appRepository = Injection.provideAppRepository();

        appRepository.fetchAppVersion().subscribe(new DisposableObserver<String>(null) {
            @Override
            public void onNext(String version) {
                if (!BuildConfig.VERSION_NAME.equals(version)) {
                    showNewAppVersionDialog(context);
                }
            }
        });
    }

    private static void showNewAppVersionDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(R.string.title_new_app_version);
        builder.setMessage(R.string.description_new_app_version);

        builder.setPositiveButton(R.string.go_to_playstore, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                AndroidUtils.goToPlayStore(context);
                dialog.dismiss();
            } });

        builder.setNegativeButton(R.string.later, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            } });
        builder.show();
    }
}