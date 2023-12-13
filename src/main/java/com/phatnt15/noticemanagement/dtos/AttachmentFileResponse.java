package com.phatnt15.noticemanagement.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.core.io.Resource;

import java.io.Serializable;

/**
 * The type Attachment file response.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AttachmentFileResponse implements Serializable {

    private Resource resource;

    private String fileName;

    private String contentType;
}
