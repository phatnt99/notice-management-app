package com.phatnt15.noticemanagement.services;

import com.phatnt15.noticemanagement.dtos.NoticeRequest;
import com.phatnt15.noticemanagement.dtos.NoticeResponse;
import com.phatnt15.noticemanagement.dtos.NoticeSearchRequest;
import com.phatnt15.noticemanagement.entities.Notice;
import com.phatnt15.noticemanagement.helpers.PageableCollectionHelper;
import com.phatnt15.noticemanagement.helpers.UriBuldingHelper;
import com.phatnt15.noticemanagement.mappers.NoticeMapper;
import com.phatnt15.noticemanagement.repositories.NoticeRepository;
import com.phatnt15.noticemanagement.repositories.wrappers.NoticeRepoWrapper;
import com.phatnt15.noticemanagement.services.impls.NoticeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NoticeServiceTest {

    @InjectMocks
    private NoticeServiceImpl noticeService;

    @Mock
    private NoticeRepository noticeRepository;

    @Mock
    private NoticeMapper noticeMapper;

    @Mock
    private AttachmentService attachmentService;

    @Mock
    private NoticeRepoWrapper noticeRepoWrapper;

    @Mock
    private UriBuldingHelper uriBuldingHelper;

    int listSize = 5;

    @Test
    public void whenGetAllNotices_shouldOk() {
        Pageable pageable = PageableCollectionHelper.getPageable();

        when(noticeRepoWrapper.findAll(any(Pageable.class)))
                .thenReturn(PageableCollectionHelper.generatePageResponse(Notice.class, listSize));
        when(noticeMapper.toResponse(any(Notice.class))).thenReturn(new NoticeResponse());

        Page<NoticeResponse> result = noticeService.getAllNotices(pageable);

        assertEquals(result.getTotalElements(), 5);
    }

    @Test
    public void whenGetNoticeById_shouldOk() {
        UUID request = UUID.randomUUID();
        Notice entity = new Notice();

        NoticeResponse response = new NoticeResponse();
        response.setId(request);

        when(noticeRepoWrapper.findById(any(UUID.class))).thenReturn(entity);
        when(noticeMapper.toResponse(any(Notice.class))).thenReturn(response);

        NoticeResponse result = noticeService.getNoticeById(request);

        assertEquals(request, result.getId());
    }

    @Test
    public void whenSearchNotice_shouldOk() {
        Pageable pageable = PageableCollectionHelper.getPageable();
        NoticeSearchRequest request = new NoticeSearchRequest();

        when(noticeRepoWrapper.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(PageableCollectionHelper.generatePageResponse(Notice.class, listSize));
        when(noticeMapper.toResponse(any(Notice.class))).thenReturn(new NoticeResponse());

        Page<NoticeResponse> result = noticeService.searchNotice(request, pageable);

        assertEquals(result.getTotalElements(), 5);
    }

    @Test
    public void whenCreateNotice_shouldOk() {
        Notice entity = new Notice();
        entity.setId(UUID.randomUUID());

        NoticeResponse response = new NoticeResponse();
        response.setId(entity.getId());

        when(noticeMapper.toEntity(any(NoticeRequest.class))).thenReturn(entity);
        when(noticeRepository.save(any(Notice.class))).thenReturn(entity);
        when(noticeMapper.toResponse(any(Notice.class))).thenReturn(response);

        NoticeResponse result = noticeService.createNotice(new NoticeRequest(), null);

        assertEquals(result.getId(), entity.getId());
    }

    @Test
    public void whenUpdateNotice_shouldOk() {
        UUID updatingId = UUID.randomUUID();

        Notice entity = new Notice();
        entity.setId(updatingId);

        NoticeResponse response = new NoticeResponse();
        response.setId(entity.getId());

        when(noticeRepoWrapper.findById(any(UUID.class))).thenReturn(entity);
        when(noticeRepository.save(any(Notice.class))).thenReturn(entity);
        when(noticeMapper.toResponse(any(Notice.class))).thenReturn(response);


        NoticeResponse result = noticeService.updateNotice(updatingId, new NoticeRequest());

        assertEquals(result.getId(), entity.getId());
    }


    @Test
    public void whenDeleteNotice_shouldOk() {
        UUID updatingId = UUID.randomUUID();

        Notice entity = new Notice();
        entity.setId(updatingId);

        lenient().when(noticeRepoWrapper.findById(any(UUID.class))).thenReturn(entity);
        
        noticeService.deleteNotice(updatingId);
    }
}
