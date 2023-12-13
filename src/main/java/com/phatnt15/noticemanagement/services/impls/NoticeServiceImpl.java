package com.phatnt15.noticemanagement.services.impls;

import com.phatnt15.noticemanagement.dtos.NoticeRequest;
import com.phatnt15.noticemanagement.dtos.NoticeResponse;
import com.phatnt15.noticemanagement.dtos.NoticeSearchRequest;
import com.phatnt15.noticemanagement.entities.Notice;
import com.phatnt15.noticemanagement.helpers.UriBuldingHelper;
import com.phatnt15.noticemanagement.mappers.NoticeMapper;
import com.phatnt15.noticemanagement.repositories.NoticeRepository;
import com.phatnt15.noticemanagement.repositories.wrappers.NoticeRepoWrapper;
import com.phatnt15.noticemanagement.services.AttachmentService;
import com.phatnt15.noticemanagement.services.NoticeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * The type Notice service.
 */
@Service
@AllArgsConstructor
@Slf4j
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;

    private final NoticeMapper noticeMapper;

    private final AttachmentService attachmentService;

    private final NoticeRepoWrapper noticeRepoWrapper;

    private final UriBuldingHelper uriBuldingHelper;

    @Override
    @Cacheable(cacheNames = "notices", key = "{#pageable.pageNumber, #pageable.pageSize}")
    public Page<NoticeResponse> getAllNotices(Pageable pageable) {
        return noticeRepoWrapper.findAll(pageable)
                .map(noticeMapper::toResponse);
    }

    @Override
    @Cacheable(cacheNames = "notice", key = "#id")
    public NoticeResponse getNoticeById(UUID id) {
        Notice notice = noticeRepoWrapper.findById(id);

        notice.setNumberOfViewsUrl(uriBuldingHelper.buildSingleObjectUri("/api/v1/views/with-increment/notices/{id}", id));

        return noticeMapper.toResponse(notice);
    }

    public Page<NoticeResponse> searchNotice(NoticeSearchRequest searchRequest, Pageable pageable) {
        return noticeRepoWrapper.findAll(
                Specification.where(noticeRepository.titleContains(searchRequest.getTitle()))
                        .and(noticeRepository.contentContains(searchRequest.getContent()))
                        .and(noticeRepository.startTimeFrom(searchRequest.getStartTime()))
                        .and(noticeRepository.endTimeTo(searchRequest.getEndTime())), pageable)
                .map(noticeMapper::toResponse);
    }

    @Override
    @CachePut(cacheNames = "notice", key = "#result.id")
    @CacheEvict(cacheNames = "notices", allEntries = true)
    public NoticeResponse createNotice(NoticeRequest request, MultipartFile[] attachments) {
        Notice notice = noticeRepository.save(noticeMapper.toEntity(request));
        // create attachments
        attachmentService.createAttachments(notice.getId(), attachments);

        return noticeMapper.toResponse(notice);
    }

    @Override
    @CachePut(cacheNames = "notice", key = "#id")
    @CacheEvict(cacheNames = "notices", allEntries = true)
    public NoticeResponse updateNotice(UUID id, NoticeRequest request) {
        Notice notice = noticeRepoWrapper.findById(id);

        noticeMapper.updateEntity(notice, request);

        return noticeMapper.toResponse(noticeRepository.save(notice));
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "notices", allEntries = true),
            @CacheEvict(cacheNames = "attachments", allEntries = true),
            @CacheEvict(cacheNames = "notice", key = "#id"),
    })
    public void deleteNotice(UUID id) {
        Notice notice = noticeRepoWrapper.findById(id);

        noticeRepository.delete(notice);
    }
}
