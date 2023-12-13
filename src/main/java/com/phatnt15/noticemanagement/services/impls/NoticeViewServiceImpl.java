package com.phatnt15.noticemanagement.services.impls;

import com.phatnt15.noticemanagement.configurations.AppConfig;
import com.phatnt15.noticemanagement.entities.Notice;
import com.phatnt15.noticemanagement.entities.NoticeView;
import com.phatnt15.noticemanagement.helpers.AuthContextHelper;
import com.phatnt15.noticemanagement.repositories.NoticeViewRepository;
import com.phatnt15.noticemanagement.repositories.UserRepository;
import com.phatnt15.noticemanagement.repositories.wrappers.NoticeRepoWrapper;
import com.phatnt15.noticemanagement.services.NoticeViewService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * The type Notice view service.
 */
@Service
@AllArgsConstructor
@Slf4j
public class NoticeViewServiceImpl implements NoticeViewService {

    private final NoticeViewRepository noticeViewRepository;

    private final NoticeRepoWrapper noticeRepoWrapper;

    private final UserRepository userRepository;

    private final AuthContextHelper authContextHelper;

    private final AppConfig appConfig;
    public Long incrementAndCountByNoticeId(UUID noticeId) {
        incrementCountByNoticeId(noticeId);

        return noticeViewRepository.countByNoticeId(noticeId);
    }

    /**
     * Increment count by notice id.
     *
     * @param noticeId the notice id
     */
    private void incrementCountByNoticeId(UUID noticeId) {
        Optional<UUID> currentAuthUserId = authContextHelper.getCurrentAuthUserId();
        Notice targetNotice = noticeRepoWrapper.findById(noticeId);

        if (currentAuthUserId.isEmpty()) {
            return;
        }

        if (!appConfig.getAllowSelfView() && authContextHelper.isSameWithAuthUser(targetNotice.getUser())) {
            log.info("Self view is not allowed");
            return;
        }

        if (!appConfig.getAllowMultipleTimesView() &&
                noticeViewRepository.existsByNoticeIdAndViewerId(targetNotice.getId(), currentAuthUserId.get())) {
            log.info("multiple times view is not allowed");
            return;
        }

        NoticeView noticeView = new NoticeView();
        noticeView.setNotice(targetNotice);
        noticeView.setViewer(userRepository.getReferenceById(currentAuthUserId.get()));

        noticeViewRepository.saveAndFlush(noticeView);
    }

}
