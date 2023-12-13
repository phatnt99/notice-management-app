package com.phatnt15.noticemanagement.services;

import com.phatnt15.noticemanagement.configurations.AppConfig;
import com.phatnt15.noticemanagement.entities.Notice;
import com.phatnt15.noticemanagement.entities.User;
import com.phatnt15.noticemanagement.helpers.AuthContextHelper;
import com.phatnt15.noticemanagement.repositories.NoticeViewRepository;
import com.phatnt15.noticemanagement.repositories.UserRepository;
import com.phatnt15.noticemanagement.repositories.wrappers.NoticeRepoWrapper;
import com.phatnt15.noticemanagement.services.impls.NoticeViewServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NoticeViewServiceTest {

    @InjectMocks
    NoticeViewServiceImpl noticeViewService;

    @Mock
    private NoticeViewRepository noticeViewRepository;

    @Mock
    private NoticeRepoWrapper noticeRepoWrapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthContextHelper authContextHelper;

    @Mock
    private AppConfig appConfig;

    @Test
    public void whenIncrementAndCountByNoticeId_shouldOk() {
        UUID noticeId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Long count = 1L;

        when(noticeViewRepository.countByNoticeId(any(UUID.class))).thenReturn(count);
        when(authContextHelper.getCurrentAuthUserId()).thenReturn(Optional.of(userId));
        when(noticeRepoWrapper.findById(any(UUID.class))).thenReturn(new Notice());
        when(appConfig.getAllowSelfView()).thenReturn(true);
        when(appConfig.getAllowMultipleTimesView()).thenReturn(true);

        Long result = noticeViewService.incrementAndCountByNoticeId(noticeId);

        assertEquals(result, count);
    }

    @Test
    public void whenIncrementAndCountByNoticeId_withNotAllowSelfView_shouldOk() {
        UUID noticeId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Long count = 1L;

        Notice notice = new Notice();
        notice.setUser(new User());

        when(noticeViewRepository.countByNoticeId(any(UUID.class))).thenReturn(count);
        when(authContextHelper.getCurrentAuthUserId()).thenReturn(Optional.of(userId));
        when(authContextHelper.isSameWithAuthUser(any(User.class))).thenReturn(true);
        when(noticeRepoWrapper.findById(any(UUID.class))).thenReturn(notice);
        when(appConfig.getAllowSelfView()).thenReturn(false);

        Long result = noticeViewService.incrementAndCountByNoticeId(noticeId);

        assertEquals(result, count);
    }

    @Test
    public void whenIncrementAndCountByNoticeId_withNotAllowMultipleTimesView_shouldOk() {
        UUID noticeId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Long count = 1L;

        Notice notice = new Notice();
        notice.setId(noticeId);

        when(noticeViewRepository.countByNoticeId(any(UUID.class))).thenReturn(count);
        when(authContextHelper.getCurrentAuthUserId()).thenReturn(Optional.of(userId));
        when(noticeRepoWrapper.findById(any(UUID.class))).thenReturn(notice);
        when(appConfig.getAllowSelfView()).thenReturn(true);
        when(appConfig.getAllowMultipleTimesView()).thenReturn(false);
        when(noticeViewRepository.existsByNoticeIdAndViewerId(any(UUID.class), any(UUID.class))).thenReturn(true);

        Long result = noticeViewService.incrementAndCountByNoticeId(noticeId);

        assertEquals(result, count);
    }
}
