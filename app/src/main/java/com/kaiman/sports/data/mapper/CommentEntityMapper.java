package com.kaiman.sports.data.mapper;

import com.core.data.repository.mapper.Mapper;
import com.kaiman.sports.data.entity.CommentEntity;
import com.kaiman.sports.main.news.CommentViewModel;

import org.joda.time.DateTime;

/**
 * Created by jhonnybarrios on 3/25/18
 */

public class CommentEntityMapper extends Mapper<CommentEntity, CommentViewModel> {

    private String actualUserId;

    public void setActualUserId(String actualUserId) {
        this.actualUserId = actualUserId;
    }

    @Override
    public CommentViewModel map(CommentEntity value) {
        CommentViewModel result = new CommentViewModel();
        result.id = value.id;
        result.userId = value.user_id;
        result.newsId = value.new_id;
        result.commentary = value.commentary;
        result.date = new DateTime(value.created_at).toDate();
        result.userName = value.user_full_name;
        result.userPhotoUrl = value.user_image;
        result.canBeDeleted = actualUserId.equals(value.user_id);
        return result;
    }

    @Override
    public CommentEntity reverseMap(CommentViewModel value) {
        throw new UnsupportedOperationException();
    }
}
