package com.core.data.repository.mapper;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by jhonnybarrios on 10/23/17.
 */

public abstract class Mapper<T1, T2> {

    public abstract T2 map(T1 value);

    public abstract T1 reverseMap(T2 value);

    public List<T2>  map(List<T1> values){
        ArrayList<T2> returnValues = new ArrayList<>();
        for (T1 value : values) {
            returnValues.add(map(value));
        }
        return returnValues;
    }

    public List<T1> reverseMap(List<T2> values) {
        ArrayList<T1> returnValues = new ArrayList<>();
        for (T2 value : values) {
            returnValues.add(reverseMap(value));
        }
        return returnValues;
    }



}