package com.phatnt15.noticemanagement.repositories;

import com.phatnt15.noticemanagement.entities.Notice;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * The interface Notice repository.
 */
public interface NoticeRepository extends JpaRepository<Notice, UUID>, JpaSpecificationExecutor<Notice> {

    /*
    this method is an alternative of original findById
    Because currently @Filter won't work with entityManager.find
    known issue: https://hibernate.atlassian.net/browse/HHH-11973
     */
    @Override
    default Optional<Notice> findById(UUID id) {
        Notice notice = new Notice();
        notice.setId(id);
        return findOne(Example.of(notice));
    }

    /**
     * Exists by user id and id boolean.
     *
     * @param userId the user id
     * @param id     the id
     * @return the boolean
     */
    boolean existsByUserIdAndId(UUID userId, UUID id);


    /**
     * Title contains specification.
     *
     * @param title the title
     * @return the specification
     */
    default Specification<Notice> titleContains(String title) {
        if (StringUtils.isEmpty(title)) {
            return Specification.where(null);
        }
        return (root, cq, cb) -> cb.like(root.get("title"), "%" + title + "%");
    }

    /**
     * Content contains specification.
     *
     * @param content the content
     * @return the specification
     */
    default Specification<Notice> contentContains(String content) {
        if (StringUtils.isEmpty(content)) {
            return Specification.where(null);
        }

        return (root, cq, cb) -> cb.like(root.get("content"), "%" + content+ "%");
    }

    /**
     * Start time from specification.
     *
     * @param startTime the start time
     * @return the specification
     */
    default Specification<Notice> startTimeFrom(LocalDateTime startTime) {
        if (startTime == null) {
            return Specification.where(null);
        }

        return (root, cq, cb) -> cb.lessThanOrEqualTo(root.get("startTime"), startTime);
    }

    /**
     * End time to specification.
     *
     * @param endTime the end time
     * @return the specification
     */
    default Specification<Notice> endTimeTo(LocalDateTime endTime) {
        if (endTime == null) {
            return Specification.where(null);
        }

        return (root, cq, cb) -> cb.greaterThanOrEqualTo(root.get("endTime"), endTime);
    }

}
