package com.phatnt15.noticemanagement.repositories.wrappers;

import com.phatnt15.noticemanagement.entities.Notice;
import com.phatnt15.noticemanagement.repositories.NoticeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.UUID;


/**
 * The type Notice repo wrapper.
 */
@Component
@Slf4j
public class NoticeRepoWrapper extends AbstractRepoWrapper<NoticeRepository> {

    /**
     * Instantiates a new Notice repo wrapper.
     *
     * @param entityManager the entity manager
     * @param repository    the repository
     */
    public NoticeRepoWrapper(EntityManager entityManager,
                             NoticeRepository repository) {
        super(entityManager, repository);
    }

    /**
     * Find all page.
     *
     * @param pageable the pageable
     * @return the page
     */
    public Page<Notice> findAll(Pageable pageable) {
        startSoftDeleteSession();

        Page<Notice> response = repository.findAll(pageable);

        endSoftDeleteSession();

        return response;
    }

    /**
     * Find by id notice.
     *
     * @param id the id
     * @return the notice
     */
    public Notice findById(UUID id) {
        startSoftDeleteSession();

        Notice notice = repository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(String.format(
                        "Notice with id [%s] was not found!",
                        id
                )));

        endSoftDeleteSession();

        return notice;
    }

    /**
     * Find all page.
     *
     * @param spec     the spec
     * @param pageable the pageable
     * @return the page
     */
    public Page<Notice> findAll(Specification<Notice> spec, Pageable pageable) {
        startSoftDeleteSession();

        Page<Notice> response = repository.findAll(spec, pageable);

        endSoftDeleteSession();

        return response;
    }

    @Override
    public String getDeletedFilterName() {
        return "deletedNoticesFilter";
    }
}
