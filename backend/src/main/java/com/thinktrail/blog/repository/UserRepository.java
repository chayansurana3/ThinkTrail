package com.thinktrail.blog.repository;

import com.thinktrail.blog.model.Role;
import com.thinktrail.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByFirstName(String firstName);
    List<User> findByFirstNameAndLastName(String firstName, String lastName);
    List<User> findByRole(Role role);
    List<User> findByBioContaining(String keyword);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}