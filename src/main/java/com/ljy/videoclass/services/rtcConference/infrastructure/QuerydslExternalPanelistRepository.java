package com.ljy.videoclass.services.rtcConference.infrastructure;

import com.ljy.videoclass.services.rtcConference.application.external.ExternalPanelistRepository;
import com.ljy.videoclass.services.rtcConference.application.external.Panelist;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.ljy.videoclass.services.panelist.domain.QPanelist.panelist;
import static com.querydsl.core.types.Projections.constructor;

@Repository
public class QuerydslExternalPanelistRepository implements ExternalPanelistRepository {
    @Autowired private JPAQueryFactory jpaQueryFactory;
    @Override
    public Panelist getPanelist(String panelistId) {
        return jpaQueryFactory.select(constructor(Panelist.class,
                        panelist.id().value,
                        panelist.status))
                .from(panelist)
                .where(panelist.id().value.eq(panelistId))
                .fetchFirst();
    }
}
