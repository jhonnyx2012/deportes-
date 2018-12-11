package com.kaiman.sports.data.mapper;

import com.core.data.repository.mapper.Mapper;
import com.kaiman.sports.data.entity.LessonMemberEntity;
import com.kaiman.sports.main.workshops.model.LessonMemberViewModel;

/**
 * Created by jhonnybarrios on 3/25/18
 */

public class LessonMemberEntityMapper extends Mapper<LessonMemberEntity, LessonMemberViewModel> {

    @Override
    public LessonMemberViewModel map(LessonMemberEntity value) {
        LessonMemberViewModel result = new LessonMemberViewModel();
        result.id = value.id;
        result.attend = value.attend;
        result.userId = value.user_id;
        result.lessonId = value.lesson_id;
        result.memberImage = value.member_image;
        result.memberName = value.member_name;
        return result;
    }

    @Override
    public LessonMemberEntity reverseMap(LessonMemberViewModel value) {
        throw new UnsupportedOperationException();
    }
}