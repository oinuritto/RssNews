package ru.itis.rssnews.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.rssnews.models.RssSource;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RssSourcesRepositoryClearJpa {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void deleteBySourceId(Long sourceId) {
        Query query = em.createQuery("DELETE FROM sources r WHERE r.id = :source_id");
        query.setParameter("source_id", sourceId);
        query.executeUpdate();
    }

    @Transactional
    public List<RssSource> findAll() {
        TypedQuery<RssSource> query = em.createQuery("SELECT r FROM sources r", RssSource.class);
        return query.getResultList();
    }
}
