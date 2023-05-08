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
    public void deleteById(Long id) {
//        Query queryForArticlesDelete = em.createQuery("DELETE FROM articles a WHERE a.source = :source_id");
//        Query queryForSourceDelete = em.createQuery("DELETE FROM sources r WHERE r.id = :source_id");
//        queryForArticlesDelete.setParameter("source_id", sourceId);
//        queryForSourceDelete.setParameter("source_id", sourceId);
//        queryForArticlesDelete.executeUpdate();
//        queryForSourceDelete.executeUpdate();

        Query query = em.createQuery("DELETE FROM sources r WHERE r.id = :source_id");
        query.setParameter("source_id", id);
        query.executeUpdate();
    }

    @Transactional
    public List<RssSource> findAll() {
        TypedQuery<RssSource> query = em.createQuery("SELECT r FROM sources r", RssSource.class);
        return query.getResultList();
    }

    @Transactional
    public RssSource update(RssSource rssSource) {
        return em.merge(rssSource);
    }

    @Transactional
    public RssSource save(RssSource rssSource) {
        em.persist(rssSource);
        return rssSource;
    }

    @Transactional
    public RssSource findById(Long id) {
        return em.find(RssSource.class, id);
    }
}
