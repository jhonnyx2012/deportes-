package com.kaiman.sports.data.mapper;

import com.core.data.repository.mapper.Mapper;
import com.kaiman.sports.data.entity.NewsEntity;
import com.kaiman.sports.data.entity.User;
import com.kaiman.sports.main.news.PublicationViewModel;
import org.joda.time.DateTime;

/**
 * Created by jhonnybarrios on 3/25/18
 */

public class NewsEntityMapper extends Mapper<NewsEntity, PublicationViewModel> {

    private User actualUser;

    @Override
    public PublicationViewModel map(NewsEntity value) {
        PublicationViewModel result = new PublicationViewModel();
        result.id = value.id;
        result.description = value.description;
        result.userPhotoUrl = value.user_image;
        result.reactionId = value.reaction_id;
        result.photoUrl = value.image;
        result.userName = value.user_full_name;
        result.date = new DateTime(value.created_at).toDate();
        result.likesCount = value.likes_count;
        result.canDelete = actualUser.id.equals(value.user_id) || actualUser.isAdministrator();
        result.iHaveCommented = true;
        result.commentsCount = value.commentaries_count;
        return result;
    }

    @Override
    public NewsEntity reverseMap(PublicationViewModel value) {
        throw new UnsupportedOperationException();
    }

    public void setActualUser(User actualUser) {
        this.actualUser = actualUser;
    }
}
