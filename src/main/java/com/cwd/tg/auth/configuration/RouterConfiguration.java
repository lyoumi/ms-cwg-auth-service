package com.cwd.tg.auth.configuration;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import com.cwd.tg.auth.data.Authority;
import com.cwd.tg.auth.data.Role;
import com.cwd.tg.auth.data.User;
import com.cwd.tg.auth.handlers.AuthRequestHandler;
import com.cwd.tg.auth.handlers.UserRequestHandler;
import com.cwd.tg.auth.output.UserOutputPayload;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest.Headers;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Configuration
public class RouterConfiguration {

    @Bean
    public RouterFunction routerFunction(
            UserRequestHandler userRequestHandler,
            AuthRequestHandler authRequestHandler) {
        return
                //temporary routes
                route(GET("/private/auth/user/name/{name}"), request ->
                        ServerResponse.ok().body(Mono.justOrEmpty(request.pathVariable("name"))
                                .flatMap(userRequestHandler::getUserByName), User.class))
                        .andRoute(GET("/private/auth/user/all"), request ->
                                ServerResponse.ok().body(userRequestHandler.findAllUsers(), User.class))
                        .andRoute(POST("/private/auth/user"), request ->
                                ServerResponse.ok().body(request.bodyToMono(User.class)
                                        .flatMap(userRequestHandler::createUser), User.class))
                        .andRoute(PUT("/private/auth/user"), request ->
                                ServerResponse.ok().body(request.bodyToMono(User.class)
                                        .flatMap(userRequestHandler::updateUser), User.class))
                        .andRoute(PUT("/private/auth/user/disable/{id}"), request ->
                                ServerResponse.ok().body(Mono.justOrEmpty(request.pathVariable("id"))
                                        .flatMap(userRequestHandler::disableUser), User.class))
                        .andRoute(DELETE("/private/auth/user/{id}"), request ->
                                ServerResponse.noContent().build(Mono.justOrEmpty(request.pathVariable("id"))
                                        .flatMap(userRequestHandler::deleteUserById)))

                        .andRoute(POST("/private/auth/permission"), request ->
                                ServerResponse.ok().body(request.bodyToMono(Authority.class)
                                        .flatMap(userRequestHandler::createAuthority), Authority.class))
                        .andRoute(DELETE("/private/auth/permission/{id}"), request ->
                                ServerResponse.noContent().build(Mono.justOrEmpty(request.pathVariable("id"))
                                        .flatMap(userRequestHandler::deleteAuthorityById)))

                        .andRoute(POST("/private/auth/role"), request ->
                                ServerResponse.ok().body(request.bodyToMono(Role.class)
                                        .flatMap(userRequestHandler::createRole), Role.class))
                        .andRoute(PUT("/private/auth/role"), request ->
                                ServerResponse.ok().body(request.bodyToMono(Role.class)
                                        .flatMap(userRequestHandler::updateRole), Role.class))
                        .andRoute(GET("/private/auth/role/{id}"), request ->
                                ServerResponse.ok().body(Mono.justOrEmpty(request.pathVariable("id"))
                                        .flatMap(userRequestHandler::getRoleById), Role.class))
                        .andRoute(DELETE("/private/auth/role/{id}"), request ->
                                ServerResponse.noContent().build(Mono.justOrEmpty(request.pathVariable("id"))
                                        .flatMap(userRequestHandler::deleteRoleById)))

                        .andRoute(POST("/private/auth/generate"), request ->
                                ServerResponse.ok().body(request.bodyToMono(User.class).flatMap(user ->
                                                authRequestHandler.generateUserToken(user, request.headers().asHttpHeaders())),
                                        String.class))
                        .andRoute(GET("/private/auth/validate"), request ->
                                ServerResponse.ok().body(Mono.just(request.headers())
                                        .map(Headers::asHttpHeaders)
                                        .map(httpHeaders -> httpHeaders.get("Authorization"))
                                        .map(headers -> headers.get(0))
                                        .flatMap(authRequestHandler::validateUserToken), UserOutputPayload.class));
    }
}
