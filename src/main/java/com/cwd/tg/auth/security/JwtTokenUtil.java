package com.cwd.tg.auth.security;

import com.cwd.tg.auth.data.User;

import io.jsonwebtoken.Claims;

import java.util.Date;
import java.util.function.Function;

public interface JwtTokenUtil {

    String getUsernameFromToken(String token);

    Date getExpirationDateFromToken(String token);

    <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver);

    String generateToken(User userDetails);

    Boolean isTokenExpired(String token);
}
