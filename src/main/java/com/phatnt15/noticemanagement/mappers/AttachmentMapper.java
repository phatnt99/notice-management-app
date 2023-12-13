package com.phatnt15.noticemanagement.mappers;

import com.phatnt15.noticemanagement.dtos.AttachmentResponse;
import com.phatnt15.noticemanagement.entities.Attachment;
import org.mapstruct.Mapper;

/**
 * The interface Attachment mapper.
 */
@Mapper(componentModel = "spring")
public interface AttachmentMapper {

    /**
     * To response attachment response.
     *
     * @param entity the entity
     * @return the attachment response
     */
    AttachmentResponse toResponse(Attachment entity);
}
