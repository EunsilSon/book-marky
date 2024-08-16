package com.eunsil.bookmarky.config.filter;

import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FilterManager {

    private final SessionFactory sessionFactory;


    /**
     * 필터 활성화 및 파라미터 설정
     *
     * @param filter 활성화 할 필터 이름
     * @param paramName 파라미터 이름
     * @param paramValue 파라미터 값
     */
    public void enableFilter(String filter, String paramName, Object paramValue) {
        sessionFactory.inTransaction(session -> {
            session.enableFilter(filter)
                    .setParameter(paramName, paramValue)
                    .validate();
        });
    }


    /**
     * 필터 비활성화
     *
     * @param filter 비활성화 할 필터 이름
     */
    public void disableFilter(String filter) {
        sessionFactory.inTransaction(session -> {
            session.disableFilter(filter);
        });
    }

}
