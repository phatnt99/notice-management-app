package com.phatnt15.noticemanagement.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * The type Notice response.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NoticeResponse implements Serializable {

    private UUID id;

    private String title;

    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime registrationDate;

    private String numberOfViewsUrl;

    private String author;
}
