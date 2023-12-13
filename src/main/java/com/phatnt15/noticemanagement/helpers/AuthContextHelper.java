package com.phatnt15.noticemanagement.helpers;

import com.phatnt15.noticemanagement.entities.User;
import com.phatnt15.noticemanagement.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * The type Auth context helper.
 */
@Component
@AllArgsConstructor
@Slf4j
public class AuthContextHelper {

    private final UserRepository userRepository;

    /**
     * Gets current auth user id.
     *
     * @return the current auth user id
     */
    public Optional<UUID> getCurrentAuthUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        return Optional.of((UUID) authentication.getCredentials());
    }

    /**
     * Is same with auth user boolean.
     *
     * @param user the user
     * @return the boolean
     */
    public boolean isSameWithAuthUser(User user) {
        Optional<UUID> currentAuthUserId = getCurrentAuthUserId();

        if (currentAuthUserId.isEmpty()) {
            return false;
        }

        return currentAuthUserId.get().equals(user.getId());
    }
}
