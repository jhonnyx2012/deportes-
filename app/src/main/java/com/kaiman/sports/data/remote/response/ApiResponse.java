package com.kaiman.sports.data.remote.response;

import java.util.List;

/**
 * Created by jhonnybarrios on 3/12/18
 */

public class ApiResponse<T> {
    public String status;
    public T data;
    public List<String> errors;
    public String error;
}