package com.phatnt15.noticemanagement.repositories;

import com.phatnt15.noticemanagement.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * The interface User repository.
 */
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Find by username optional.
     *
     * @param username the username
     * @return the optional
     */
    Optional<User> findByUsername(String username);

    /**
     * Exists by username boolean.
     *
     * @param username the username
     * @return the boolean
     */
    boolean existsByUsername(String username);
}
