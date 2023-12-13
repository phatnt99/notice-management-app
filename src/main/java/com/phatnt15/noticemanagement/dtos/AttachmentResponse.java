package com.phatnt15.noticemanagement.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

/**
 * The type Attachment response.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AttachmentResponse implements Serializable {

    private UUID id;

    private String fileName;

    private String filePath;

    private String contentType;
}
