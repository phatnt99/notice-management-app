package com.phatnt15.noticemanagement.dtos;


import lombok.*;

/**
 * The type Generic response.
 *
 * @param <T> the type parameter
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GenericResponse<T> {

    private T message;
}
