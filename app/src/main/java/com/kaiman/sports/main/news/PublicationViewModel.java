package com.kaiman.sports.main.news;


import java.util.Date;

/**
 * Created by jhonnybarrios on 3/15/18
 */

public class PublicationViewModel {
    public String description;
    public String photoUrl;
    public String userPhotoUrl;
    public String userName;
    public Date date;
    public boolean iHaveCommented;
    public boolean canDelete;
    public String commentsCount;
    public String likesCount;
    public String id;
    public String reactionId;
    public boolean isUpdating;

    public boolean iLikeIt() {
        return reactionId != null;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof PublicationViewModel && ((PublicationViewModel)obj).id.equals(id);
    }
}