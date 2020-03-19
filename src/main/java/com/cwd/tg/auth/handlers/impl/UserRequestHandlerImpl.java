package com.cwd.tg.auth.handlers.impl;

import com.cwd.tg.auth.data.Authority;
import com.cwd.tg.auth.data.Role;
import com.cwd.tg.auth.data.User;
import com.cwd.tg.auth.errors.exceptions.UserNotFoundException;
import com.cwd.tg.auth.handlers.UserRequestHandler;
import com.cwd.tg.auth.repositories.PermissionRepository;
import com.cwd.tg.auth.repositories.RoleRepository;
import com.cwd.tg.auth.repositories.UserRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class UserRequestHandlerImpl implements UserRequestHandler {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    public Mono<User> getUserByName(String username) {
        return Mono.just(userRepository.getByUsername(username));
    }

    @Override
    public Mono<User> createUser(User cwUser) {
        return Mono.just(userRepository.saveAndFlush(cwUser));
    }

    @Override
    public Mono<User> updateUser(User cwUser) {
        return Mono.just(userRepository.saveAndFlush(cwUser));
    }

    @Override
    public Mono<Void> deleteUserById(String id) {
        return Mono.fromRunnable(() -> userRepository.deleteById(id));
    }

    @Override
    public Flux<User> findAllUsers() {
        return Flux.fromIterable(userRepository.findAll());
    }

    @Override
    public Mono<User> disableUser(String id) {
        return Mono.fromCallable(() -> userRepository.getOne(id))
                .switchIfEmpty(Mono.error(new UserNotFoundException(String.format("User with id %s not found", id))))
                .doOnSuccess(user -> user.setEnabled(false));
    }

    @Override
    public Mono<Role> createRole(Role role) {
        return Mono.just(roleRepository.saveAndFlush(role));
    }

    @Override
    public Mono<Role> updateRole(Role role) {
        return Mono.just(roleRepository.saveAndFlush(role));
    }

    @Override
    public Mono<Role> getRoleById(String id) {
        return Mono.just(roleRepository.getOne(id));
    }

    @Override
    public Mono<Void> deleteRoleById(String id) {
        return Mono.fromRunnable(() -> roleRepository.deleteById(id));
    }

    @Override
    public Mono<Authority> createAuthority(Authority authority) {
        return Mono.just(permissionRepository.saveAndFlush(authority));
    }

    @Override
    public Mono<Void> deleteAuthorityById(String id) {
        return Mono.fromRunnable(() -> permissionRepository.deleteById(id));
    }
}
