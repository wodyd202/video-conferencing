package com.ljy.videoclass.services.conferenceHistory.command.infrastructure;

import com.ljy.videoclass.common.AbstractHibernateRepository;
import com.ljy.videoclass.services.conferenceHistory.domain.Conference;
import com.ljy.videoclass.services.conferenceHistory.domain.ConferenceRepository;
import org.springframework.stereotype.Repository;

@Repository
public class QuerydslConferenceRepository extends AbstractHibernateRepository<Conference> implements ConferenceRepository {
}
