package com.ljy.videoclass.services.conference.command.infrastructure;

import com.ljy.videoclass.common.AbstractHibernateRepository;
import com.ljy.videoclass.services.conference.domain.Conference;
import com.ljy.videoclass.services.conference.domain.ConferenceRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class QuerydslConferenceRepository extends AbstractHibernateRepository<Conference> implements ConferenceRepository {
}
