package com.kaiman.sports.data.repository.user;

import com.kaiman.sports.data.entity.DeviceRegistration;
import com.kaiman.sports.data.entity.User;
import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Created by jhonnybarrios on 3/12/18
 */

public interface UserDataSource {
    Observable<User> login(String email, String password);
    Completable resetPassword(String email);
    Observable<User> register(String email, String password, String confirmPassword);
    User getLoggedUser();

    Observable<User> editUser(String rut, String firstName, String lastName, String address, String password);

    Observable<DeviceRegistration> registerDevice(String refreshedToken);
    Completable deleteThisDevice();

    void saveLocalPushToken(String refreshedToken);
    boolean gotPushToken();

    String getLocalPushToken();

    boolean deviceIsRegistered();

    void logout();

    Observable<User> editUserPhoto(String photoUrl);
}