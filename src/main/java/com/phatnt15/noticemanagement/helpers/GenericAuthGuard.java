package com.phatnt15.noticemanagement.helpers;

/**
 * The interface Generic auth guard.
 *
 * @param <ID> the type parameter
 */
public interface GenericAuthGuard<ID> {

    /**
     * Is object owner boolean.
     *
     * @param id the id
     * @return the boolean
     */
    boolean isObjectOwner(ID id);
}
