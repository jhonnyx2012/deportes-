package com.kaiman.sports.data.mapper;

import com.core.data.repository.mapper.Mapper;
import com.kaiman.sports.data.entity.LessonEntity;
import com.kaiman.sports.main.workshops.model.LessonViewModel;

import org.joda.time.DateTime;

/**
 * Created by jhonnybarrios on 3/25/18
 */

public class LessonEntityMapper extends Mapper<LessonEntity, LessonViewModel> {

    @Override
    public LessonViewModel map(LessonEntity value) {
        LessonViewModel result = new LessonViewModel();
        result.id = value.id;
        result.title = value.title;
        result.startDate = new DateTime(value.start_date);
        result.timeInterval = value.time_interval;
        result.participantsCount = value.members_count;
        result.assistancePercent = value.attendance_percentaje.replace(" ", "");
        result.content = value.content;
        result.objetive = value.objetive;
        if (value.time_interval != null && !value.time_interval.trim().isEmpty()) {
            if (value.time_interval.split("-").length > 1) {
                result.endHour = value.time_interval.split("-")[1].trim();
            } else {
                result.endHour = "";
            }
            result.startHour = value.time_interval.split("-")[0].trim();
        } else {
            result.endHour = "";
            result.startHour = "";
        }
        return result;
    }

    @Override
    public LessonEntity reverseMap(LessonViewModel value) {
        throw new UnsupportedOperationException();
    }
}
