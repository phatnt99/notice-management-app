package com.phatnt15.noticemanagement.repositories;

import com.phatnt15.noticemanagement.entities.Attachment;
import com.phatnt15.noticemanagement.entities.Notice;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * The interface Attachment repository.
 */
public interface AttachmentRepository extends JpaRepository<Attachment, UUID> {

    /*
    this method is an alternative of original findByXXX
    Because currently @Filter won't work with entityManager.find
    known issue: https://hibernate.atlassian.net/browse/HHH-11973
     */
    @Override
    default Optional<Attachment> findById(UUID id) {
        Attachment attachment = new Attachment();
        attachment.setId(id);
        return findOne(Example.of(attachment));
    }


    /*
    this method is an alternative of original findByXXX
    Because currently @Filter won't work with entityManager.find
    known issue: https://hibernate.atlassian.net/browse/HHH-11973
     */
    default Page<Attachment> findByNoticeId(UUID noticeId, Pageable pageable) {
        Attachment attachment = new Attachment();
        Notice notice = new Notice();
        notice.setId(noticeId);
        attachment.setNotice(notice);
        return findAll(Example.of(attachment), pageable);
    }

    /**
     * Exists by user id and id boolean.
     *
     * @param userId the user id
     * @param id     the id
     * @return the boolean
     */
    boolean existsByUserIdAndId(UUID userId, UUID id);
}
