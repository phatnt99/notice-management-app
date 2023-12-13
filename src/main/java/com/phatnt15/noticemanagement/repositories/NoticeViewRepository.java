package com.phatnt15.noticemanagement.repositories;

import com.phatnt15.noticemanagement.entities.NoticeView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * The interface Notice view repository.
 */
public interface NoticeViewRepository extends JpaRepository<NoticeView, UUID> {

    /**
     * Exists by notice id and viewer id boolean.
     *
     * @param noticeId the notice id
     * @param viewerId the viewer id
     * @return the boolean
     */
    boolean existsByNoticeIdAndViewerId(UUID noticeId, UUID viewerId);

    /**
     * Count by notice id long.
     *
     * @param noticeId the notice id
     * @return the long
     */
    Long countByNoticeId(UUID noticeId);
}
