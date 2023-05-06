//package ru.itis.rssnews.repositories;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//import ru.itis.rssnews.models.RssSource;
//import ru.itis.rssnews.models.Article;
//
//@Repository
//public interface RssSourcesRepository extends JpaRepository<RssSource, Long> {
//    @Modifying
//    @Query("DELETE FROM RssSource r WHERE r.id = :sourceId")
//    void deleteBySourceId(@Param("sourceId") Long sourceId);
//}
