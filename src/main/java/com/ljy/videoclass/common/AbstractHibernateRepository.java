package com.ljy.videoclass.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.lang.reflect.Method;
import java.util.Collection;

@Transactional
abstract public class AbstractHibernateRepository<T extends AbstractAggregateRoot> {
    @PersistenceContext
    protected EntityManager entityManager;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    protected void beforePersist(T obj){}
    protected void afterPersist(T obj){}
    public void save(T obj){
        beforePersist(obj);

        if(entityManager.contains(obj)){
            entityManager.merge(obj);
        }else{
            entityManager.persist(obj);
        }

        afterPersist(obj);
        // publish event
        publishEvent(obj);
    }

    private void publishEvent(T aggregate){
        try {
            Collection<Object> domainEvents = getDomainEvents(aggregate);
            domainEvents.forEach(applicationEventPublisher::publishEvent);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 리플렉션을 사용해 aggregate의 domainEvents 목록을 가져옴
    private final String DOMAIN_EVENTS = "domainEvents";
    private Collection<Object> getDomainEvents(T aggregate) throws Exception {
        Method declaredMethod = AbstractAggregateRoot.class.getDeclaredMethod(DOMAIN_EVENTS);
        declaredMethod.setAccessible(true);

        @SuppressWarnings("unchecked")
        Collection<Object> domainEvents = (Collection<Object>) declaredMethod.invoke(aggregate);
        return domainEvents;
    }
}
