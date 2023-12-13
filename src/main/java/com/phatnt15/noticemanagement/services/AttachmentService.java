package com.phatnt15.noticemanagement.services;

import com.phatnt15.noticemanagement.dtos.AttachmentFileResponse;
import com.phatnt15.noticemanagement.dtos.AttachmentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * The interface Attachment service.
 */
public interface AttachmentService {

    /**
     * Create attachments.
     *
     * @param noticeId the notice id
     * @param request  the request
     */
    void createAttachments(UUID noticeId, MultipartFile[] request);

    /**
     * Gets attachments by notice id.
     *
     * @param noticeId the notice id
     * @param pageable the pageable
     * @return the attachments by notice id
     */
    Page<AttachmentResponse> getAttachmentsByNoticeId(UUID noticeId, Pageable pageable);

    /**
     * Gets attachment file.
     *
     * @param attachmentId the attachment id
     * @return the attachment file
     */
    AttachmentFileResponse getAttachmentFile(UUID attachmentId);

    /**
     * Delete attachment.
     *
     * @param id the id
     */
    void deleteAttachment(UUID id);
}
