package com.cwd.tg.auth.handlers;

import com.cwd.tg.auth.data.User;
import com.cwd.tg.auth.output.UserOutputPayload;

import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;


public interface AuthRequestHandler {

    Mono<String> generateUserToken(User cwUser, HttpHeaders headers);

    Mono<UserOutputPayload> validateUserToken(String auth);
}
