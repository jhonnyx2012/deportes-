package com.kaiman.sports.data.local.api;

/**
 * Created by jhonnybarrios on 3/12/18
 */

public interface ApiAuthLocalDataSource {
    boolean isAuthenticated();
    String getAccessToken();
    String getClient();
    String getExpiry();
    String getTokenType();
    String getUid();

    void setAccessToken(String value);
    void setClient(String value);
    void setExpiry(String value);
    void setTokenType(String value);
    void setUid(String value);

    void clear();
}