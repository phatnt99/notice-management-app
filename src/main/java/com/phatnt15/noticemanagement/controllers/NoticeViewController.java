package com.phatnt15.noticemanagement.controllers;

import com.phatnt15.noticemanagement.services.NoticeViewService;
import com.phatnt15.noticemanagement.utils.HttpResponseBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * The type Notice view controller.
 */
@RestController
@RequestMapping("/api/v1/views")
@AllArgsConstructor
@Slf4j
public class NoticeViewController {

    private final NoticeViewService noticeViewService;

    /**
     * Increment and return notice view response entity.
     *
     * @param noticeId the notice id
     * @return the response entity
     */
    @GetMapping("/with-increment/notices/{noticeId}")
    public ResponseEntity<?> incrementAndReturnNoticeView(@PathVariable UUID noticeId) {
        return ResponseEntity.ok(HttpResponseBuilder.toResponse(noticeViewService.incrementAndCountByNoticeId(noticeId)));
    }
}
