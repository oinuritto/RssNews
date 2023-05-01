package ru.itis.rssnews.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.rssnews.models.User;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {
    boolean existsUserByEmail(String email);
}
