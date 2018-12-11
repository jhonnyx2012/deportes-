package com.kaiman.sports.data.entity;

import com.kaiman.sports.utils.Utils;

/**
 * Created by jhonnybarrios on 3/12/18
 */

public class User {
    public static final String PERMISSION_MONITOR = "1";
    public static final String PERMISSION_BASIC = "2";
    public static final String PERMISSION_ADMINISTRATOR = "3";
    public String id;
    public String provider;
    public String uid;
    public boolean allow_password_change;
    public String first_name;
    public String last_name;
    public String nickname;
    public String address;
    public String image;
    public String rut;
    public String email;
    //[1, "monitor"], [2, "basic"]
    public String permission_id;
    public String image_file_name;
    public String image_content_type;
    public String image_file_size;
    public String image_updated_at;

    public String getFullName() {
        return first_name+" "+last_name;
    }

    public boolean isMonitor() {
        return permission_id != null && User.PERMISSION_MONITOR.equals(permission_id);
    }

    public boolean canSubscribe() {
        return isBasicUser();
    }

    public boolean canEditLesson() {
        return permission_id != null && !isBasicUser();
    }

    public boolean canCreateLessons() {
        return canEditLesson();
    }

    public boolean isBasicUser() {
        return permission_id != null && permission_id.equals(User.PERMISSION_BASIC);
    }

    public boolean isAdministrator() {
        return permission_id != null && permission_id.equals(User.PERMISSION_ADMINISTRATOR);
    }

    public boolean canCreatePublications() {
        return isAdministrator() || isMonitor();
    }

    public boolean hasFilledAllPersonalData() {
        return Utils.nonNullOrEmpty(first_name)
                &&Utils.nonNullOrEmpty(last_name)
                &&Utils.nonNullOrEmpty(address)
                &&Utils.nonNullOrEmpty(rut);
    }
}