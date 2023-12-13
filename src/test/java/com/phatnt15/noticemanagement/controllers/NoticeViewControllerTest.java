package com.phatnt15.noticemanagement.controllers;

import com.phatnt15.noticemanagement.filters.JwtAuthFilter;
import com.phatnt15.noticemanagement.services.NoticeViewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(NoticeViewController.class)
@AutoConfigureMockMvc(addFilters = false)
public class NoticeViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoticeViewService noticeViewService;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @Test
    public void whenIncrementAndReturnNoticeView_shouldOk() throws Exception {
        Long response = 1L;
        when(noticeViewService.incrementAndCountByNoticeId(any(UUID.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/views/with-increment/notices/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(response));

    }
}
