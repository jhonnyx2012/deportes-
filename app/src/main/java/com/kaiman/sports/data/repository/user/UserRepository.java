package com.kaiman.sports.data.repository.user;

import com.core.data.remote.SchedulerProvider;
import com.kaiman.sports.data.entity.DeviceRegistration;
import com.kaiman.sports.data.entity.User;
import com.kaiman.sports.data.remote.ApiService;
import com.kaiman.sports.data.remote.response.ApiResponse;
import com.kaiman.sports.data.repository.user.local.LocalUserRepository;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;

/**
 * Created by jhonnybarrios on 3/12/18
 */

public class UserRepository implements UserDataSource {

    private final SchedulerProvider schedulerProvider;
    private LocalUserRepository local;
    private ApiService apiService;

    public UserRepository(LocalUserRepository local, ApiService apiService, SchedulerProvider schedulerProvider) {
        this.local = local;
        this.apiService = apiService;
        this.schedulerProvider = schedulerProvider;
    }

    @Override
    public Observable<User> login(String email, String password) {
        return apiService.login(email, password)
                .observeOn(schedulerProvider.observer())
                .subscribeOn(schedulerProvider.subscriber())
                .map(new Function<ApiResponse<User>, User>() {
                    @Override
                    public User apply(ApiResponse<User> userApiResponse) throws Exception {
                        local.setLoggedUser(userApiResponse.data);
                        return userApiResponse.data;
                    }
                });
    }

    @Override
    public Completable resetPassword(String email) {
        return apiService.resetPassword(email)
                .observeOn(schedulerProvider.observer())
                .subscribeOn(schedulerProvider.subscriber());
    }

    @Override
    public Observable<User> register(String email, String password, String confirmPassword) {
        return apiService.register(email, password, confirmPassword)
                .observeOn(schedulerProvider.observer())
                .subscribeOn(schedulerProvider.subscriber())
                .map(new Function<ApiResponse<User>, User>() {
                    @Override
                    public User apply(ApiResponse<User> userApiResponse) throws Exception {
                        local.setLoggedUser(userApiResponse.data);
                        return userApiResponse.data;
                    }
                });
    }

    @Override
    public User getLoggedUser() {
        return local.getLoggedUser();
    }

    @Override
    public Observable<User> editUser(String rut, String firstName, String lastName, String address, String password) {
        String userId = local.getLoggedUser().id;
        return apiService.editUser(userId, rut, firstName, lastName, address, password, password)
                .observeOn(schedulerProvider.observer())
                .subscribeOn(schedulerProvider.subscriber())
                .map(new Function<ApiResponse<User>, User>() {
                    @Override
                    public User apply(ApiResponse<User> userApiResponse) throws Exception {
                        local.setLoggedUser(userApiResponse.data);
                        return userApiResponse.data;
                    }
                });
    }

    @Override
    public Observable<DeviceRegistration> registerDevice(String refreshedToken) {
        if (local.getLoggedUser() == null) {
            return Observable.error(new Exception("user_not_logged_in"));
        }
        String userId = local.getLoggedUser().id;
        return apiService.registerDevice(userId, refreshedToken)
                .observeOn(schedulerProvider.observer())
                .subscribeOn(schedulerProvider.subscriber())
                .map(new Function<ApiResponse<DeviceRegistration>, DeviceRegistration>() {
                    @Override
                    public DeviceRegistration apply(ApiResponse<DeviceRegistration> deviceRegistrationApiResponse) throws Exception {
                        local.setDeviceRegistration(deviceRegistrationApiResponse.data.id);
                        return deviceRegistrationApiResponse.data;
                    }
                });
    }

    @Override
    public Completable deleteThisDevice() {
        String deviceRegistrationId = local.getDeviceRegistration();
        if (deviceRegistrationId == null) {
            local.clearRegistrationId();
            local.clearPushToken();
            return Completable.complete();
        }
        return apiService.deleteDevice(deviceRegistrationId).doOnComplete(new Action() {
            @Override
            public void run() throws Exception {
                local.clearRegistrationId();
                local.clearPushToken();
            }
        });
    }

    @Override
    public void saveLocalPushToken(String refreshedToken) {
        local.setPushToken(refreshedToken);
    }

    @Override
    public boolean gotPushToken() {
        return local.getPushToken() != null;
    }

    @Override
    public String getLocalPushToken() {
        return local.getPushToken();
    }

    @Override
    public boolean deviceIsRegistered() {
        return local.getDeviceRegistration() != null;
    }

    @Override
    public void logout() {
        local.clear();
    }

    @Override
    public Observable<User> editUserPhoto(String photoUrl) {
        String userId = local.getLoggedUser().id;
        return apiService.updateUserImage(userId, photoUrl)
                .observeOn(schedulerProvider.observer())
                .subscribeOn(schedulerProvider.subscriber())
                .map(new Function<ApiResponse<User>, User>() {
                    @Override
                    public User apply(ApiResponse<User> userApiResponse) throws Exception {
                        local.setLoggedUser(userApiResponse.data);
                        return userApiResponse.data;
                    }
                });
    }
}