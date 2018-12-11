package com.kaiman.sports.data.mapper;

import com.core.data.repository.mapper.Mapper;
import com.kaiman.sports.data.entity.DisciplineEntity;
import com.kaiman.sports.main.workshops.model.DisciplineViewModel;

/**
 * Created by jhonnybarrios on 3/25/18
 */

public class DisciplineEntityMapper extends Mapper<DisciplineEntity, DisciplineViewModel> {
    @Override
    public DisciplineViewModel map(DisciplineEntity value) {
        DisciplineViewModel result = new DisciplineViewModel();
        result.id = value.id;
        result.name = value.name;
        return result;
    }

    @Override
    public DisciplineEntity reverseMap(DisciplineViewModel value) {
        throw new UnsupportedOperationException();
    }
}
