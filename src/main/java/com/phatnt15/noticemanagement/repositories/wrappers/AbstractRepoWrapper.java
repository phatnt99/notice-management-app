package com.phatnt15.noticemanagement.repositories.wrappers;

import jakarta.persistence.EntityManager;
import lombok.NoArgsConstructor;
import org.hibernate.Filter;
import org.hibernate.Session;

/**
 * The type Abstract repo wrapper.
 *
 * @param <R> the type parameter
 */
@NoArgsConstructor
public abstract class AbstractRepoWrapper<R> {

    /**
     * The Entity manager.
     */
    protected EntityManager entityManager;

    /**
     * The Session.
     */
    protected Session session;

    /**
     * The Repository.
     */
    protected R repository;

    /**
     * Instantiates a new Abstract repo wrapper.
     *
     * @param entityManager the entity manager
     * @param repository    the repository
     */
    protected AbstractRepoWrapper(EntityManager entityManager,
                                  R repository) {
        this.entityManager = entityManager;
        this.repository = repository;
    }

    /**
     * Gets deleted filter name.
     *
     * @return the deleted filter name
     */
    public abstract String getDeletedFilterName();

    /**
     * Start soft delete session.
     */
    public void startSoftDeleteSession() {
        this.session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter(getDeletedFilterName());
        filter.setParameter("isDeleted", false);
    }

    /**
     * End soft delete session.
     */
    public void endSoftDeleteSession() {
        this.session.disableFilter(getDeletedFilterName());
    }
}
