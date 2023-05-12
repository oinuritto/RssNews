package ru.itis.rssnews.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.rssnews.dto.RssSourcesPage;
import ru.itis.rssnews.models.RssSource;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RssSourcesRepositoryClearJpa {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void deleteById(Long id) {
        Query query = em.createQuery("DELETE FROM RssSource r WHERE r.id = :source_id");
        query.setParameter("source_id", id);
        query.executeUpdate();
    }

    @Transactional
    public List<RssSource> findAll() {
        TypedQuery<RssSource> query = em.createQuery("SELECT r FROM RssSource r", RssSource.class);
        return query.getResultList();
    }

    @Transactional
    public RssSourcesPage findAll(int page, int pageSize) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<RssSource> query = cb.createQuery(RssSource.class);
        Root<RssSource> root = query.from(RssSource.class);
        query.orderBy(cb.desc(root.get("id")));

        TypedQuery<RssSource> typedQuery = em.createQuery(query);
        typedQuery.setFirstResult((page - 1) * pageSize);
        typedQuery.setMaxResults(pageSize);

        List<RssSource> content = typedQuery.getResultList();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        countQuery.select(cb.count(countQuery.from(RssSource.class)));
        Long total = em.createQuery(countQuery).getSingleResult();
        int pagesCount = (int) Math.ceil((double) total / pageSize);

        return RssSourcesPage.builder()
                .rssSources(content)
                .pageNumber(page)
                .totalPagesCount(pagesCount)
                .build();
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
