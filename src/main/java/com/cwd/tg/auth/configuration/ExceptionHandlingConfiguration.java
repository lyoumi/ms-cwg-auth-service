package com.cwd.tg.auth.configuration;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

import com.cwd.tg.auth.errors.exceptions.MissingHeaderException;
import com.cwd.tg.auth.errors.exceptions.TokenExpirationException;
import com.cwd.tg.auth.errors.exceptions.TokenValidationException;
import com.cwd.tg.auth.errors.handlers.CommonExceptionHandler;
import com.cwd.tg.auth.errors.http.HttpErrorMessage;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@Configuration
public class ExceptionHandlingConfiguration {

    private static final String DELIMITER = "; ";
    private static final Map<Class<? extends Throwable>, Function<Throwable, Mono<ServerResponse>>> EXCEPTION_MAPPINGS =
            Map.of(
                    MissingHeaderException.class, exception ->
                            ServerResponse.status(BAD_REQUEST)
                                    .body(fromObject(new HttpErrorMessage(1034000,
                                            BAD_REQUEST.getReasonPhrase(),
                                            exception.getMessage()))),

                    TokenExpirationException.class, exception ->
                            ServerResponse.status(UNAUTHORIZED)
                                    .body(fromObject(new HttpErrorMessage(1034010, UNAUTHORIZED.getReasonPhrase(),
                                            exception.getMessage()))),

                    TokenValidationException.class, exception ->
                            ServerResponse.status(FORBIDDEN)
                                    .body(fromObject(new HttpErrorMessage(1034030, FORBIDDEN.getReasonPhrase(),
                                            FORBIDDEN.getReasonPhrase()))),

                    ConstraintViolationException.class, exception ->
                            ServerResponse.status(UNPROCESSABLE_ENTITY)
                                    .body(fromObject(new HttpErrorMessage(1034220,
                                            UNPROCESSABLE_ENTITY.getReasonPhrase(),
                                            ((ConstraintViolationException) exception).getConstraintViolations()
                                                    .stream()
                                                    .map(ConstraintViolation::getMessage)
                                                    .collect(Collectors.joining(DELIMITER)))))
            );

    @Bean
    @Primary
    public AbstractErrorWebExceptionHandler exceptionHandler(
            ErrorAttributes errorAttributes,
            ResourceProperties resourceProperties,
            ApplicationContext applicationContext,
            ObjectProvider<ViewResolver> viewResolvers,
            ServerCodecConfigurer serverCodecConfigurer) {

        var exceptionHandler = new CommonExceptionHandler(errorAttributes,
                resourceProperties, applicationContext, EXCEPTION_MAPPINGS);
        exceptionHandler.setViewResolvers(viewResolvers.orderedStream().collect(Collectors.toList()));
        exceptionHandler.setMessageWriters(serverCodecConfigurer.getWriters());
        exceptionHandler.setMessageReaders(serverCodecConfigurer.getReaders());
        return exceptionHandler;
    }
}
