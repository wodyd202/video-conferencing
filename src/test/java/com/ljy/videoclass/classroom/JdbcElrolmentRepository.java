package com.ljy.videoclass.classroom;

import com.ljy.videoclass.elrolment.command.ElrolmentRepository;
import com.ljy.videoclass.elrolment.domain.ElrolmentUser;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@Primary
public class JdbcElrolmentRepository implements ElrolmentRepository {
    @PersistenceContext private EntityManager entityManager;

    @Override
    public void save(ElrolmentUser elrolmentUser) {
        if(entityManager.contains(elrolmentUser)){
            entityManager.merge(elrolmentUser);
        }else{
            entityManager.persist(elrolmentUser);
        }
    }
}
