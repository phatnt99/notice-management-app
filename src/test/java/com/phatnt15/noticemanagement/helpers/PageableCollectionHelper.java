package com.phatnt15.noticemanagement.helpers;

import org.springframework.data.domain.*;

import java.util.List;
import java.util.stream.IntStream;

public class PageableCollectionHelper {

    public static Pageable getPageable() {
        return PageRequest.of(0, 100, Sort.by("createdDate").descending());
    }

    public static  <T> Page<T> generatePageResponse(Class<T> clazz, int size) {
        Pageable pageable = PageRequest.of(0, 100, Sort.by("createdDate").descending());

        List<T> resultList = IntStream.range(0, size)
                .mapToObj(i -> {
                    try {
                        return clazz.newInstance();
                    } catch (InstantiationException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }).toList();

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), resultList.size());

        List<T> pageContent = resultList.subList(start, end);

        return new PageImpl<>(pageContent, pageable, resultList.size());
    }
}
