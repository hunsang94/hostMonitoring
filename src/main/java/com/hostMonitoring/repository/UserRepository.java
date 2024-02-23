package com.hostMonitoring.repository;

import com.hostMonitoring.dto.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserInfo, String> {
    public Optional<UserInfo> findById(String id);
}
