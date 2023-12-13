package com.phatnt15.noticemanagement.helpers;

import com.phatnt15.noticemanagement.repositories.AttachmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * The type Attachment auth guard.
 */
@Component
@AllArgsConstructor
public class AttachmentAuthGuard implements GenericAuthGuard<UUID> {

    private final AuthContextHelper authContextHelper;

    private final AttachmentRepository attachmentRepository;

    @Override
    public boolean isObjectOwner(UUID uuid) {
        Optional<UUID> currentAuthUserId = authContextHelper.getCurrentAuthUserId();

        if (currentAuthUserId.isEmpty()) {
            return false;
        }

        return attachmentRepository.existsByUserIdAndId(currentAuthUserId.get(), uuid);
    }
}
