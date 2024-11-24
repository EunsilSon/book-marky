package com.eunsil.bookmarky.global.config.filter;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FilterManager {

    private Session session;
    private final EntityManager entityManager;

    /**
     * 필터 활성화 및 파라미터 설정
     * @param filter 활성화 할 필터 이름
     * @param paramName 파라미터 이름
     * @param paramValue 파라미터 값
     */
    public void enableFilter(String filter, String paramName, Object paramValue) {
            session = entityManager.unwrap(Session.class);
            session.enableFilter(filter).setParameter(paramName, paramValue);
    }


    /**
     * 필터 비활성화
     * @param filter 비활성화 할 필터 이름
     */
    public void disableFilter(String filter) {
        session.disableFilter(filter);
    }

}
