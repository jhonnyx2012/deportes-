package com.kaiman.sports.data.remote;

import android.support.annotation.NonNull;
import com.kaiman.sports.data.local.api.ApiAuthLocalDataSource;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Insert headers to requests to allow server know that the client is authenticated
 *
 * access-token:plZEYXk-oeDtLtjczAhYzg
 * client:IibFztCl7hlMuQv1Rsa_7A
 * expiry:1522798556
 * token-type:Bearer
 * uid:jhonnyx2012@gmail.com
 * Content-Type:application/json
 *
 * Created by Jhonny Barrios on 3/25/18
 */

public class AuthenticationInterceptor implements Interceptor {
    private static final String ACCESS_TOKEN = "access-token";
    private static final String CLIENT = "client";
    private static final String EXPIRY = "expiry";
    private static final String TOKEN_TYPE = "token-type";
    private static final String UID = "uid";
    private static final String CONTENT_TYPE = "Content-Type";

    private ApiAuthLocalDataSource apiAuthPreferences;

    public AuthenticationInterceptor(@NonNull ApiAuthLocalDataSource apiAuthPreferences) {
        this.apiAuthPreferences = apiAuthPreferences;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (apiAuthPreferences.isAuthenticated()) {
             request = request.newBuilder()
                     .addHeader(ACCESS_TOKEN, apiAuthPreferences.getAccessToken())
                     .addHeader(CLIENT, apiAuthPreferences.getClient())
                     .addHeader(TOKEN_TYPE, apiAuthPreferences.getTokenType())
                     .addHeader(EXPIRY, apiAuthPreferences.getExpiry())
                     .addHeader(UID, apiAuthPreferences.getUid())
                     .addHeader(CONTENT_TYPE, "application/json")
                     .build();
        }

        Response response = chain.proceed(request);

        if (response.header(ACCESS_TOKEN) != null) {
            apiAuthPreferences.setAccessToken(response.header(ACCESS_TOKEN));
            apiAuthPreferences.setClient(response.header(CLIENT));
            apiAuthPreferences.setExpiry(response.header(EXPIRY));
            apiAuthPreferences.setTokenType(response.header(TOKEN_TYPE));
            apiAuthPreferences.setUid(response.header(UID));
        }

        return response;
    }
}
