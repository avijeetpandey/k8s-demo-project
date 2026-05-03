package com.avijeet.k8sdemoproject.repository;

import com.avijeet.k8sdemoproject.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
