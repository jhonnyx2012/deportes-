package com.kaiman.sports.data.entity;

/**
 * Created by jhonnybarrios on 4/1/18
 */

public class NewsReaction {
    public String id;
    public String new_id;
    public String user_id;
    public String reaction;
    public String created_at;
    public String updated_at;

    public NewsReaction() {}

    public NewsReaction(String newsId, String userId) {
        this.new_id = newsId;
        this.user_id = userId;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof NewsReaction
                && user_id !=null
                && new_id !=null
                && user_id.equals(((NewsReaction) obj).user_id)
                && new_id.equals(((NewsReaction) obj).new_id);
    }
}
