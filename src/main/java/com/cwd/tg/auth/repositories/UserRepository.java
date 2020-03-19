package com.cwd.tg.auth.repositories;

import com.cwd.tg.auth.data.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    User getByUsername(String username);
}
