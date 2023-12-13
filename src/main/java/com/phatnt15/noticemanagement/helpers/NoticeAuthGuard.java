package com.phatnt15.noticemanagement.helpers;

import com.phatnt15.noticemanagement.repositories.NoticeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * The type Notice auth guard.
 */
@Component
@AllArgsConstructor
public class NoticeAuthGuard implements GenericAuthGuard<UUID> {

    private final AuthContextHelper authContextHelper;

    private final NoticeRepository noticeRepository;

    public boolean isObjectOwner(UUID uuid) {
        Optional<UUID> currentAuthUserId = authContextHelper.getCurrentAuthUserId();

        if (currentAuthUserId.isEmpty()) {
            return false;
        }

        return noticeRepository.existsByUserIdAndId(currentAuthUserId.get(), uuid);
    }
}
