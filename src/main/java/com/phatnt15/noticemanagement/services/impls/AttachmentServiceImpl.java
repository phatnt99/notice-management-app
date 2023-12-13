package com.phatnt15.noticemanagement.services.impls;

import com.phatnt15.noticemanagement.configurations.AppConfig;
import com.phatnt15.noticemanagement.dtos.AttachmentFileResponse;
import com.phatnt15.noticemanagement.dtos.AttachmentResponse;
import com.phatnt15.noticemanagement.entities.Attachment;
import com.phatnt15.noticemanagement.entities.Notice;
import com.phatnt15.noticemanagement.exceptions.AppGenericException;
import com.phatnt15.noticemanagement.exceptions.NoticeViewFileException;
import com.phatnt15.noticemanagement.helpers.AuthContextHelper;
import com.phatnt15.noticemanagement.mappers.AttachmentMapper;
import com.phatnt15.noticemanagement.repositories.AttachmentRepository;
import com.phatnt15.noticemanagement.repositories.NoticeRepository;
import com.phatnt15.noticemanagement.repositories.UserRepository;
import com.phatnt15.noticemanagement.repositories.wrappers.AttachmentRepoWrapper;
import com.phatnt15.noticemanagement.services.AttachmentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The type Attachment service.
 */
@Service
@AllArgsConstructor
@Slf4j
public class AttachmentServiceImpl implements AttachmentService {

    private final NoticeRepository noticeRepository;

    private final AttachmentRepository attachmentRepository;

    private final UserRepository userRepository;

    private final AttachmentMapper attachmentMapper;

    private AttachmentRepoWrapper attachmentRepoWrapper;

    private final AppConfig appConfig;

    private AuthContextHelper authContextHelper;

    public void createAttachments(UUID noticeId, MultipartFile[] request) {
        List<AttachmentResponse> createdAttachments = new ArrayList<>();
        // get the current working dir
        String currentWorkingDir = System.getProperty("user.dir");
        log.info("current dir = {}", currentWorkingDir);

        Notice notice = noticeRepository.getReferenceById(noticeId);

        for (MultipartFile file : request) {
            try {
                String fileName = StringUtils.cleanPath(file.getOriginalFilename());
                String filePath = appConfig.getUploadDir() + "/" + noticeId;
                Path uploadPath = Paths.get(currentWorkingDir, appConfig.getUploadDir(), noticeId.toString());

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Files.copy(file.getInputStream(), Paths.get(uploadPath.toString(), fileName), StandardCopyOption.REPLACE_EXISTING);

                Attachment attachment = new Attachment();
                attachment.setFileName(fileName);
                attachment.setFilePath(filePath);
                attachment.setContentType(file.getContentType());
                attachment.setNotice(notice);
                attachment.setUser(userRepository.getReferenceById(authContextHelper.getCurrentAuthUserId().get()));

                createdAttachments.add(attachmentMapper.toResponse(attachmentRepository.save(attachment)));
            } catch (Exception e) {
                log.error("Error when creating an attachment, fileName = {}, error = {}", file.getOriginalFilename(), e.getMessage(), e);
            }
        }
    }


    @Cacheable(cacheNames = "attachment", key = "#attachmentId")
    public AttachmentFileResponse getAttachmentFile(UUID attachmentId) {
        Attachment attachment = attachmentRepoWrapper.findById(attachmentId);

        String currentWorkingDir = System.getProperty("user.dir");

        try {
            Path filePath = Paths.get(currentWorkingDir, attachment.getFilePath())
                    .resolve(attachment.getFileName());
            Resource fileResource = new UrlResource(filePath.toUri());

            if (fileResource.exists() && fileResource.isReadable()) {
                AttachmentFileResponse response = new AttachmentFileResponse();
                response.setResource(fileResource);
                response.setContentType(attachment.getContentType());

                return response;
            } else {
                throw new NoticeViewFileException(String.format("file with id [%s] doesn't exist", attachment.getFileName()));
            }
        }
        catch (NoticeViewFileException e) {
            throw e;
        }
        catch (Exception e) {
            log.error("Exception when getting attachment file, file = {}, error = {}", attachment.getFileName(), e.getMessage(), e);
            throw new AppGenericException(String.format("cannot get file [%s] at this time, please try again later", attachment.getFileName()));
        }
    }

    @Cacheable(cacheNames = "attachments", key = "{#noticeId, #pageable.pageNumber, #pageable.pageSize}")
    public Page<AttachmentResponse> getAttachmentsByNoticeId(UUID noticeId, Pageable pageable) {
        return attachmentRepoWrapper.findByNoticeId(noticeId, pageable)
                .map(attachmentMapper::toResponse);
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = "attachments", allEntries = true),
            @CacheEvict(cacheNames = "attachment", key = "#id")
    })
    public void deleteAttachment(UUID id) {
        Attachment attachment = attachmentRepoWrapper.findById(id);

        attachmentRepository.delete(attachment);
    }
}
