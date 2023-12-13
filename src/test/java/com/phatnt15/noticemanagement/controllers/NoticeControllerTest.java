package com.phatnt15.noticemanagement.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.phatnt15.noticemanagement.dtos.NoticeRequest;
import com.phatnt15.noticemanagement.dtos.NoticeResponse;
import com.phatnt15.noticemanagement.dtos.NoticeSearchRequest;
import com.phatnt15.noticemanagement.filters.JwtAuthFilter;
import com.phatnt15.noticemanagement.helpers.NoticeAuthGuard;
import com.phatnt15.noticemanagement.helpers.PageableCollectionHelper;
import com.phatnt15.noticemanagement.helpers.UriBuldingHelper;
import com.phatnt15.noticemanagement.mappers.NoticeMapper;
import com.phatnt15.noticemanagement.services.NoticeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;

@WebMvcTest(NoticeController.class)
@AutoConfigureMockMvc(addFilters = false)
public class NoticeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoticeService noticeService;

    @MockBean
    private NoticeAuthGuard noticeAuthGuard;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @MockBean
    private NoticeMapper noticeMapper;

    @MockBean
    private UriBuldingHelper uriBuldingHelper;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void whenGetAllNotices_shouldOk() throws Exception {
        Page<NoticeResponse> notices = PageableCollectionHelper.generatePageResponse(NoticeResponse.class, 5);

        when(noticeService.getAllNotices(any(Pageable.class))).thenReturn(notices);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/notices")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void whenGetNoticeById_shouldOk() throws Exception {
        NoticeResponse response = new NoticeResponse();
        response.setId(UUID.randomUUID());

        when(noticeService.getNoticeById(any(UUID.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/notices/" + response.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message.id").value(response.getId().toString()));
    }

    @Test
    public void whenSearchNotice_shouldOk() throws Exception {
        NoticeSearchRequest request = new NoticeSearchRequest();
        Page<NoticeResponse> notices = PageableCollectionHelper.generatePageResponse(NoticeResponse.class, 5);

        when(noticeService.searchNotice(any(NoticeSearchRequest.class), any(Pageable.class))).thenReturn(notices);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/notices/query")
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void whenCreateNotice_shouldOk() throws Exception {
        mapper.registerModule(new JavaTimeModule());

        NoticeRequest request = new NoticeRequest();
        request.setTitle("title");
        request.setContent("content");
        request.setStartTime(LocalDateTime.now().plusDays(1));
        request.setEndTime(LocalDateTime.now().plusDays(2));

        MockMultipartFile fileRequest
                = new MockMultipartFile(
                "request",
                "request",
                MediaType.APPLICATION_JSON_VALUE,
                mapper.writeValueAsString(request).getBytes()
        );

        NoticeResponse response = new NoticeResponse();
        response.setId(UUID.randomUUID());

        when(noticeService.createNotice(any(NoticeRequest.class), nullable(MultipartFile[].class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/notices")
                        .file(fileRequest)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message.id").value(response.getId().toString()));
    }


    @Test
    public void whenCreateNotice_withBadRequest_shouldFail() throws Exception {
        mapper.registerModule(new JavaTimeModule());

        NoticeRequest request = new NoticeRequest();

        MockMultipartFile fileRequest
                = new MockMultipartFile(
                "request",
                "request",
                MediaType.APPLICATION_JSON_VALUE,
                mapper.writeValueAsString(request).getBytes()
        );

        NoticeResponse response = new NoticeResponse();
        response.setId(UUID.randomUUID());

        when(noticeService.createNotice(any(NoticeRequest.class), nullable(MultipartFile[].class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/notices")
                        .file(fileRequest)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message.title").value("must not be blank"));
    }

    @Test
    public void whenUpdateNotice_shouldOk() throws Exception {
        mapper.registerModule(new JavaTimeModule());

        NoticeRequest request = new NoticeRequest();
        request.setTitle("title");
        request.setContent("content");
        request.setStartTime(LocalDateTime.now().plusDays(1));
        request.setEndTime(LocalDateTime.now().plusDays(2));


        NoticeResponse response = new NoticeResponse();
        response.setId(UUID.randomUUID());

        when(noticeService.updateNotice(any(UUID.class), any(NoticeRequest.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/notices/" + response.getId())
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message.title").value(response.getTitle()));
    }

    @Test
    public void whenUpdateNotice_withBadRequest_shouldFail() throws Exception {
        mapper.registerModule(new JavaTimeModule());

        NoticeRequest request = new NoticeRequest();


        NoticeResponse response = new NoticeResponse();
        response.setId(UUID.randomUUID());

        when(noticeService.updateNotice(any(UUID.class), any(NoticeRequest.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/notices/" + response.getId())
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message.title").value("must not be blank"));
    }

    @Test
    public void whenDeleteNotice_shouldOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/notices/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
