package com.hostMonitoring.repository;

import com.hostMonitoring.dto.Host;
import com.hostMonitoring.dto.HostPk;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HostRepository extends JpaRepository<Host, HostPk> {
}
