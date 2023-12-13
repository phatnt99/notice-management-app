package com.phatnt15.noticemanagement.controllers;

import com.phatnt15.noticemanagement.dtos.NoticeRequest;
import com.phatnt15.noticemanagement.dtos.NoticeResponse;
import com.phatnt15.noticemanagement.dtos.NoticeSearchRequest;
import com.phatnt15.noticemanagement.helpers.NoticeAuthGuard;
import com.phatnt15.noticemanagement.helpers.UriBuldingHelper;
import com.phatnt15.noticemanagement.services.NoticeService;
import com.phatnt15.noticemanagement.utils.HttpResponseBuilder;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * The type Notice controller.
 */
@RestController
@RequestMapping("/api/v1/notices")
@AllArgsConstructor
@Slf4j
public class NoticeController {

    private final NoticeService noticeService;

    private final NoticeAuthGuard noticeAuthGuard;

    private final UriBuldingHelper uriBuldingHelper;

    /**
     * Gets all notices.
     *
     * @param offset the offset
     * @param limit  the limit
     * @return the all notices
     */
    @GetMapping
    public ResponseEntity<?> getAllNotices(@RequestParam(defaultValue = "0") Integer offset,
                                           @RequestParam(defaultValue = "100") Integer limit) {
        Pageable pageable = PageRequest.of(offset, limit, Sort.by("createdDate").descending());

        return ResponseEntity.ok(HttpResponseBuilder.toResponse(noticeService.getAllNotices(pageable)));
    }

    /**
     * Gets notice by id.
     *
     * @param id the id
     * @return the notice by id
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getNoticeById(@PathVariable UUID id) {
        return ResponseEntity.ok(HttpResponseBuilder.toResponse(noticeService.getNoticeById(id)));
    }

    /**
     * Search notice response entity.
     *
     * @param queryParams the query params
     * @param offset      the offset
     * @param limit       the limit
     * @return the response entity
     */
    @PostMapping("/query")
    public ResponseEntity<?> searchNotice(@RequestBody NoticeSearchRequest queryParams,
                                          @RequestParam(defaultValue = "0") Integer offset,
                                          @RequestParam(defaultValue = "100") Integer limit) {
        Pageable pageable = PageRequest.of(offset, limit, Sort.by("createdDate").descending());

        return ResponseEntity.ok(HttpResponseBuilder.toResponse(noticeService.searchNotice(queryParams, pageable)));

//        return ResponseEntity.ok(queryParams);
    }

    /**
     * Create notice response entity.
     *
     * @param request     the request
     * @param attachments the attachments
     * @return the response entity
     */
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createNotice(@RequestPart @Valid NoticeRequest request,
                                          @RequestPart(required = false) MultipartFile[] attachments) {
        NoticeResponse noticeResponse = noticeService.createNotice(request, attachments);
        return ResponseEntity
                .created(uriBuldingHelper.buildControllerObjectUri("/{id}", noticeResponse.getId()))
                .body(HttpResponseBuilder.toResponse(noticeResponse));
    }

    /**
     * Update notice response entity.
     *
     * @param id      the id
     * @param request the request
     * @return the response entity
     */
    @PutMapping("/{id}")
    @PreAuthorize("@noticeAuthGuard.isObjectOwner(#id)")
    public ResponseEntity<?> updateNotice(@PathVariable UUID id, @RequestBody @Valid NoticeRequest request) {
        return ResponseEntity.ok(HttpResponseBuilder.toResponse((noticeService.updateNotice(id, request))));
    }

    /**
     * Delete notice response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("@noticeAuthGuard.isObjectOwner(#id)")
    public ResponseEntity<?> deleteNotice(@PathVariable UUID id) {
        noticeService.deleteNotice(id);

        return ResponseEntity.noContent().build();
    }
}
