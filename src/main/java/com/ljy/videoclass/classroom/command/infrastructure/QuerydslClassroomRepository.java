package com.ljy.videoclass.classroom;

import com.ljy.videoclass.classroom.domain.Classroom;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
@Transactional
public class QuerydslClassroomRepository implements ClassroomRepository{
    @PersistenceContext private EntityManager entityManager;

    @Override
    public void save(Classroom classroom) {
        if(entityManager.contains(classroom)){
            entityManager.merge(classroom);
        }else{
            entityManager.persist(classroom);
        }
    }
}
