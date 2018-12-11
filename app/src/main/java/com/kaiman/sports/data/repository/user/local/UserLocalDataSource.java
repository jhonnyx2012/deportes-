package com.kaiman.sports.data.repository.user.local;

import com.kaiman.sports.data.entity.User;

/**
 * Created by jhonnybarrios on 3/12/18
 */

public interface UserLocalDataSource {
    User getLoggedUser();

    String getDeviceRegistration();

    void setLoggedUser(User user);

    void setDeviceRegistration(String deviceRegistrationId);

    void clearRegistrationId();

    void setPushToken(String refreshedToken);

    String getPushToken();

    void clearPushToken();
}