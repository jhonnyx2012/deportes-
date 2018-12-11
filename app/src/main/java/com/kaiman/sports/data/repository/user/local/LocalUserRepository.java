package com.kaiman.sports.data.repository.user.local;

import android.content.Context;
import com.core.data.local.preferences.Preferences;
import com.google.gson.Gson;
import com.kaiman.sports.data.entity.User;
import com.kaiman.sports.utils.Utils;

/**
 * Created by jhonnybarrios on 3/12/18
 */

public class LocalUserRepository extends Preferences implements UserLocalDataSource{

    private final Gson gson;

    enum Key {
        USER, DEVICE_REGISTRATION, PUSH_TOKEN
    }

    public LocalUserRepository(Context context) {
        super(context);
        gson = new Gson();
    }

    @Override public String getName() {return "UserPreferences";}

    @Override
    public User getLoggedUser() {
        String current = getString(Key.USER, "");

        if (Utils.nonNullOrEmpty(current)) {
            return gson.fromJson(current, User.class);
        }
        return null;
    }

    @Override
    public String getDeviceRegistration() {
       return getString(Key.DEVICE_REGISTRATION, null);
    }

    @Override
    public void setLoggedUser(User user) {
        save(Key.USER, gson.toJson(user));
    }

    @Override
    public void setDeviceRegistration(String deviceRegistrationId) {
        save(Key.DEVICE_REGISTRATION, deviceRegistrationId);
    }

    @Override
    public void clearRegistrationId() {
        remove(Key.DEVICE_REGISTRATION);
    }

    @Override
    public void setPushToken(String refreshedToken) {
        save(Key.PUSH_TOKEN, refreshedToken);
    }

    @Override
    public String getPushToken() {
        return getString(Key.PUSH_TOKEN, null);
    }

    @Override
    public void clearPushToken() {
        remove(Key.PUSH_TOKEN);
    }
}