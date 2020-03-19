package com.cwd.tg.auth.repositories;

import com.cwd.tg.auth.data.Role;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {
}
