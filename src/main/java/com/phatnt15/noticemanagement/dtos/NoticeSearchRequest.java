package com.phatnt15.noticemanagement.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The type Notice search request.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NoticeSearchRequest implements Serializable {

    private String title;

    private String content;

    @Schema(type = "string", pattern = "yyyy-MM-dd HH:mm:ss", example = "2024-12-01 00:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @Schema(type = "string", pattern = "yyyy-MM-dd HH:mm:ss", example = "2024-12-02 00:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
}
