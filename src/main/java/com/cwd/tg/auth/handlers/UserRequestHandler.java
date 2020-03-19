package com.cwd.tg.auth.handlers;

import com.cwd.tg.auth.data.Authority;
import com.cwd.tg.auth.data.Role;
import com.cwd.tg.auth.data.User;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRequestHandler {

    Mono<User> getUserByName(String username);

    Mono<User> createUser(User cwUser);

    Mono<User> updateUser(User cwUser);

    Mono<Void> deleteUserById(String id);

    Flux<User> findAllUsers();

    Mono<User> disableUser(String id);

    Mono<Role> createRole(Role role);

    Mono<Role> updateRole(Role role);

    Mono<Role> getRoleById(String id);

    Mono<Void> deleteRoleById(String id);

    Mono<Authority> createAuthority(Authority authority);

    Mono<Void> deleteAuthorityById(String id);
}
