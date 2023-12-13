package com.phatnt15.noticemanagement.services;

import com.phatnt15.noticemanagement.dtos.NoticeRequest;
import com.phatnt15.noticemanagement.dtos.NoticeResponse;
import com.phatnt15.noticemanagement.dtos.NoticeSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * The interface Notice service.
 */
public interface NoticeService {
    /**
     * Gets all notices.
     *
     * @param pageable the pageable
     * @return the all notices
     */
    Page<NoticeResponse> getAllNotices(Pageable pageable);

    /**
     * Gets notice by id.
     *
     * @param id the id
     * @return the notice by id
     */
    NoticeResponse getNoticeById(UUID id);

    /**
     * Search notice page.
     *
     * @param searchRequest the search request
     * @param pageable      the pageable
     * @return the page
     */
    Page<NoticeResponse> searchNotice(NoticeSearchRequest searchRequest, Pageable pageable);

    /**
     * Create notice notice response.
     *
     * @param request     the request
     * @param attachments the attachments
     * @return the notice response
     */
    NoticeResponse createNotice(NoticeRequest request, MultipartFile[] attachments);

    /**
     * Update notice notice response.
     *
     * @param id      the id
     * @param request the request
     * @return the notice response
     */
    NoticeResponse updateNotice(UUID id, NoticeRequest request);

    /**
     * Delete notice.
     *
     * @param id the id
     */
    void deleteNotice(UUID id);
}
