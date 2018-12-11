package com.kaiman.sports.main.news.presenter;

import android.support.annotation.NonNull;
import com.core.presentation.BasePresenter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.UploadTask;
import com.kaiman.sports.data.remote.DisposableCompletableObserver;
import com.kaiman.sports.data.repository.news.NewsDataSource;
import com.kaiman.sports.main.news.contract.CreateNews;
import com.kaiman.sports.utils.Utils;
import java.io.File;

/**
 * Created by jhonnybarrios on 3/12/18
 */

public class CreateNewsPresenter extends BasePresenter<CreateNews.View> implements CreateNews.Presenter {
    private NewsDataSource newsRepository;
    private File imageToUpload;

    public CreateNewsPresenter(@NonNull NewsDataSource newsRepository) {
        this.newsRepository = newsRepository;
    }

    @Override
    public void attemptPost(String description) {
        view.clearErrorFromFields();

        if (imageToUpload == null) {
            view.showNoImageTakedError();
        } else if (!Utils.nonNullOrEmpty(description)) {
            view.showEmptyDescriptionError();
        } else {
            uploadFile(description, imageToUpload);
        }
    }

    private void uploadFile(final String description, File imageToUpload) {
        view.showProgress();
        Utils.uploadImage(view, imageToUpload, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String imageUrl = taskSnapshot.getDownloadUrl().toString();
                doPost(description, imageUrl);
            }
        });
    }

    @Override
    public void setImageToUpload(File file) {
        view.showImage(file);
        this.imageToUpload = file;
    }

    private void doPost(String description, String imageUrl) {
        addSubscription(newsRepository.postPublication(description, imageUrl).subscribeWith(new DisposableCompletableObserver(view) {
            @Override
            public void onComplete() {
                view.showSuccessCreationMessage();
                view.goBack();
            }
        }));
    }
}
