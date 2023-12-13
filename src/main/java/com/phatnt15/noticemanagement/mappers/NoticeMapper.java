package com.phatnt15.noticemanagement.mappers;

import com.phatnt15.noticemanagement.dtos.NoticeRequest;
import com.phatnt15.noticemanagement.dtos.NoticeResponse;
import com.phatnt15.noticemanagement.entities.Notice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

/**
 * The interface Notice mapper.
 */
@Mapper(componentModel = "spring")
public interface NoticeMapper {
    /**
     * To response notice response.
     *
     * @param entity the entity
     * @return the notice response
     */
    @Mappings({
            @Mapping(target = "registrationDate", source = "createdDate"),
            @Mapping(target = "author", source = "createdBy")
    })
    NoticeResponse toResponse(Notice entity);

    /**
     * To entity notice.
     *
     * @param request the request
     * @return the notice
     */
    Notice toEntity(NoticeRequest request);

    /**
     * Update entity.
     *
     * @param entity  the entity
     * @param request the request
     */
    @Mapping(target = "id", ignore = true)
    void updateEntity(@MappingTarget Notice entity, NoticeRequest request);
}
