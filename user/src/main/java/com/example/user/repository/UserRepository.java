package com.example.user.repository;

import com.example.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsUserByUsernameAndIsDelete(String username, Boolean isDelete);

    Optional<User> findByUsernameAndIsDelete(String username, Boolean isDelete);

    Optional<User> findByIdAndIsDelete(Long id, Boolean isDelete);
}
