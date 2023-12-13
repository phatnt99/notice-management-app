package com.phatnt15.noticemanagement.helpers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

/**
 * The type Uri bulding helper.
 */
@Component
@AllArgsConstructor
@Slf4j
public class UriBuldingHelper {

    public String buildSingleObjectUri(String path, UUID id) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(path)
                .buildAndExpand(id)
                .toUri().toString();
    }

    public URI buildControllerObjectUri(String path, UUID id) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .path(path)
                .buildAndExpand(id)
                .toUri();
    }
}
