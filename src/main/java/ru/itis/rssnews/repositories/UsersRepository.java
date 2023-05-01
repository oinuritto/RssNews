package ru.itis.rssnews.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.rssnews.models.User;

public interface UsersRepository extends JpaRepository<User, Long> {
}
