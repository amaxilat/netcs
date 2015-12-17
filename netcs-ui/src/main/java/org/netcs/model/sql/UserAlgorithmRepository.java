package org.netcs.model.sql;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface UserAlgorithmRepository extends JpaRepository<UserAlgorithm, Long> {
    Set<UserAlgorithm> findByUser(User user);
}
