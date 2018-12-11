package com.kaiman.sports.data.remote.push;

import android.util.Log;

import com.core.data.remote.SchedulerProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.kaiman.sports.Injection;
import com.kaiman.sports.data.entity.DeviceRegistration;
import com.kaiman.sports.data.repository.user.UserDataSource;

import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by jhonnybarrios on 3/29/18
 */

public class SportsFirebaseInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("SportsFirebaseInstance", "Refreshed token: " + refreshedToken);
        UserDataSource userRepository = Injection.provideUserRepository();
        userRepository.saveLocalPushToken(refreshedToken);
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer();
    }

    public static void sendRegistrationToServer() {
        UserDataSource userRepository = Injection.provideUserRepository();
        if (userRepository.gotPushToken() && !userRepository.deviceIsRegistered()) {
            String refreshedToken = userRepository.getLocalPushToken();
            SchedulerProvider schedulerProvider = Injection.provideSchedulerProvider();
            userRepository.registerDevice(refreshedToken)
                    .observeOn(schedulerProvider.observer())
                    .subscribeOn(schedulerProvider.subscriber())
                    .subscribe(new DisposableObserver<DeviceRegistration>() {
                        @Override
                        public void onNext(DeviceRegistration deviceRegistration) {
                            Log.d("SportsFirebaseInstance", "token sent to server "+deviceRegistration.id);
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onComplete() {}
                    });
        }

    }
}
