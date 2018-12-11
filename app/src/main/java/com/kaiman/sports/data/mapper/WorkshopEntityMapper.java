package com.kaiman.sports.data.mapper;

import com.core.data.repository.mapper.Mapper;
import com.kaiman.sports.data.entity.WorkshopEntity;
import com.kaiman.sports.main.workshops.model.WorkshopViewModel;

/**
 * Created by jhonnybarrios on 3/25/18
 */

public class WorkshopEntityMapper extends Mapper<WorkshopEntity, WorkshopViewModel> {
    @Override
    public WorkshopViewModel map(WorkshopEntity value) {
        WorkshopViewModel result = new WorkshopViewModel();
        result.id = value.id;
        result.availability = value.quota;
        result.title = value.name;
        result.instructorName = value.monitor_name;
        result.address = value.address;
        result.enclosure = value.enclosure;
        result.schedules = value.schedule;
        result.instructorPhotoUrl = value.monitor_image;
        result.photoUrl = value.image;
        result.description = value.description;
        return result;
    }

    @Override
    public WorkshopEntity reverseMap(WorkshopViewModel value) {
        throw new UnsupportedOperationException();
    }
}
