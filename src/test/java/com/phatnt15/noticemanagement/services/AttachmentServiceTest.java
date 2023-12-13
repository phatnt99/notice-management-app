package com.phatnt15.noticemanagement.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phatnt15.noticemanagement.configurations.AppConfig;
import com.phatnt15.noticemanagement.dtos.AttachmentFileResponse;
import com.phatnt15.noticemanagement.dtos.AttachmentResponse;
import com.phatnt15.noticemanagement.entities.Attachment;
import com.phatnt15.noticemanagement.exceptions.NoticeViewFileException;
import com.phatnt15.noticemanagement.helpers.AuthContextHelper;
import com.phatnt15.noticemanagement.helpers.PageableCollectionHelper;
import com.phatnt15.noticemanagement.mappers.AttachmentMapper;
import com.phatnt15.noticemanagement.repositories.AttachmentRepository;
import com.phatnt15.noticemanagement.repositories.NoticeRepository;
import com.phatnt15.noticemanagement.repositories.UserRepository;
import com.phatnt15.noticemanagement.repositories.wrappers.AttachmentRepoWrapper;
import com.phatnt15.noticemanagement.services.impls.AttachmentServiceImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AttachmentServiceTest {

    @InjectMocks
    private AttachmentServiceImpl attachmentService;

    @Mock
    private NoticeRepository noticeRepository;

    @Mock
    private AttachmentRepository attachmentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AttachmentMapper attachmentMapper;

    @Mock
    private AttachmentRepoWrapper attachmentRepoWrapper;

    @Mock
    private AppConfig appConfig;

    @Mock
    private AuthContextHelper authContextHelper;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void whenCreateAttachments_shouldOk() throws Exception {
        UUID noticeId = UUID.randomUUID();
        Attachment entity = new Attachment();

        MockMultipartFile fileRequest
                = new MockMultipartFile(
                "file",
                "file",
                MediaType.APPLICATION_JSON_VALUE,
                mapper.writeValueAsString(entity).getBytes()
        );

        MultipartFile[] fileRequests = new MultipartFile[] {fileRequest};

        when(appConfig.getUploadDir()).thenReturn("attachments");
        when(authContextHelper.getCurrentAuthUserId()).thenReturn(Optional.of(UUID.randomUUID()));
        attachmentService.createAttachments(noticeId, fileRequests);

        Path fullPath = Paths.get(System.getProperty("user.dir"), "attachments", noticeId.toString());
        File result = new File(fullPath.toString());

        assertTrue(result.exists());
    }

    @Test
    public void whenCreateAttachments_withNotValidFile_shouldFail() throws Exception {
        UUID noticeId = UUID.randomUUID();

        MultipartFile[] fileRequests = new MultipartFile[] {null};

        assertThrows(Exception.class, () -> attachmentService.createAttachments(noticeId, fileRequests));
    }

    @Test
    public void whenGetAttachmentFile_shouldOk() throws Exception {
        String fileName = "file";

        // first create attachment
        UUID noticeId = UUID.randomUUID();
        Attachment entity = new Attachment();

        MockMultipartFile fileRequest
                = new MockMultipartFile(
                fileName,
                fileName,
                MediaType.APPLICATION_JSON_VALUE,
                mapper.writeValueAsString(entity).getBytes()
        );

        MultipartFile[] fileRequests = new MultipartFile[] {fileRequest};

        when(appConfig.getUploadDir()).thenReturn("attachments");
        when(authContextHelper.getCurrentAuthUserId()).thenReturn(Optional.of(UUID.randomUUID()));
        attachmentService.createAttachments(noticeId, fileRequests);

        // then get attachment
        Attachment attachment = new Attachment();
        attachment.setId(UUID.randomUUID());
        attachment.setFileName(fileName);
        attachment.setFilePath("attachments/" + noticeId);
        attachment.setContentType(MediaType.TEXT_PLAIN_VALUE);

        when(attachmentRepoWrapper.findById(any(UUID.class))).thenReturn(attachment);

        AttachmentFileResponse response = attachmentService.getAttachmentFile(attachment.getId());

        assertEquals(response.getContentType(), attachment.getContentType());
    }

    @Test
    public void whenGetAttachmentFile_withFileNotExist_shouldFail() throws Exception {
        String fileName = "file";

        // first create attachment
        UUID noticeId = UUID.randomUUID();

        Attachment attachment = new Attachment();
        attachment.setId(UUID.randomUUID());
        attachment.setFileName(fileName);
        attachment.setFilePath("attachments/" + noticeId);
        attachment.setContentType(MediaType.TEXT_PLAIN_VALUE);

        when(attachmentRepoWrapper.findById(any(UUID.class))).thenReturn(attachment);

        assertThrows(NoticeViewFileException.class, () -> attachmentService.getAttachmentFile(attachment.getId()));
    }

    @Test
    public void whenGetAttachmentFile_withMalformedFile_shouldFail() throws Exception {
        String fileName = "file";

        // first create attachment
        UUID noticeId = UUID.randomUUID();

        Attachment attachment = new Attachment();
        attachment.setId(UUID.randomUUID());
        attachment.setFileName("///");
        attachment.setFilePath("attachments/" + noticeId);
        attachment.setContentType(MediaType.TEXT_PLAIN_VALUE);

        when(attachmentRepoWrapper.findById(any(UUID.class))).thenReturn(attachment);

        assertThrows(Exception.class, () -> attachmentService.getAttachmentFile(attachment.getId()));
    }

    @Test
    public void whenGetAttachmentsByNoticeId_shouldOk() {
        int listSize = 5;
        when(attachmentRepoWrapper.findByNoticeId(any(UUID.class), any(Pageable.class)))
                .thenReturn(PageableCollectionHelper.generatePageResponse(Attachment.class, listSize));
        when(attachmentMapper.toResponse(any(Attachment.class))).thenReturn(new AttachmentResponse());

        Page<AttachmentResponse> response = attachmentService.getAttachmentsByNoticeId(UUID.randomUUID(), PageableCollectionHelper.getPageable());

        assertEquals(response.getTotalElements(), listSize);
    }

    @Test
    public void whenDeleteAttachment_shouldOk() {
        when(attachmentRepoWrapper.findById(any(UUID.class))).thenReturn(new Attachment());
        attachmentService.deleteAttachment(UUID.randomUUID());
    }

    @AfterAll
    public static void cleanUp() {
        String currentWorkingDir = System.getProperty("user.dir");
        Path fullPath = Paths.get(currentWorkingDir, "attachments");
        try {
            FileSystemUtils.deleteRecursively(fullPath);
        } catch (IOException e) {

        }
    }


}
