package com.phatnt15.noticemanagement.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.phatnt15.noticemanagement.annotations.ValidDateRange;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The type Notice request.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ValidDateRange(
        fromDate = "startTime",
        toDate = "endTime"
)
public class NoticeRequest implements Serializable {

    @NotBlank
    @Size(max = 250)
    private String title;

    @NotBlank
    @Size(max = 1500)
    private String content;

    @Schema(type = "string", pattern = "yyyy-MM-dd HH:mm:ss", example = "2024-12-01 00:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @FutureOrPresent
    private LocalDateTime startTime;

    @Schema(type = "string", pattern = "yyyy-MM-dd HH:mm:ss", example = "2024-12-02 00:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @FutureOrPresent
    private LocalDateTime endTime;
}
