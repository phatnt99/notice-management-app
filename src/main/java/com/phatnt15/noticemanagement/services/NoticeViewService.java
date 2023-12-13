package com.phatnt15.noticemanagement.services;

import java.util.UUID;

/**
 * The interface Notice view service.
 */
public interface NoticeViewService {

    /**
     * Increment and count by notice id long.
     *
     * @param noticeId the notice id
     * @return the long
     */
    Long incrementAndCountByNoticeId(UUID noticeId);
}
