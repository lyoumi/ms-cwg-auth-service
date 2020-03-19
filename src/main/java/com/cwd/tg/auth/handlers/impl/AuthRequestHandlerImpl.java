package com.cwd.tg.auth.handlers.impl;

import com.cwd.tg.auth.data.Authority;
import com.cwd.tg.auth.data.User;
import com.cwd.tg.auth.errors.exceptions.AccessDeniedException;
import com.cwd.tg.auth.errors.exceptions.TokenExpirationException;
import com.cwd.tg.auth.handlers.AuthRequestHandler;
import com.cwd.tg.auth.output.UserOutputPayload;
import com.cwd.tg.auth.repositories.UserRepository;
import com.cwd.tg.auth.security.JwtTokenUtil;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class AuthRequestHandlerImpl implements AuthRequestHandler {

    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public Mono<String> generateUserToken(User cwUser, HttpHeaders headers) {
        return Mono.just(cwUser.getUsername())
                .map(userRepository::getByUsername)
                .filter(user -> user.getPassword().equals(cwUser.getPassword()))
                .switchIfEmpty(Mono.error(new AccessDeniedException("Incorrect username or password")))
                .map(jwtTokenUtil::generateToken);
    }

    @Override
    public Mono<UserOutputPayload> validateUserToken(String token) {
        return Mono.just(token)
                .filter(userToken -> !jwtTokenUtil.isTokenExpired(userToken))
                .switchIfEmpty(Mono.error(new TokenExpirationException(String.format("User token %s was expired. Please, generate new token", token))))
                .map(jwtTokenUtil::getUsernameFromToken)
                .map(userRepository::getByUsername)
                .map(cwUser ->
                    new UserOutputPayload(cwUser.getUsername(), cwUser.getRoles().stream()
                            .flatMap(role -> role.getPermissions().stream())
                            .map(Authority::getPermission)
                            .collect(Collectors.toSet())));
    }
}
