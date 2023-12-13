package com.phatnt15.noticemanagement.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phatnt15.noticemanagement.dtos.AttachmentFileResponse;
import com.phatnt15.noticemanagement.dtos.AttachmentResponse;
import com.phatnt15.noticemanagement.filters.JwtAuthFilter;
import com.phatnt15.noticemanagement.helpers.AttachmentAuthGuard;
import com.phatnt15.noticemanagement.helpers.PageableCollectionHelper;
import com.phatnt15.noticemanagement.services.AttachmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(AttachmentController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AttachmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AttachmentService attachmentService;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @MockBean
    private AttachmentAuthGuard attachmentAuthGuard;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void whenGetAttachmentsByNoticeId_shouldOk() throws Exception {
        Page<AttachmentResponse> response = PageableCollectionHelper.generatePageResponse(AttachmentResponse.class, 5);

        when(attachmentService.getAttachmentsByNoticeId(any(UUID.class), any(Pageable.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/attachments/notices/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void whenGetAttachmentFile_shouldOk() throws Exception {
        AttachmentFileResponse response = new AttachmentFileResponse();
        response.setFileName("test-file");
        response.setContentType("image/png");

        when(attachmentService.getAttachmentFile(any(UUID.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/attachments/" + UUID.randomUUID() + "/file")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void whenDeleteAttachment_shouldOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/attachments/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
