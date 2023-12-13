package com.phatnt15.noticemanagement.controllers;

import com.phatnt15.noticemanagement.dtos.AttachmentFileResponse;
import com.phatnt15.noticemanagement.helpers.AttachmentAuthGuard;
import com.phatnt15.noticemanagement.services.AttachmentService;
import com.phatnt15.noticemanagement.utils.HttpResponseBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * The type Attachment controller.
 */
@RestController
@RequestMapping("/api/v1/attachments")
@AllArgsConstructor
@Slf4j
public class AttachmentController {

    private final AttachmentService attachmentService;

    private final AttachmentAuthGuard attachmentAuthGuard;

    /**
     * Gets attachments by notice id.
     *
     * @param noticeId the notice id
     * @param offset   the offset
     * @param limit    the limit
     * @return the attachments by notice id
     */
    @GetMapping("/notices/{noticeId}")
    public ResponseEntity<?> getAttachmentsByNoticeId(@PathVariable UUID noticeId,
                                                      @RequestParam(defaultValue = "0") Integer offset,
                                                      @RequestParam(defaultValue = "100") Integer limit) {
        Pageable pageable = PageRequest.of(offset, limit);

        return ResponseEntity.ok(HttpResponseBuilder.toResponse(attachmentService.getAttachmentsByNoticeId(noticeId, pageable)));
    }

    /**
     * Gets attachment file.
     *
     * @param attachmentId the attachment id
     * @return the attachment file
     */
    @GetMapping("/{attachmentId}/file")
    public ResponseEntity<?> getAttachmentFile(@PathVariable UUID attachmentId) {
        AttachmentFileResponse response = attachmentService.getAttachmentFile(attachmentId);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + response.getFileName());
        headers.add(HttpHeaders.CONTENT_TYPE, response.getContentType());

        return ResponseEntity.ok()
                .headers(headers)
                .body(response.getResource());
    }

    /**
     * Delete attachment response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("@attachmentAuthGuard.isObjectOwner(#id)")
    public ResponseEntity<?> deleteAttachment(@PathVariable UUID id) {
        attachmentService.deleteAttachment(id);

        return ResponseEntity.noContent().build();
    }
}
