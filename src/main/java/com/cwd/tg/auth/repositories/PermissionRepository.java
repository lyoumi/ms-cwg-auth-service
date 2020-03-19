package com.cwd.tg.auth.repositories;

import com.cwd.tg.auth.data.Authority;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Authority, String> {
}
