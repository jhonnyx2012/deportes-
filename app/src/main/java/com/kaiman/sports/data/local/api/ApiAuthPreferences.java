package com.kaiman.sports.data.local.api;

import android.content.Context;
import com.core.data.local.preferences.Preferences;

/**
 * Created by jhonnybarrios on 3/25/18
 */

public class ApiAuthPreferences extends Preferences implements ApiAuthLocalDataSource {

    enum Key {ACCESS_TOKEN, CLIENT, EXPIRY, TOKEN_TYPE, UID}

    public ApiAuthPreferences(Context context) {super(context);}

    @Override
    public String getName() {
        return "ApiAuthPreferences";
    }

    @Override
    public boolean isAuthenticated() {
        return getAccessToken() != null;
    }

    @Override
    public String getAccessToken() {
        return getString(Key.ACCESS_TOKEN);
    }

    @Override
    public String getClient() {
        return getString(Key.CLIENT);
    }

    @Override
    public String getExpiry() {
        return getString(Key.EXPIRY);
    }

    @Override
    public String getTokenType() {
        return getString(Key.TOKEN_TYPE);
    }

    @Override
    public String getUid() {
        return getString(Key.UID);
    }

    @Override
    public void setAccessToken(String value) {
        save(Key.ACCESS_TOKEN, value);
    }

    @Override
    public void setClient(String value) {
        save(Key.CLIENT, value);
    }

    @Override
    public void setExpiry(String value) {
        save(Key.EXPIRY, value);
    }

    @Override
    public void setTokenType(String value) {
        save(Key.TOKEN_TYPE, value);
    }

    @Override
    public void setUid(String value) {
        save(Key.UID, value);
    }
}
