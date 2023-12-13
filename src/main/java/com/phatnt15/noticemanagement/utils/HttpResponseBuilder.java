package com.phatnt15.noticemanagement.utils;

import com.phatnt15.noticemanagement.dtos.GenericResponse;

/**
 * The type Http response builder.
 */
public final class HttpResponseBuilder {

    /**
     * To response generic response.
     *
     * @param data the data
     * @return the generic response
     */
    public static GenericResponse<?> toResponse(Object data) {
        GenericResponse<Object> response = new GenericResponse<>();
        response.setMessage(data);

        return response;
    }
}
