package com.ljy.videoclass.services.panelist.infrastructure;

import com.ljy.videoclass.services.panelist.Email;
import com.ljy.videoclass.services.panelist.Panelist;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
@Transactional
public class QuerydslPanelistRepository implements PanelistRepository {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Panelist> findById(Email email) {
        return Optional.empty();
    }

    @Override
    public void save(Panelist panelist) {
        if(entityManager.contains(panelist)){
            entityManager.merge(panelist);
        }else{
            entityManager.persist(panelist);
        }
    }
}
