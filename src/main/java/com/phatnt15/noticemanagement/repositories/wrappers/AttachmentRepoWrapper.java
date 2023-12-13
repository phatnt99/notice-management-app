package com.phatnt15.noticemanagement.repositories.wrappers;

import com.phatnt15.noticemanagement.entities.Attachment;
import com.phatnt15.noticemanagement.repositories.AttachmentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * The type Attachment repo wrapper.
 */
@Component
@Slf4j
public class AttachmentRepoWrapper extends AbstractRepoWrapper<AttachmentRepository> {

    /**
     * Instantiates a new Attachment repo wrapper.
     *
     * @param entityManager the entity manager
     * @param repository    the repository
     */
    public AttachmentRepoWrapper(EntityManager entityManager,
                                 AttachmentRepository repository) {
        super(entityManager, repository);
    }

    /**
     * Find by id attachment.
     *
     * @param id the id
     * @return the attachment
     */
    public Attachment findById(UUID id) {
        startSoftDeleteSession();

        Attachment attachment = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(
                        "Attachment with id [%s] was not found!",
                        id
                )));

        endSoftDeleteSession();

        return attachment;
    }

    /**
     * Find by notice id page.
     *
     * @param noticeId the notice id
     * @param pageable the pageable
     * @return the page
     */
    public Page<Attachment> findByNoticeId(UUID noticeId, Pageable pageable) {
        startSoftDeleteSession();

        Page<Attachment> response = repository.findByNoticeId(noticeId, pageable);

        endSoftDeleteSession();
        return response;
    }

    @Override
    public String getDeletedFilterName() {
        return "deletedAttachmentsFilter";
    }
}
